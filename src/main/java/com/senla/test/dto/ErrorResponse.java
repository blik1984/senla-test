package com.senla.test.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

	private String errorMessage;
	private int errorCode;
	private LocalDateTime timestamp;
	private String path;

	public ErrorResponse(String errorMessage, int errorCode, String path) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.timestamp = LocalDateTime.now();
		this.path = path;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getPath() {
		return path;
	}
}