package de.tuberlin.cit.lamport;
import java.util.concurrent.ThreadLocalRandom;

public class ExternalMessage extends Message {

	int randomInt;

    public ExternalMessage(int id) {
        messageId = id;
        this.randomInt = ThreadLocalRandom.current().nextInt(0, 99 + 1);
    }

    public String toString() {
        return "External Message with ID: " + this.messageId + " and random integer: " + randomInt;
    }
}
