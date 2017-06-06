package com.noob.engine;

import org.drools.event.ObjectInsertedEvent;
import org.drools.event.ObjectRetractedEvent;
import org.drools.event.ObjectUpdatedEvent;
import org.drools.event.WorkingMemoryEventListener;


public class DebugModelEventListener implements WorkingMemoryEventListener {

	public DebugModelEventListener() {
	}

	public void objectInserted(final ObjectInsertedEvent event) {
		outPrintln("objectInserted", event);
	}

	public void objectUpdated(final ObjectUpdatedEvent event) {
		outPrintln("objectUpdated", event);
	}

	public void objectRetracted(final ObjectRetractedEvent event) {
		outPrintln("objectRetracted", event);
	}

	<T> void outPrintln(String methodName, T t) {
		System.out.println(this.getClass().getName() + " " + methodName + "  " + t.toString());

	};

}
