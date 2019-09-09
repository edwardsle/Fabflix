package Helpers;

public class InvalidInputException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String key;

	public InvalidInputException(String key) {
		super();
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getMessage() {
		return key + " is invalid";
	}
}
