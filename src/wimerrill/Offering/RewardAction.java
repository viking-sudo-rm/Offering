package wimerrill.Offering;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RewardAction {
	
	public RewardAction() { 
		
	}
	
	public void process(String name, Player player, Parameter p, Block block) {
		final ArrayList<String> parameter = new ArrayList<String>();
		for (final String p1 : p.getParams()) {
			if (p1.startsWith("$") && p1.length() > 1) {
				parameter.add(searchSigns(block,p1));
			}
			else {
				parameter.add(p1);
			}
		}
		if (name.equalsIgnoreCase("tp")) doTp(player,parameter);
		if (name.equalsIgnoreCase("lightning")) doLightning(player,parameter);
		if (name.equalsIgnoreCase("heal")) doHeal(player,parameter);
		if (name.equalsIgnoreCase("time")) doTime(player,parameter);
		if (name.equalsIgnoreCase("weather")) doWeather(player,parameter);
		if (name.equalsIgnoreCase("give")) doGive(player,parameter);
		/*
		 * TODO: add error catching
		 * TODO: add custom decays/no decays
		 * TODO: add more rewards
		 * TODO: add custom messages
		 * 
		 */
	}
	
	private String[] getSignText(Block block) {
		if (block.getState() instanceof Sign) {
			Sign sign = (Sign)block.getState();
			return sign.getLines();
		}
		return null;
	}
	
	public String searchSigns(Block block,String name) {
		final int bx = block.getX();
		final int bz = block.getZ();
		final int by = block.getY();
		final World world = block.getWorld();
		for (int x = bx - 1; x <= bx + 1; x++) {
			for (int z = bz - 1; z <= bz + 1; z++) {
				final String[] signText = getSignText(world.getBlockAt(x, by, z));
				if (signText != null && name.equals(signText[1])) {
					return signText[2];
				}
			}
		}
		return null;
	}
	
	private String safeGet(ArrayList<String> a, int i) {
		if (a.size() > i) {
			return a.get(i);
		}
		return "0";
	}
	
	@SuppressWarnings("unused")
	private void notYetSupported(Player player) {
		player.sendMessage(ChatColor.RED + "This reward is currently still in testing and not fully supported");
	}
	
	private void doTp(Player player, ArrayList<String> parameter) {
		final World world = player.getWorld();
		final int x = Integer.parseInt(safeGet(parameter,0));
		final int y = Integer.parseInt(safeGet(parameter,1));
		final int z = Integer.parseInt(safeGet(parameter,2));
		final Location location = new Location(world, x, y, z);
		player.sendMessage("Whoosh!");
		player.teleport(location);
	}
	
	private void doLightning(Player player, ArrayList<String> parameter) {
		player.sendMessage("Zap!");
		player.getWorld().strikeLightning(player.getLocation());
	}
	
	private void doHeal(Player player, ArrayList<String> parameter) {
		final int a = Integer.parseInt(safeGet(parameter,0));
		player.sendMessage("Feeling better?");
		player.setHealth(a);
	}
	
	private void doTime(Player player, ArrayList<String> parameter) {
		final int t = Integer.parseInt(safeGet(parameter,0));
		final World world = player.getWorld();
		player.sendMessage("The sun has obeyed!");
		world.setTime(t);
	}
	
	private void doGive(Player player, ArrayList<String> parameter) {
		final String materialName = safeGet(parameter, 0);
		final int count = Integer.parseInt(safeGet(parameter, 1));
		final Material material = Material.getMaterial(materialName);
		final ItemStack stack = new ItemStack(material, count);
		player.getInventory().addItem(stack);
		player.sendMessage("Your inventory gets heavier!");
	}
	
	private void doWeather(Player player, ArrayList<String> parameter) {
		final World world = player.getWorld();
		final String weatherType = safeGet(parameter,0);
		if (weatherType.equalsIgnoreCase("rain")) {
			world.setThundering(false);
			world.setStorm(true);
			player.sendMessage("Cold front approaching!");
		} else if (weatherType.equalsIgnoreCase("thunder")) {
			world.setStorm(true);
			world.setThundering(true);
			player.sendMessage("Electrical storm approaching!");
		} else if (weatherType.equalsIgnoreCase("clear")) {
			world.setThundering(false);
			world.setStorm(false);
			player.sendMessage("Warm front approaching!");
		} else {
			player.sendMessage("Weather can be rain, thunder, or clear");
		}
	}
	
}
