package de.tuberlin.cit.lamport;

/**
 * - represents an abstract message class
 * - external and internal message inherit the <messageId> and <counter>
 * 
 * @author Christian Mischok, Alessandro Schneider
 *
 */
public abstract class Message  {
	
	private int messageId;
	private int counter;

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public abstract String toString();
}
