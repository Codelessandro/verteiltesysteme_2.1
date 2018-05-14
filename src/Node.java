import java.util.concurrent.LinkedBlockingDeque;

public class Node extends Thread {
    static int nodeCounter = 0;
    LinkedBlockingDeque<Message> inbox = new LinkedBlockingDeque();
    MessageSequencer messageSequencer;
    int nodeId;

    public Node(MessageSequencer messageSequencer) {
        this.nodeId = nodeCounter;
        this.messageSequencer = messageSequencer;
        this.inbox = new LinkedBlockingDeque();
        nodeCounter++;

    }

    public void forwardMessage(ExternalMessage externalMessage) {
        InternalMessage internalMessage = new InternalMessage(externalMessage);
        this.messageSequencer.insertMessage(internalMessage);

    }

    public void insertMessage(Message message) {
        try {
            inbox.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void run() {
        while (!isInterrupted()) {
                Message message = this.inbox.poll();
                if (message != null) {
                    this.forwardMessage((ExternalMessage) message);
                }

        }

        System.out.println("Node #" + this.nodeId + " saves his history.");
    }
}
