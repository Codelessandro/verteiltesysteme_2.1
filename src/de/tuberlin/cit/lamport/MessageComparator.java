package de.tuberlin.cit.lamport;

import java.util.Comparator;

public class MessageComparator implements Comparator<InternalMessage> {

	@Override
	public int compare(InternalMessage msg1, InternalMessage msg2) {
		if (msg1.nodeId < msg2.nodeId || msg1.counter < msg2.counter) {
			return -1;
		} else {
			return 1;
		}
	}

}
