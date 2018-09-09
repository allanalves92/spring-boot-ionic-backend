package com.allanalves.cursomc.services.exceptions;

public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthorizationException(String mensagem) {
		super(mensagem);
	}

	public AuthorizationException(String mensagem, Throwable cause) {
		super(mensagem, cause);
	}
}
