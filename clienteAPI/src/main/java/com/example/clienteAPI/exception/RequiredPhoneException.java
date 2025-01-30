package com.example.clienteAPI.exception;


public class RequiredPhoneException extends RuntimeException{
	
	public RequiredPhoneException (String message) {
		super(message);
	}

}