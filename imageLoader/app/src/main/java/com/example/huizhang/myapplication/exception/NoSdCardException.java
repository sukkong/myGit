package com.example.huizhang.myapplication.exception;

public class NoSdCardException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoSdCardException() {
		super();
	}

	public NoSdCardException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NoSdCardException(String detailMessage) {
		super(detailMessage);
	}

	public NoSdCardException(Throwable throwable) {
		super(throwable);
	}

}
