package de.tuberlin.cit.lamport;

/**
 * - represents an internal message which is broadcastet from one node to another node
 * - the internal message stores exactly the same lamport timestamp which was attached to the external message
 * 
 * @author Christian Mischok
 *
 */
public class InternalMessage extends Message {
	
	private int nodeId;
	
	/**
	 * - attaches the lamport timestamp to the new created internal message
	 * @param counter
	 * @param nodeId
	 * @param messageId
	 */
    public InternalMessage(int counter, int nodeId, int messageId) {
        this.nodeId = nodeId;
        this.setCounter(counter);
        this.setMessageId(messageId);
    }
    
    /**
     * - output of this method is shown in the log files of each node
     */
    public String toString() {
    	return "Lamport timestamp (<counter>,<id>): (" + this.getCounter() + "," + nodeId + ")";
    }

}
