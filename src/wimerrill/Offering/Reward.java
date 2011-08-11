package wimerrill.Offering;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;

public class Reward {
	
	private HashMap<String, Parameter> args = new HashMap();
	public Boolean hasArgs = false;
	public String name;
	
	public Reward(String iname, HashMap<String, Parameter> pass) {
		args = pass;
		name = iname;
	}
	
	public Parameter getAttr(String key) {
		return args.get(key);
	}
	
	public Set<String> getKeys() {
		return args.keySet();
	}
	
}
