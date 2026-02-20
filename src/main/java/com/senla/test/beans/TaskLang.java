package com.senla.test.beans;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TaskLang {
	
	RU("ru"),
	EN("en"); //API яндекса принимает ещё украински с кодом "uk" 
	
	private final String code;
	
	TaskLang(String code){
		this.code = code;
	}
	
	public String getCode(){
		return code;
	}
	
	private static final Set<String> CODES  = Arrays.stream(TaskLang.values()).map(l->l.getCode().toLowerCase()).collect(Collectors.toSet());
	
	public static boolean isValid (String lang) {
		if (lang!=null && CODES.contains(lang.toLowerCase())) {
			return true;
		}
		return false;
	}
}
