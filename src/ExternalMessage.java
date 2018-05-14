import java.util.concurrent.ThreadLocalRandom;

public class ExternalMessage extends Message {
    int randomInt;
    int messageID;

    public ExternalMessage(int i) {
        this.messageID = i;
        this.randomInt = ThreadLocalRandom.current().nextInt(0, 99 + 1);
    }

    public String toString() {
        return "External Message with ID: " + this.messageID;
    }
}
