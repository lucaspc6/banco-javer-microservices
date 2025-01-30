package com.example.clienteAPI.exception;

public class NegativeSaldoException extends RuntimeException{
	
	public NegativeSaldoException (String message) {
		super(message);
	}

}
