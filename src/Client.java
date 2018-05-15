import java.util.Random;

public class Client extends Thread {

    int nrMessages;
    Node[] nodeList;

    public Client(int nrMessages, Node[] nodeList) {
        this.nrMessages = nrMessages;
        this.nodeList = nodeList;
    }

    public void run() {
        for (int i = 0; i < nrMessages; i++) {
            ExternalMessage externalMessage = new ExternalMessage(i);
            this.sendMessage(externalMessage);
        }
        System.out.println("Client has sent all messages.");
    }

    public void sendMessage(ExternalMessage message) {
    	System.out.println("external msg: " + message);
    	Node randomNode = this.getRandomNode();
        randomNode.insertMessage(message);
        System.out.println("Client sent message to Node:" + randomNode.nodeId);

    }

    public Node getRandomNode() {
        int rnd = new Random().nextInt(this.nodeList.length);
        return this.nodeList[rnd];
    }


}
