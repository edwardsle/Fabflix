package Helpers;

public class InvalidNumberException extends InvalidInputException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String numberType;

	public InvalidNumberException(String key, String numberType) {
		super(key);
		this.numberType = numberType;
	}
	
	public String getMessage() {
		if (numberType == null || numberType.isEmpty())
			return key + " must be a number";
		else
			return key + " must be a " + numberType + " number";
			
	}
}
