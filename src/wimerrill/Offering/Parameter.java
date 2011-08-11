package wimerrill.Offering;

public class Parameter {
	
	public Boolean hasParams;
	private String[] params;
	
	public Parameter(String input) {
		if (input.equals(null)) {
			hasParams = false;
		}
		else {
			hasParams = true;
			params = input.split(" ");
		}
	}
	
	public String[] getParams() {
		if (hasParams) {
			return params;
		}
		return null;
	}
	
}
