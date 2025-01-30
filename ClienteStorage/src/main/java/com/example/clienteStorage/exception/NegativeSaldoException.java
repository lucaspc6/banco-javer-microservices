package com.example.clienteStorage.exception;

public class NegativeSaldoException extends RuntimeException{
	
	public NegativeSaldoException (String message) {
		super(message);
	}

}
