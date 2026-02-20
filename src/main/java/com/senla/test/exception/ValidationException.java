package com.senla.test.exception;

import java.util.List;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final List<String> errors;

	public ValidationException(List<String> errors) {
		super(String.join("; ", errors));
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}

}
