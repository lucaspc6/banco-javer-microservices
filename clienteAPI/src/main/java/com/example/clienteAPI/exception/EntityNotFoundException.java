package com.example.clienteAPI.exception;

public class EntityNotFoundException extends RuntimeException{
	
	public EntityNotFoundException (String message) {
		super(message);
	}

}
