package de.tuberlin.cit.lamport;

public class InternalMessage extends Message {
    	
	static int nextId = 0;
	
	int nodeId;

    public InternalMessage(int counter, int nodeId) {
        this.nodeId = nodeId;
        this.counter = counter;
        messageId = nextId;
        nextId++;
    }
    
    public String toString() {
    	return "Lamport timestamp (<counter>,<id>): (" + counter + "," + nodeId + ")";
    }

}
