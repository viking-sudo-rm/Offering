package wimerrill.Offering;

import java.util.ArrayList;

import org.bukkit.Material;

public class Gift {

	public Material material;
	public String rewardname;
	private Reward reward;
	public Boolean rewardexists;
	public int amount;
	
	public Gift(Material imaterial, String ireward,ArrayList<Reward> possiblerewards, int iamount) {
		material = imaterial;
		rewardname = ireward;
		amount = iamount;
		rewardexists = false;
		for (Reward r : possiblerewards) {
			if (r.name.equals(rewardname)) {
				reward = r;
				rewardexists = true;
			}
		}
	}
	
	public Reward getReward() {
		if (rewardexists) {
			return reward;
		}
		return null;
	}
	
}
