package com.senla.test.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.senla.test.beans.TaskLang;
import com.senla.test.service.SpellerService;
import com.senla.test.service.utils.ErrorCorrector;
import com.senla.test.service.utils.OptionsBuilderForYandexSpeller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class YandexSpellerService implements SpellerService {

	private final RestTemplate restTemplate;
	private static final String URL = "https://speller.yandex.net/services/spellservice.json/checkTexts";
	private static final int MAX_CHARS = 10000;

	@Override
	public Optional<String> checkText(String text, TaskLang lang) {
		try {
			OptionsBuilderForYandexSpeller optionsBuilder = new OptionsBuilderForYandexSpeller(text);
			int options = optionsBuilder.build();
			List<String> parts = splitTextByMaxLength(text, MAX_CHARS);
			StringBuilder corrected = new StringBuilder();
			log.debug("Yandex speller start. lang={}, options={}, textLen={}, parts={}", lang, options, text.length(),
					parts.size());
			for (String part : parts) {
				Optional<String> result = checkTextPart(part, lang, options);
				if(result.isEmpty()) {
					return Optional.empty();
				}
				corrected.append(result.get());
				corrected.append(" ");
			}
			log.debug("Yandex speller done. lang={}, options={}, parts={}", lang, options, parts.size());
			return Optional.of(corrected.toString().trim());

		} catch (Exception e) {
			log.error("Yandex speller failed (outer). lang={}", lang, e);
			return Optional.empty();
		}
	}

	private List<String> splitTextByMaxLength(String text, int maxLength) {
		List<String> chunks = new ArrayList<>();
		int start = 0;

		while (start < text.length()) {
			int end = Math.min(start + maxLength, text.length());

			if (end < text.length()) {
				int lastSpace = text.lastIndexOf(' ', end);
				if (lastSpace > start) {
					end = lastSpace;
				}
				if (end == start) {
					end = Math.min(start + maxLength, text.length());
				}
			}
			chunks.add(text.substring(start, end).trim());
			start = end;
		}
		return chunks;
	}

	private Optional<String> checkTextPart(String textPart, TaskLang lang, int options) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
			form.add("text", textPart);
			form.add("lang", lang.getCode());
			
			if (options > 0) {
				form.add("options", String.valueOf(options));
			}
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

			ResponseEntity<List<List<Map<String, Object>>>> response = restTemplate.exchange(URL, HttpMethod.POST,
					entity, new ParameterizedTypeReference<>() {
					});

			if (!response.getStatusCode().is2xxSuccessful()) {
				log.warn("Yandex speller non-2xx response. status={}, lang={}, options={}, partLen={}",
						response.getStatusCode(), lang, options, textPart.length());
				return Optional.empty();
			}

			if (response.getBody() == null) {
				log.warn("Yandex speller 2xx but body is null. status={}, lang={}, options={}, partLen={}",
						response.getStatusCode(), lang, options, textPart.length());
				return Optional.empty();
			}

			List<List<Map<String, Object>>> body = response.getBody();
			if (body.isEmpty() || body.get(0) == null || body.get(0).isEmpty()) {
				return Optional.of(textPart);
			}

			List<Map<String, Object>> errors = body.get(0);

			errors.sort((e1, e2) -> {
				int pos1 = ((Number) e1.get("pos")).intValue();
				int pos2 = ((Number) e2.get("pos")).intValue();
				return Integer.compare(pos2, pos1);
			});
			ErrorCorrector corrector = new ErrorCorrector(textPart, errors);
			String result = corrector.run();
			return Optional.of(result);

		} catch (Exception e) {
			log.error("Yandex speller call failed. lang={}, options={}, partLen={}", lang, options, textPart.length(),
					e);
			return Optional.empty();
		}
	}
}
