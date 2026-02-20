package com.senla.test.service.utils;

import java.util.List;
import java.util.Map;

public class ErrorCorrector {

	private final String textPart;
	private final List<Map<String, Object>> errors;

	public ErrorCorrector(String textPart, List<Map<String, Object>> errors) {
		this.textPart = textPart;
		this.errors = errors;
	}

	public String run() {
		StringBuilder sb = new StringBuilder(textPart);

		for (Map<String, Object> error : errors) {
			Object posObj = error.get("pos");
			Object lenObj = error.get("len");

			@SuppressWarnings("unchecked")
			List<String> suggestions = (List<String>) error.get("s");

			if (posObj == null || lenObj == null || suggestions == null || suggestions.isEmpty()) {
				continue;
			}

			int pos = ((Number) posObj).intValue();
			int len = ((Number) lenObj).intValue();
			int end = pos + len;

			if (pos < 0 || end > sb.length() || len <= 0) {
				continue;
			}

			String replacement = suggestions.get(0);
			sb.replace(pos, end, replacement);
		}
		return sb.toString();
	}
}
