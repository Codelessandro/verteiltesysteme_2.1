import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class ExternalMessage extends Message implements Serializable {
    
	private static final long serialVersionUID = -8460080396776188730L;
	
	int randomInt;
    int messageID;

    public ExternalMessage(int id) {
        this.messageID = id;
        this.randomInt = ThreadLocalRandom.current().nextInt(0, 99 + 1);
    }

    public String toString() {
        return "External Message with ID: " + this.messageID;
    }
}
