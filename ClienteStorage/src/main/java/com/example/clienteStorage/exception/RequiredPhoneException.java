package com.example.clienteStorage.exception;


public class RequiredPhoneException extends RuntimeException{
	
	public RequiredPhoneException (String message) {
		super(message);
	}

}