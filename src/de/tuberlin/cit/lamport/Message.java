package de.tuberlin.cit.lamport;

public abstract class Message  {
	
	private int messageId;
	private int counter;
	
	public int getMessageId() {
		return messageId;
	}
	
	public int getCounter() {
		return counter;
	}
	
	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public abstract String toString();
}
