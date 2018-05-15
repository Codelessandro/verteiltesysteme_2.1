import java.io.Serializable;

public class InternalMessage extends Message implements Serializable {
    
	private static final long serialVersionUID = 2329734168064135559L;
	
	static int nextId = 0;
	int nodeId;
    int counter;
    ExternalMessage externalMessage; //root message

    public InternalMessage(ExternalMessage externalMessage, int nodeId) {
        this.externalMessage = externalMessage;
        this.nodeId = nodeId;
        counter = nextId;
        nextId++;
    }



}
