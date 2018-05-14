public class InternalMessage extends Message {
    int threadId;
    int counter;
    ExternalMessage externalMessage; //root message

    public InternalMessage(ExternalMessage externalMessage) {
        this.externalMessage = externalMessage;
    }



}
