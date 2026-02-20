package com.senla.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.senla.test.service.utils.OptionsBuilderForYandexSpeller;

public class OptionBuilderForYandexSpellerTest {

	@Test
	void build_shouldReturnTwoIfTextContainsDigit() {
		String text = "сини5 иней";
		
		OptionsBuilderForYandexSpeller optionBuilder = new OptionsBuilderForYandexSpeller(text);
		
		int out = optionBuilder.build();
		
		assertEquals(2, out);
	}
	
	@Test
	void build_shouldReturnzeroIfTextDontContainsDigitOrURL() {
		String text = "синий иней";
		
		OptionsBuilderForYandexSpeller optionBuilder = new OptionsBuilderForYandexSpeller(text);
		
		int out = optionBuilder.build();
		
		assertEquals(0, out);
	}
	
	@Test
	void build_shouldReturnFourIfTextContainsURL() {
		String text = "http://синий иней";
		
		OptionsBuilderForYandexSpeller optionBuilder = new OptionsBuilderForYandexSpeller(text);
		
		int out = optionBuilder.build();
		
		assertEquals(4, out);
	}
	
	@Test
	void build_shouldReturnSixIfTextContainsDigitAndURL() {
		String text = "сини5 http://иней";
		
		OptionsBuilderForYandexSpeller optionBuilder = new OptionsBuilderForYandexSpeller(text);
		
		int out = optionBuilder.build();
		
		assertEquals(6, out);
	}
}
