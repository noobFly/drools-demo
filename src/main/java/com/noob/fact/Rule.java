package com.noob.fact;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rule {
	
	private String code;
	private OP op;
	private String[] limitVal ;
	private boolean or;
	private String desc;

}
