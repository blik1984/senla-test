package com.senla.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.senla.test.dto.TaskRequest;
import com.senla.test.validation.TaskValidation;

public class TaskValidatorTest {

	@Test
	void validation_shouldReturnTooShort_whenTextIsNull() {

		TaskRequest task = new TaskRequest();
		task.setText(null);
		task.setLang("RU");

		List<String> out = TaskValidation.validation(task);

		assertTrue(out.contains("MESSAGE IS TOO SHORT"));
	}

	@Test
	void validation_shouldReturnTooShort_whenTextLengthLessThan3() {
		TaskRequest task = new TaskRequest();
		task.setText("ру");
		task.setLang("RU");

		List<String> out = TaskValidation.validation(task);

		assertTrue(out.contains("MESSAGE IS TOO SHORT"));
	}

	@Test
	void validation_shouldReturnUnsupportedLanguage_whenLangInvalid() {
		TaskRequest task = new TaskRequest();
		task.setText("синий");
		task.setLang("TU");

		List<String> out = TaskValidation.validation(task);

		assertTrue(out.contains("UNSUPPORTED MESSAGE LANGUAGE"));
	}

	@Test
	void validation_shouldReturnOnlySymbolsAndDigits_whenNoLetters() {
		TaskRequest task = new TaskRequest();
		task.setText("++456*    ");
		task.setLang("RU");

		List<String> out = TaskValidation.validation(task);

		assertTrue(out.contains("TEXT CONTAINS ONLY SYMBOLS AND DIGITS"));
	}

}
