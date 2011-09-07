package wimerrill.Offering;

public class Parameter {
	private String[] params;
	
	public Parameter(String input) {
		if (input.equals(null)) {
			params = null;
		}
		else {
			params = input.split(" ");
		}
	}
	
	public String[] getParams() {
		return params;
	}
	
}
