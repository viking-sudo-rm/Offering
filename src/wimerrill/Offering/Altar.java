package wimerrill.Offering;

import java.util.ArrayList;

import org.bukkit.Material;

public class Altar {
	
	public Material material;
	public ArrayList<Gift> gifts;
	
	public Altar(Material imaterial, ArrayList<Gift> igifts) {
		material = imaterial;
		gifts = igifts;
	}
	
}
