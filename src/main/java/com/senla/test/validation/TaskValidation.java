package com.senla.test.validation;

import java.util.ArrayList;
import java.util.List;

import com.senla.test.beans.TaskLang;
import com.senla.test.dto.TaskRequest;

public class TaskValidation {
	
	private static final int MINIMUM_TEXT_SIZE = 3;
	
	public static List<String> validation (TaskRequest task) {
		List<String> errors = new ArrayList<>();
		
		if (!langValidation(task.getLang())){
			errors.add("UNSUPPORTED MESSAGE LANGUAGE");
		}
		if(task.getText()==null) {
			errors.add("MESSAGE IS TOO SHORT");
			return errors;
		}
		
		if (!sizeTextValidation(task.getText())){
			errors.add("MESSAGE IS TOO SHORT");
		}
		if (onlySymbolsOrDigits(task.getText())) {
			errors.add("TEXT CONTAINS ONLY SYMBOLS AND DIGITS");
		}
		return errors;
	}
	
	private static boolean langValidation (String lang) {
		
		return TaskLang.isValid(lang);
	}
	
	private static boolean sizeTextValidation (String text) {
		
		if (text.length() >= MINIMUM_TEXT_SIZE) {
			return true;
		}
		return false;
	}
	
	private static boolean onlySymbolsOrDigits(String text) {
		
		for(Character ch:text.toCharArray()) {
			if(Character.isLetter(ch)) {
				return false;
			}
		}
		return true;
	}
}
