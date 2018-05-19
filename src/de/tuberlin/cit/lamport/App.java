package de.tuberlin.cit.lamport;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * 
 * @author Christian Mischok, Alessandro Schneider
 * - main method to start the app
 * - creates nodes (Threads) and one client who sends messages to the nodes
 * - number of nodes and external messages are specified by the command line arguments 
 */
public class App {
    
	/**
	 * - main method start Threads and interrupts them including the main Thread
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

    	// two arguments are required
		if (args.length < 2) {
    		System.err.println("usage of the app: <number_of_nodes> <number_of_messages>");
    		System.exit(1);
    	}
    	
    	int nrNodes = Integer.parseInt(args[0]);
        int nrMessages = Integer.parseInt(args[1]);
        
        System.out.println("app started with nrNodes=" + nrNodes +", nrMsg=" + nrMessages);

        // number of nodes created depending on the command line argument
        Node[] nodeList = IntStream.range(0, nrNodes).mapToObj(number -> new Node()).toArray(Node[]::new);
        // every node stores the reference to all remaining nodes for broadcasting
        Arrays.stream(nodeList).forEach(node -> node.setNodes(nodeList));
        
        Client client = new Client(nrMessages, nodeList);
        
        // root folder created for locating the log files
        File rootDir = new File("lamportLogs");
        rootDir.mkdir();
        

        // start all threads
        client.start();
        Arrays.stream(nodeList).forEach(e -> e.start());


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // interrupt all threads
        client.interrupt();
        Arrays.stream(nodeList).forEach(e -> e.interrupt());
        
        // depending on the number of messages it takes more time to create the log files on time
        try {
			Thread.sleep(15000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        // interrupting main Thread
        System.out.println("all threads are interrupted. main thread is finishing...");
        Thread.currentThread().interrupt();


    }
}
