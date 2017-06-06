package com.noob.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.drools.RuleBase;
import org.drools.StatefulSession;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.event.DebugWorkingMemoryEventListener;
import org.drools.spi.Activation;

import com.noob.drl.DrlFileCreator;

/**
 * Drools 规则文件有一个或多个 rule 声明。每个 rule 声明由一个或多个 conditional 元素以及要执行的一个或多个
 * consequences 或 actions 组成。一个规则文件还可以有多个（即 0 个或多个）import 声明、多个 global 声明以及多个
 * function 声明。
 * 
 * @author xiongwenjun
 *
 */
public class RuleEngineImpl implements IRuleEngine {
	private RuleBase ruleBase = RuleBaseFacatory.getRuleBase();
	private boolean debug = false;

	public void initEngine() {
		try {
			PackageBuilder backageBuilder = getPackageBuilderFromDrlFile(DrlFileCreator.DIRECTORY);
			ruleBase.addPackages(backageBuilder.getPackages());
		} catch (DroolsParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取规则文件中
	 * 
	 * @return
	 * @throws Exception
	 */
	private PackageBuilder getPackageBuilderFromDrlFile(String directory) throws Exception {
		// 装载脚本文件
		List<Reader> readers = readRuleFromDrlFile(getDrlFile(directory));
		Properties properties = new Properties();
		properties.load(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("drools.properties"), "UTF-8"));
		PackageBuilder backageBuilder = new PackageBuilder(new PackageBuilderConfiguration(properties));
		for (Reader r : readers) {
			backageBuilder.addPackageFromDrl(r);
		}

		// 检查脚本是否有问题
		if (backageBuilder.hasErrors()) {
			throw new Exception(backageBuilder.getErrors().toString());
		}

		return backageBuilder;
	}

	/**
	 * @param drlFilePath
	 *            脚本文件路径
	 * @return
	 * @throws FileNotFoundException
	 */
	private List<Reader> readRuleFromDrlFile(File[] files) throws Exception {
		if (null == files || 0 == files.length) {
			return null;
		}

		List<Reader> readers = new ArrayList<Reader>();

		for (File file : files) {
			readers.add(new InputStreamReader(new FileInputStream(file)));
		}

		return readers;
	}

	/**
	 * 获取测试规则文件
	 * 
	 * @return
	 */
	private File[] getDrlFile(String directory) {
		File[] drlFiles = null;
		File dir = new File(directory);
		if (dir.exists() && dir.isDirectory()) {
			drlFiles = dir.listFiles((a, b) -> b.endsWith(".drl"));

		}

		return drlFiles;
	}

	public void refreshEnginRule() {
		org.drools.rule.Package[] packages = ruleBase.getPackages();
		for (org.drools.rule.Package pg : packages) {
			ruleBase.removePackage(pg.getName());
		}

		initEngine();
	}

	public <T> void executeRuleEngine(final T t) {
		executeRuleEngine(m -> {
			// 如果在对条件求值时，需要让规则引擎引用未 用作知识的对象，则应使用 WorkingMemory 类的 setGlobal() 方法
			// m.setGlobal("testDAO", null);
			m.insert(t);
		});

	}

	public <T> void executeRuleEngine(IWorkingEnvironmentCallback environment) {
		if (null == ruleBase.getPackages() || 0 == ruleBase.getPackages().length) {
			return;
		}
		StatefulSession workingMemory = ruleBase.newStatefulSession();
		if (debug) {
			workingMemory.addEventListener(new DebugWorkingMemoryEventListener());

		}
		environment.initEnvironment(workingMemory);
		workingMemory.fireAllRules(new org.drools.spi.AgendaFilter() {
			public boolean accept(Activation activation) {
				return !activation.getRule().getName().contains("_test");
			}
		});
		workingMemory.dispose();

	}

}
