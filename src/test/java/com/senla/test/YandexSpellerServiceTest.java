package com.senla.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.senla.test.beans.TaskLang;
import com.senla.test.service.impl.YandexSpellerService;

@ExtendWith(MockitoExtension.class)
public class YandexSpellerServiceTest {

	@Mock
	private RestTemplate restTemplate;
	@InjectMocks
	private YandexSpellerService service;

	@Test
	@SuppressWarnings("unchecked")
	void checkText_shouldReturnEmptyIfResponsNot2xx() {

		String input = "синий иний";

		ResponseEntity<List<List<Map<String, Object>>>> response = ResponseEntity.status(500).build();
		when(restTemplate.exchange(eq("https://speller.yandex.net/services/spellservice.json/checkTexts"),
				eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
				.thenReturn(response);
		Optional<String> out = service.checkText(input, TaskLang.RU);

		assertTrue(out.isEmpty());
	}

	@Test
	@SuppressWarnings("unchecked")
	void checkText_shouldReturnEmptyIfBodyResponsIsNULL() {
		String input = "синий иний";

		List<List<Map<String, Object>>> body = null;
		ResponseEntity<List<List<Map<String, Object>>>> response = ResponseEntity.ok(body);

		when(restTemplate.exchange(eq("https://speller.yandex.net/services/spellservice.json/checkTexts"),
				eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
				.thenReturn(response);

		Optional<String> out = service.checkText(input, TaskLang.RU);

		assertTrue(out.isEmpty());
	}

	@Test
	@SuppressWarnings("unchecked")
	void checkText_shouldReturnOriginalTextIfBodyResponsIsEmpty() {

		String input = "синий иний";

		List<List<Map<String, Object>>> body = Collections.emptyList();
		ResponseEntity<List<List<Map<String, Object>>>> response = ResponseEntity.ok(body);

		when(restTemplate.exchange(eq("https://speller.yandex.net/services/spellservice.json/checkTexts"),
				eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
				.thenReturn(response);

		Optional<String> out = service.checkText(input, TaskLang.RU);

		assertTrue(out.isPresent());
		assertEquals(input, out.get());
	}

	@Test
	@SuppressWarnings("unchecked")
	void checkText_shouldReturnEmptyIfRestTemplateThrowError() {

		String input = "синий иний";

		when(restTemplate.exchange(eq("https://speller.yandex.net/services/spellservice.json/checkTexts"),
				eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
				.thenThrow(new RuntimeException(""));

		Optional<String> out = service.checkText(input, TaskLang.RU);

		assertTrue(out.isEmpty());
	}

	@Test
	@SuppressWarnings("unchecked")
	void checkText_shouldSplitLongTextIntoTwoParts_andCallYandexTwice() {

		String part1 = "a".repeat(9999);
		String part2 = "bbb";
		String input = part1 + " " + part2;

		ResponseEntity<List<List<Map<String, Object>>>> okNoErrors = ResponseEntity.ok(Collections.emptyList());

		when(restTemplate.exchange(eq("https://speller.yandex.net/services/spellservice.json/checkTexts"),
				eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
				.thenReturn(okNoErrors);

		Optional<String> out = service.checkText(input, TaskLang.RU);

		assertTrue(out.isPresent());
		assertEquals(input, out.get());

		verify(restTemplate, times(2)).exchange(eq("https://speller.yandex.net/services/spellservice.json/checkTexts"),
				eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class));
	}
}
