package wimerrill.Offering;

import java.util.ArrayList;

import org.bukkit.Material;

public class Altar {
	private Material material;
	private ArrayList<Gift> gifts;
	
	public Altar(Material imaterial, ArrayList<Gift> igifts) {
		material = imaterial;
		gifts = igifts;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public ArrayList<Gift> getGifts() {
		return this.gifts;
	}
	
}
