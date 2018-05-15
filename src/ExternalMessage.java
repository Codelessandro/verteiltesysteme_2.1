import java.util.concurrent.ThreadLocalRandom;

public class ExternalMessage extends Message {

	int randomInt;

    public ExternalMessage(int id) {
        messageId = id;
        this.randomInt = ThreadLocalRandom.current().nextInt(0, 99 + 1);
    }

    public String toString() {
        return " and includes the external Message with ID: " + this.messageId + " and random integer: " + randomInt;
    }
}
