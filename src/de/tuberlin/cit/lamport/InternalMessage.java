package de.tuberlin.cit.lamport;

public class InternalMessage extends Message {
	
	int nodeId;

    public InternalMessage(int counter, int nodeId, int messageId) {
        this.nodeId = nodeId;
        this.counter = counter;
        this.messageId = messageId;
    }
    
    public String toString() {
    	return "Lamport timestamp (<counter>,<id>): (" + this.counter + "," + nodeId + ")";
    }

}
