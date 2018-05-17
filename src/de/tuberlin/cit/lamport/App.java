package de.tuberlin.cit.lamport;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

public class App {
    
	public static void main(String[] args) throws IOException {

    	if (args.length < 2) {
    		System.err.println("usage of the app: <number_of_nodes> <number_of_messages>");
    		System.exit(1);
    	}
    	
    	int nrNodes = Integer.parseInt(args[0]);
        int nrMessages = Integer.parseInt(args[1]);
        
        System.out.println("app started with nrNodes=" + nrNodes +", nrMsg=" + nrMessages);

        Node[] nodeList = IntStream.range(0, nrNodes).mapToObj(number -> new Node()).toArray(Node[]::new);
        Arrays.stream(nodeList).forEach(node -> node.nodes = nodeList);

        System.out.println("nodeId of first node: " + nodeList[0].nodeId);
        
        Client client = new Client(nrMessages, nodeList);
        
        File rootDir = new File("logs");
        rootDir.mkdir();
        

        /* start all threads */
        client.start();
        Arrays.stream(nodeList).forEach(e -> e.start());


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // interrupt all threads
        client.interrupt();
        Arrays.stream(nodeList).forEach(e -> e.interrupt());


    }
}
