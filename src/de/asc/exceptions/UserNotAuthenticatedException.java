package de.asc.exceptions;

public class UserNotAuthenticatedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserNotAuthenticatedException(String userid) {
		super("unauthenticated user: "+userid);
	}

	public UserNotAuthenticatedException() {
		// TODO Auto-generated constructor stub
	}
}
