package de.tuberlin.cit.lamport;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class ExternalMessage extends Message {

	static AtomicInteger incrCounter = new AtomicInteger(0);
	
	int randomInt;

    public ExternalMessage(int id) {
    	this.messageId = id;
        this.randomInt = ThreadLocalRandom.current().nextInt(0, 99 + 1);
    }

    public String toString() {
        return "External Message with ID: " + this.messageId + " and random integer: " + randomInt;
    }
    
}
