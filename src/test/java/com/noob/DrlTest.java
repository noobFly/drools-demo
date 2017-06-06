package com.noob;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.noob.drl.DrlFileCreator;
import com.noob.engine.IRuleEngine;
import com.noob.engine.RuleEngineImpl;
import com.noob.fact.JudgeModel;
import com.noob.fact.OP;
import com.noob.fact.Rule;

import junit.framework.TestCase;

public class DrlTest extends TestCase {

	public void testCreateDrl() throws Exception {

		DrlFileCreator creator = new DrlFileCreator();
		Rule r1 = new Rule("code1", OP.GT, new String[] { "3" }, true, "code1 >  3");
		Rule r2 = new Rule("code2", OP.EQ, new String[] { "4" }, true, "code2 >  4");
		Rule r3 = new Rule("code3", OP.LT, new String[] { "5" }, true, "code3 >  5");
		Rule r4 = new Rule("code4", OP.RANGE, new String[] { "1", "10" }, true, "1 < code4 <=  10");
		List<Rule> list = Arrays.asList(r1, r2, r3, r4);

		creator.write(creator.buildDrlContent(list, "test rule1"), "test1.drl");
	}

	public void testExecute() throws Exception {
		try {
			IRuleEngine engine = new RuleEngineImpl();
			engine.initEngine();
			JudgeModel model = new JudgeModel();
			Map<String, String> map = new HashMap<>();
			map.put("code1", "1");
			map.put("code2", "2");
			map.put("code3", "3");
			map.put("code4", "4");
			model.setMap(map);
			engine.executeRuleEngine(model);
			System.out.println(model.toString());
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
