import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Node extends Thread {

    int nrIntMsg = 0;
    int nrExtMsg = 0;
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

    }

    public void insertMessage(Message message) {
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
                	// only internal messages sent by the message sequencer are stored in the history
                    nrIntMsg++;
                    history.add((InternalMessage) message);
                } else {
                    // only stores external messages sent by the client in the inbox
                    nrExtMsg++;
                    this.forwardMessage((ExternalMessage) message);
                }
            }
        }
        
        System.out.println("int Msg=" + nrIntMsg);
        System.out.println("ext Msg=" + nrExtMsg);
        log();
    }

    public void log() {

        if (!(history.size() == 1000)) {
            System.err.println("the node did not store 1000 messages in the history");
        }

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
