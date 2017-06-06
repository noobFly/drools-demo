package com.noob.engine;

import org.drools.FactException;
import org.drools.WorkingMemory;

public interface IWorkingEnvironmentCallback {

	void initEnvironment(WorkingMemory workingMemory) throws FactException;
}
