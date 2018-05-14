import java.util.Arrays;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {

        int nrMessages = 100;
        int nrNodes = 20;


        MessageSequencer messageSequencer = new MessageSequencer();
        Node[] nodeList = IntStream.range(0, nrNodes).mapToObj(i -> new Node(messageSequencer)).toArray(Node[]::new);
        messageSequencer.nodeList = nodeList;

        System.out.println(nodeList[0].nodeId);


        Client client = new Client(nrMessages, nodeList);

        /* Starte alle threads */
        client.start();
        Arrays.stream(nodeList).forEach(e -> e.start());
        messageSequencer.start();


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Beende alle Threads
        messageSequencer.interrupt();
        client.interrupt();
        Arrays.stream(nodeList).forEach(e -> e.interrupt());


    }
}
