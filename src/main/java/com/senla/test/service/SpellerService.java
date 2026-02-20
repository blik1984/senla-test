package com.senla.test.service;

import java.util.Optional;

import com.senla.test.beans.TaskLang;

public interface SpellerService {
	
	Optional<String> checkText (String text, TaskLang lang);

}
