package de.tuberlin.cit.lamport;

public abstract class Message  {
	
	public static int incrCounter;
	
	public int messageId;
	public int counter;

	public abstract String toString();
}
