package com.senla.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.senla.test.service.utils.ErrorCorrector;

public class ErrorCorrectorTest {
	
	@Test
	void run_shouldReplaceErrorUsingFirstSuggestion() {

		String input = "синий иний";

		Map<String, Object> error = new HashMap<>();
		error.put("pos", 6);
		error.put("len", 4);
		error.put("row", 0);
		error.put("s", List.of("иней"));

		List<Map<String, Object>> errors = List.of(error);
		ErrorCorrector corrector = new ErrorCorrector(input, errors);
		
		String out = corrector.run();

		assertEquals("синий иней", out);
	}

	@Test
	void run_shouldReturnOriginalText_whenErrorsAreInvalid() {
		String input = "синий иний";

		Map<String, Object> error = new HashMap<>();
		error.put("pos", 6);
		error.put("len", 0);
		error.put("row", 0);
		error.put("s", null);
		List<Map<String, Object>> errors = List.of(error);
		ErrorCorrector corrector = new ErrorCorrector(input, errors);
		
		String out = corrector.run();

		assertEquals(input, out);
	}
}
