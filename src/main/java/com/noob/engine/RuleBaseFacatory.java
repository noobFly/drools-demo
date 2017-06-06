package com.noob.engine;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;

/**
 * 单实例RuleBase生成工具
 */
public class RuleBaseFacatory {

	private static class LazyHolder {
		private static final RuleBase INSTANCE = RuleBaseFactory.newRuleBase();
	}

	private RuleBaseFacatory() {
	}

	public static RuleBase getRuleBase() {
		return RuleBaseFacatory.LazyHolder.INSTANCE;
	}

}
