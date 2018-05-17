package de.tuberlin.cit.lamport;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Node extends Thread {
	
    static int nodeCounter = 0;
 
    LinkedBlockingQueue<Message> inbox = new LinkedBlockingQueue<>();
    List<InternalMessage> history = new ArrayList<>();
    int nodeId;
    Node[] nodes;

    public Node() {
        this.nodeId = nodeCounter;
        this.inbox = new LinkedBlockingQueue<>();
        nodeCounter++;
    }

    public void broadcast(ExternalMessage externalMessage) {
        InternalMessage intMsg = new InternalMessage(externalMessage.counter, nodeId);
        Arrays.stream(nodes).forEach(node -> node.insertMessage(intMsg));

    }

    public void insertMessage(Message message) {
        // only internal messages sent by the message sequencer are stored in the history
            try {	
    			inbox.put(message);
    		} catch (InterruptedException e) {
    			System.out.println("Problem during inserting an external message into the inbox of the node" 
    					+ e.getMessage());
    			e.printStackTrace();
    		}    	
    }

    public void run() {
        
    	while (!isInterrupted()) {
    		Message message = this.inbox.poll();
            if (message != null) {
            	if (message instanceof InternalMessage) {
                    history.add((InternalMessage) message);
                } else {
                    // only stores external messages sent by the client in the inbox
                    // attach lamport timestamp to the external message
                    ++ExternalMessage.incrCounter;
                    message.counter = ExternalMessage.incrCounter;
                    System.out.println("incrCounter: " + ExternalMessage.incrCounter);
                    this.broadcast((ExternalMessage) message);

                }
            }
    	}
        log();
    }
    
    public void log() {
    	
    	if (!(history.size() == 1000)) {
    		System.err.println("the node did not store 1000 messages in the history");
    	}
    	
    	MessageComparator comp = new MessageComparator();
    	history.sort(comp);
    	
    	File file = new File("logs/" + Thread.currentThread().getName());
    	
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
