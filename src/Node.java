import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Node extends Thread {
	
	static int nrIntMsg = 0;
    static int nodeCounter = 0;
    LinkedBlockingQueue<Message> inbox = new LinkedBlockingQueue<>();
    List<InternalMessage> history = new ArrayList<>();
    MessageSequencer messageSequencer;
    int nodeId;

    public Node(MessageSequencer messageSequencer) {
        this.nodeId = nodeCounter;
        this.messageSequencer = messageSequencer;
        this.inbox = new LinkedBlockingQueue<>();
        nodeCounter++;
    }

    public void forwardMessage(ExternalMessage externalMessage) {
        InternalMessage internalMessage = new InternalMessage(externalMessage, nodeId);
        this.messageSequencer.insertMessage(internalMessage);
        System.out.println("Node: " + nodeId + " sent internal message to message sequencer");

    }

    public void insertMessage(Message message) {
        // only internal messages sent by the message sequencer are stored in the history
    	if (message instanceof InternalMessage) {
    		nrIntMsg++;
        	history.add((InternalMessage) message);
        } else {
        	// only stores external messages sent by the client in the inbox
        	try {
    			inbox.put(message);
    		} catch (InterruptedException e) {
    			System.out.println("Problem during inserting an external message into the inbox of the node" 
    					+ e.getMessage());
    			e.printStackTrace();
    		}
        }
    }

    public void run() {
        while (!isInterrupted()) {
                Message message = this.inbox.poll();
                if (message != null) {
                    this.forwardMessage((ExternalMessage) message);
                }

        }
        log();
    }
    
    public void log() {
    	
    	if (!(history.size() == 1000)) {
    		System.out.println("the node did not store 1000 messages in the history");
    	} else if (!(inbox.size() == 1000)) {
    		System.out.println("the node did not store 1000 messages in the inbox");
    	}
    	
    	try {
    		FileOutputStream fileOut = new FileOutputStream("logs/" + nodeId);
    		ObjectOutputStream out = new ObjectOutputStream(fileOut);
    		Iterator<InternalMessage> iter = history.iterator();
    		if (iter.hasNext()) {
    			InternalMessage intMsg = iter.next();
    			out.writeObject(intMsg);
    		}
    		
			
		} catch (IOException e) {
			System.out.println("problem during writing the history in a log file");
			e.printStackTrace();
		}
    }
}
