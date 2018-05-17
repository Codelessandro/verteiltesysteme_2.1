package de.tuberlin.cit.lamport;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class ExternalMessage extends Message {

	private static AtomicInteger incrCounter = new AtomicInteger(0);
	
	int randomInt;

    public ExternalMessage(int id) {
    	this.setMessageId(id);
        this.randomInt = ThreadLocalRandom.current().nextInt(0, 99 + 1);
    }

    public String toString() {
        return "External Message with ID: " + this.getMessageId() + " and random integer: " + randomInt;
    }
    
    public static AtomicInteger getIncrCounter() {
    	return ExternalMessage.incrCounter;
    }
    
    public int getRandomInt() {
    	return randomInt;
    }
}
