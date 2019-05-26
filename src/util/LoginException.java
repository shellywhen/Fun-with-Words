package util;

@SuppressWarnings("serial")
public class LoginException extends Exception {
	public LoginException(String msg) {
		super();
		System.out.println(msg);
	}
}
