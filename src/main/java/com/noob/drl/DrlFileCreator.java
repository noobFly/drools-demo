package com.noob.drl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.noob.fact.OP;
import com.noob.fact.Rule;

/**
 * 
 * 新增或编辑规则文件
 */

public class DrlFileCreator {

	public final static String DIRECTORY = System.getProperty("user.dir") + File.separator + "drl";
	private final static String NEW_LINE = "\n";

	public void write(String content, String fileName) {
		File dir = new File(DIRECTORY);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String drlName = String.join(File.separator, DIRECTORY, fileName);
		FileLock tryLock = null;
		FileChannel channel = null;
		try {
			channel = new RandomAccessFile(drlName, "rw").getChannel();
			tryLock = channel.tryLock();
			if (tryLock != null) {
				ByteBuffer sendBuffer = ByteBuffer.wrap(content.getBytes());
				channel.write(sendBuffer);
			} else {
				System.out.println(String.format("%s has been locked!", drlName));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if (tryLock != null) {
				try {
					tryLock.release();
					tryLock = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (channel != null) {
				try {
					channel.close();
					channel = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public String buildDrlContent(List<Rule> list, String topic) {
		String content = null;
		if (notEmpty(list)) {
			List<Rule> filterList = list.stream().filter(t -> t != null).collect(Collectors.toList());
			StringBuilder builder = new StringBuilder();
			appendln(builder, "package drl; ", "import com.noob.fact.JudgeModel;");
			appendln(builder, String.format("rule \"ruleTest: %s\"", topic));
			append(builder, "when $judgeResult: JudgeModel( ");

			int size = filterList.size();
			for (int i = 0; i < size; i++) {
				Rule rule = filterList.get(i);
				String lower = rule.getLimitVal()[0];
				if (rule.getOp().equals(OP.RANGE)) {
					String upper = rule.getLimitVal()[1];
					append(builder, " ( map.get(\"", rule.getCode(), "\")");

					append(builder, " >= ", lower, " && " + " map.get(\"", rule.getCode(), "\")", " <= ", upper, " )");
				} else {
					append(builder, " map.get(\"", rule.getCode(), "\")");
					if (rule.getOp().equals(OP.GT)) {

						append(builder, " > ", lower);

					} else if (rule.getOp().equals(OP.EQ)) {

						append(builder, " == ", lower);

					} else if (rule.getOp().equals(OP.LT)) {

						append(builder, " < ", lower);

					}
				}

				if (i < size - 1) {
					if (rule.isOr())
						append(builder, " || ");
					else
						append(builder, " && ");

				}

			}
			appendln(builder, ")");
			appendln(builder, "then", String.format("System.out.print(\"%s\");", topic),
					"$judgeResult.setSuccess(true);", "end");
			content = builder.toString();
		}
		return content;

	}

	private <T> boolean notEmpty(T t) {
		boolean result = false;
		if (t != null) {
			if (t instanceof String) {
				result = ((String) t).trim().length() > 0;
			} else if (t instanceof Collection) {
				Collection c = (Collection) t;
				result = c.size() > 0 && c.stream().anyMatch(s -> s != null);

			}
		}
		return result;

	}

	private void appendln(StringBuilder str, String... content) {
		for (String a : content) {
			str.append(a).append(NEW_LINE);
		}

	};

	private void append(StringBuilder str, String... content) {
		for (String a : content) {
			str.append(a);
		}
	};

}
