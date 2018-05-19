package de.tuberlin.cit.lamport;

public abstract class Message  {
	
	int messageId;
	int counter;

	public abstract String toString();
}
