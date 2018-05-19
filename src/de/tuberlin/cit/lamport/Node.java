package de.tuberlin.cit.lamport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * - node possesses an inbox queue where internal and external messages are stored
 * - internal messages from the history are written in Thread-specific log files
 * @author Christian Mischok
 *
 */
public class Node extends Thread {
	
    private static int nextId = 0;
    // using deque because of iterating backwards
    private LinkedBlockingDeque<Message> inbox = new LinkedBlockingDeque<>();
    // every node has history for storing broadcastet internal messages
    private List<InternalMessage> history = new ArrayList<>();
    // corresponds to the id of the lamport timestamp
    private int nodeId;
    private Node[] nodes;

	public Node() {
        this.nodeId = nextId;
        this.inbox = new LinkedBlockingDeque<>();
        nextId++;
    }
	
	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}
	
	public int getNodeId() {
		return this.nodeId; 
	}

	/**
	 * - external messages are converted into internal messages
	 * - internal messages are broadcastet to all other nodes
	 * 
	 * @param externalMessage
	 */
    public void broadcast(ExternalMessage externalMessage) {
        InternalMessage intMsg = new InternalMessage(
        		externalMessage.getCounter(), this.getNodeId(), externalMessage.getMessageId());
        Arrays.stream(nodes).forEach(node -> node.insertMessage(intMsg));

    }
    
    /**
     * - inbox is synchronized because multiple nodes store concurrently external and 
     *   internal messages in the inbox (read and write operation are performed on the inbox)
     * - compares the counter of the last internal message with the current internal message to compute the maximum
     * @param message
     */
    public void insertMessage(Message message) {
            synchronized (inbox) {
            	// if there is none internal message in the inbox you cannot compare the counters
            	if (!(inbox.stream().filter(msg -> (msg instanceof InternalMessage)).count() == 0)) {
            		// if it is an internal message then compare the counters with the last internal message in the inbox
            		if (message instanceof InternalMessage) {
                		// iterate from the last to the first element
            			Iterator<Message> descIter = inbox.descendingIterator();
                		Message lastIntMsg = null;
                		while (descIter.hasNext()) {
                			lastIntMsg = descIter.next();
                			if (lastIntMsg instanceof InternalMessage) {
                				break;
                			}
                		}
                		int maxCounter = Math.max(message.getCounter(), lastIntMsg.getCounter());
                		message.setCounter(maxCounter);
                	}
            	} 
            	
            	try {	
        			inbox.putLast(message);
        		} catch (InterruptedException e) {
        			System.out.println("Problem during inserting an external message into the inbox of the node" 
        					+ e.getMessage());
        			e.printStackTrace();
        		}   
			}	
    }

    /**
     * - polls continuously messages and depending on the message type it perform further operations:
     * 		- external message: attaches the lamport timestamp and gets broadcastet to other nodes
     * 		- internal message: stores it in the history of the node
     * - counter has to be synchronized because multiple nodes read and increment the static counter (race conditions)
     */
    public void run() {
        
    	while (!isInterrupted()) {
    		Message message = null;
			try {
				message = this.inbox.takeFirst();
			} catch (InterruptedException e) {
				// invokes log after interrupting the nodes
				log();
			}
            if (message != null) {
            	if (message instanceof InternalMessage) {
                    history.add((InternalMessage) message);
                } else {
                    // attach lamport timestamp (only counter) to the external message
                    synchronized (ExternalMessage.getIncrCounter()) {
                    	int counter = ExternalMessage.getIncrCounter().incrementAndGet();
                        message.setCounter(counter);
                        System.out.println("incrCounter: " + counter);
                        this.broadcast((ExternalMessage) message);
					}
                	

                }
            } else {
            	System.out.println("Message is null");
            }
    	}
    		
    }
    
    /**
     * - writes the whole history into a node-specific log file
     */
    public void log() {
    	
    	System.err.println("history size: " + history.size());
    	
    	File file = new File("lamportLogs/" + Thread.currentThread().getName());
    	
    	try (FileOutputStream fileOut = new FileOutputStream(file);
    		 ObjectOutputStream out = new ObjectOutputStream(fileOut);) {
    		
    		Iterator<InternalMessage> iter = history.iterator();
    		int msgCounter = 0;
    		while (iter.hasNext()) {
    			msgCounter++;
    			InternalMessage intMsg = iter.next();
    			out.writeBytes(msgCounter + ". message\n");
    			out.writeBytes(intMsg.toString());
    			out.writeBytes("\n\n");
    		}	
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("problem during writing log messages into file");
		}
    }
}
