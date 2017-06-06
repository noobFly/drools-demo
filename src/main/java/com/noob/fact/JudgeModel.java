package com.noob.fact;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class JudgeModel {

	private Map<String, String> map = new HashMap<String, String>();
	private boolean success;
	private String desc;

}
