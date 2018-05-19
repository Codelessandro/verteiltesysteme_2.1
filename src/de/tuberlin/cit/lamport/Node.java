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

public class Node extends Thread {
	
    static int nextId = 0;
 
    LinkedBlockingDeque<Message> inbox = new LinkedBlockingDeque<>();
    List<InternalMessage> history = new ArrayList<>();
    int nodeId;
    Node[] nodes;

    public Node() {
        this.nodeId = nextId;
        this.inbox = new LinkedBlockingDeque<>();
        nextId++;
    }

    public void broadcast(ExternalMessage externalMessage) {
        InternalMessage intMsg = new InternalMessage(externalMessage.counter, nodeId, externalMessage.messageId);
        Arrays.stream(nodes).forEach(node -> node.insertMessage(intMsg));

    }

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
                		int maxCounter = Math.max(message.counter, lastIntMsg.counter);
                		message.counter = maxCounter;
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

    public void run() {
        
    	while (!isInterrupted()) {
    		Message message = null;
			try {
				message = this.inbox.take();
			} catch (InterruptedException e) {
				log();
				
			}
            if (message != null) {
            	if (message instanceof InternalMessage) {
                    history.add((InternalMessage) message);
                } else {
                    // attach lamport timestamp (only counter) to the external message
                    synchronized (ExternalMessage.incrCounter) {
                    	int counter = ExternalMessage.incrCounter.incrementAndGet();
                        message.counter = counter;
                        System.out.println("incrCounter: " + counter);
                        this.broadcast((ExternalMessage) message);
					}
                	

                }
            } else {
            	System.out.println("Message is null");
            }
    	}
    		
    }
    
    public void log() {
    	
    	if (!(history.size() == 1000)) {
    		System.err.println("the node did not store 1000 messages in the history");
    	}
    	
    	System.err.println(history.size());
    	
    	//MessageComparator comp = new MessageComparator();
    	//history.sort(comp);
    	
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
