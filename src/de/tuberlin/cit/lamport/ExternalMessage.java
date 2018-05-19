package de.tuberlin.cit.lamport;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * - represents an External Message which is sent by the Client to a Node
 * 
 * @author Christian Mischok, Alessandro Schneider
 *
 */
public class ExternalMessage extends Message {

	// used for the lamport timestamp to store the incremented counter in the external message
	private static AtomicInteger incrCounter = new AtomicInteger(0);
	
	// payload with a random number
	private int randomInt;

	public ExternalMessage(int id) {
    	this.setMessageId(id);
        this.randomInt = ThreadLocalRandom.current().nextInt(0, 99 + 1);
    }
	
	public static AtomicInteger getIncrCounter() {
		return incrCounter;
	}

	public static void setIncrCounter(AtomicInteger incrCounter) {
		ExternalMessage.incrCounter = incrCounter;
	}

    public String toString() {
        return "External Message with ID: " + this.getMessageId() + " and random integer: " + randomInt;
    }
}
