package wimerrill.Offering;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class RewardAction {
	
	public RewardAction() { }
	
	public void process(String name,Player player,Parameter p, Block block) {
		ArrayList<String> parameter = new ArrayList();
		for (String p1 : p.getParams()) {
			if (p1.startsWith("$") && p1.length() > 1) {
				parameter.add(searchSigns(block,p1));
				player.sendMessage(searchSigns(block,p1));
			}
			else {
				parameter.add(p1);
			}
		}
		if (name.equals("tp")) { doTp(player,parameter); }
		if (name.equals("lightning")) { doLightning(player,parameter); }
		if (name.equals("heal")) { doHeal(player,parameter); }
		if (name.equals("time")) { doTime(player,parameter); }
		if (name.equals("weather")) { doWeather(player,parameter); }
		/*
		 * 
		 * TODO: add error catching
		 * TODO: add custom decays/no decays
		 * TODO: add integer for heal
		 * TODO: add more rewards
		 * TODO: test rewards
		 * 
		 */
	}
	
	public String getSignText(Block block,int index) {
		if (block.getState() instanceof Sign) {
			Sign sign = (Sign)block.getState();
			return sign.getLine(index);
		}
		return null;
	}
	
	public String searchSigns(Block block,String name) {
		int bx = block.getX();
		int bz = block.getZ();
		int by = block.getY();
		World world = block.getWorld();
		String key;
		String val;
		for (int x = bx - 1; x <= bx + 1; x++) {
			for (int z = bz - 1; z <= bz + 1; z++) {
				key = getSignText(world.getBlockAt(x,by,z),1);
				val = getSignText(world.getBlockAt(x,by,z),2);
				if (name.equals(key)) {
					return val;
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
	
	private void notYetSupported(Player player) {
		player.sendMessage(ChatColor.RED + "This reward is currently still in testing and not fully supported");
	}
	
	private void doTp(Player player, ArrayList<String> parameter) {
		Location location = player.getLocation().clone();
		int x = Integer.parseInt(safeGet(parameter,0));
		int y = Integer.parseInt(safeGet(parameter,1));
		int z = Integer.parseInt(safeGet(parameter,2));
		location.setX(x);
		location.setY(y);
		location.setZ(z);
		player.sendMessage("Whoosh!");
		player.teleport(location);
		
	}
	
	private void doLightning(Player player, ArrayList<String> parameter) {
		Location location = player.getLocation().clone();
		int x = Integer.parseInt(safeGet(parameter,0));
		int y = Integer.parseInt(safeGet(parameter,1));
		int z = Integer.parseInt(safeGet(parameter,2));
		location.setX(x);
		location.setY(y);
		location.setZ(z);
		World world = player.getWorld();
		player.sendMessage("Zap!");
		world.strikeLightning(location);
	}
	
	private void doHeal(Player player, ArrayList<String> parameter) {
		player.sendMessage("Feeling better?");
		int a = Integer.parseInt(safeGet(parameter,0));
		player.setHealth(a);
	}
	
	private void doTime(Player player, ArrayList<String> parameter) {
		notYetSupported(player);
	}
	
	private void doWeather(Player player, ArrayList<String> parameter) {
		String arg = safeGet(parameter,0);
		World world = player.getWorld();
		if (arg.equals("rain")) {
			world.setStorm(true);
			player.sendMessage("Cold front approaching!");
			
		}
		else {
			world.setStorm(false);
			player.sendMessage("Warm front approaching!");
		}
	}
	
}
