import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageSequencer extends Thread {
	
    LinkedBlockingQueue<InternalMessage> inbox = new LinkedBlockingQueue<>();
    Node[] nodeList;


    public void insertMessage(InternalMessage internalMessage) {
        synchronized (this) {
            try {
                this.inbox.put(internalMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void broadCast(InternalMessage internalMessage) {
        Arrays.stream(nodeList).forEach( e -> {
        	e.insertMessage(internalMessage);
        	// System.out.println("Message sequencer broadcastet message to node: " + e.nodeId);
        } );
        
    }

    public void run() {
        while (!isInterrupted()) {
        	
        	if (inbox.peek() != null) {
        		InternalMessage headMessage = this.inbox.poll();
                this.broadCast(headMessage);
        	}
        	
        }
    }
}
