package com.demo;

import org.drools.WorkingMemory;

import com.noob.engine.IWorkingEnvironmentCallback;

public class TestsRulesEngine {

	private RulesEngine rulesEngine;

	private TestDAO testDAO;

	public TestsRulesEngine(TestDAO testDAO) throws RulesEngineException {
		super();
		rulesEngine = new RulesEngine("rule1.drl");
		this.testDAO = testDAO;
	}

	public void assignTests(final Machine machine) {
		rulesEngine.executeRules(new IWorkingEnvironmentCallback() {
			public void initEnvironment(WorkingMemory workingMemory) {
				// Set globals first before asserting/inserting any knowledge!
				workingMemory.setGlobal("testDAO", testDAO);
				workingMemory.insert(machine);
			};
		});
	}

}
