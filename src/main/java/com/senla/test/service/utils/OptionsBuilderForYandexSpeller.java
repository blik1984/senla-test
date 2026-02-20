package com.senla.test.service.utils;

import java.util.regex.Pattern;

public class OptionsBuilderForYandexSpeller {
	private final int OPTION_IGNORE_DIGITS = 2;
	private final int OPTION_IGNORE_URLS = 4;
	private final Pattern URL_PATTERN = Pattern.compile("(https?://\\S+)|(www\\.\\S+)", Pattern.CASE_INSENSITIVE);
	private final String text;
	
	public OptionsBuilderForYandexSpeller(String text) {
		this.text = text;
	}

	public int build() {
		int options = 0;
		if (isContainsDigit(text)) {
			options |= OPTION_IGNORE_DIGITS;
		}
		if (isContainsURL(text)) {
			options |= OPTION_IGNORE_URLS;
		}
		return options;
	}

	private boolean isContainsDigit(String text) {
		for (Character ch : text.toCharArray()) {
			if (Character.isDigit(ch)) {
				return true;
			}
		}
		return false;
	}

	private boolean isContainsURL(String text) {
		if (text == null || text.isBlank()) {
			return false;
		}
		return URL_PATTERN.matcher(text).find();
	}
}
