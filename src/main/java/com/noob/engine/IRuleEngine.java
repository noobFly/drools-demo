package com.noob.engine;

/**
 * 规则引擎
 *
 */
public interface IRuleEngine {
	/** 
     * 初始化规则引擎 
     */  
    public void initEngine();

	/** 
     * 刷新规则引擎中的规则 
     */  
    public void refreshEnginRule();

	/** 
     * 执行规则引擎 
     * @param 积分Fact 
     */  
    public <T> void executeRuleEngine(final T fact);
}
