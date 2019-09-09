package Helpers;

public class RequiredInputException extends InvalidInputException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequiredInputException(String key) {
		super(key);
	}

	public String getMessage() {
		return key + " is required";
	}
}
