public class InternalMessage extends Message {
    	
	static int nextId = 0;
	int nodeId;
    ExternalMessage externalMessage;

    public InternalMessage(ExternalMessage externalMessage, int nodeId) {
        this.externalMessage = externalMessage;
        this.nodeId = nodeId;
        messageId = nextId;
        nextId++;
    }
    
    public String toString() {
    	return "Internal Message with message Id: " + messageId + " and was sent by Thread: " + nodeId 
    	   + " to the message sequencer, " + externalMessage.toString();
    }



}
