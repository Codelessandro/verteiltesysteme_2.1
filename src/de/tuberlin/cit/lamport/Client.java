package de.tuberlin.cit.lamport;
import java.util.Random;

/**
 * 
 * @author Christian Mischok, Alessandro Schneider
 * - simulates multiple clients and number of messages is specified by the argument <nrMessages>
 * @see App#main(String[])
 *
 */
public class Client extends Thread {

    private int nrMessages;
    // stores a reference to all nodes
    private Node[] nodeList;

    public Client(int nrMessages, Node[] nodeList) {
        this.nrMessages = nrMessages;
        this.nodeList = nodeList;
    }

    /**
     * - creates ExternalMessage with ascending IDs
     */
    public void run() {
        for (int i = 0; i < nrMessages; i++) {
            ExternalMessage externalMessage = new ExternalMessage(i);
            this.sendMessage(externalMessage);
        }
        System.out.println("Client has sent all messages.");
    }
    
    /**
     * - send a message to a random node
     * - store the external message in the inbox of the node
     * @param message
     */
    public void sendMessage(ExternalMessage message) {
    	Node randomNode = this.getRandomNode();
        randomNode.insertMessage(message);
    }
    
    /**
     * 
     * @return node - a random node from the node list
     */
    public Node getRandomNode() {
        int rnd = new Random().nextInt(this.nodeList.length);
        return this.nodeList[rnd];
    }


}
