package wimerrill.Offering;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Reward {
	private ConcurrentHashMap<String, Parameter> args = new ConcurrentHashMap<String, Parameter>();
	private String name;
	
	public Reward(String iname, ConcurrentHashMap<String, Parameter> pass) {
		args = pass;
		name = iname;
	}
	
	public Parameter getAttribute(String key) {
		return args.get(key);
	}
	
	public Set<String> getKeys() {
		return args.keySet();
	}
	
	public String getName(){
		return this.name;
	}
}
