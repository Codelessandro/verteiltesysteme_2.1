import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;

public class MessageSequencer extends Thread {
    LinkedBlockingDeque<InternalMessage> inbox = new LinkedBlockingDeque()<>;
    Node[] nodeList;


    public void insertMessage(InternalMessage internalMessage) {
        synchronized (this ) { //vielleicht nicht performant?
            try {
                this.inbox.put(internalMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            InternalMessage headMessage = this.inbox.poll();
            this.broadCast(internalMessage);
        }

    }

    public void broadCast(InternalMessage internalMessage) {
        Arrays.stream(nodeList).forEach( e -> e.insertMessage(internalMessage) );
    }

    public void run() {
        //
    }
}
