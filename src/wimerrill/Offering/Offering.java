package wimerrill.Offering;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Offering extends JavaPlugin {
	
	public static final Logger log = Logger.getLogger("Minecraft");
	public OfferingPlayerListener playerListener = new OfferingPlayerListener(this);
	public ArrayList<Altar> altars = new ArrayList();
	public ArrayList<Reward> rewards = new ArrayList();
	public String clickType;
	public Boolean UsePermissions;
	private static PermissionHandler Permissions;
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		
		console("Plugin enabled");
		
		setupPermissions();
		loadConfig();
		
	}
	
	public void onDisable() {
		
		console("Plugin disabled");
		
	}
	
	private void setupPermissions() {
	    Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
	    if (this.Permissions == null) {
	        if (test != null) {
	            UsePermissions = true;
	            this.Permissions = ((Permissions) test).getHandler();
	            this.console("Permissions detected");
	        } else {
	        	this.console("Permissions not detected; using default op-based system");
	        	UsePermissions = false;
	        }
	    }
	}
	
	public boolean can(Player player, String p) {
		if (UsePermissions) {
			return this.Permissions.has(player,"Offering.rewards." + p);
		}
		return player.isOp();
	}
	
	public void loadConfig() {
		File dir = new File("plugins/Offering");
		if (!dir.exists()) {
			dir.mkdir();
			console("Data folder 'Offering' created in '/plugins'");
		}
		File config = new File(dir,"config.yml");
		if (!config.exists()) {
			try {
				config.createNewFile();
				console("Default config file 'config.yml' created in '/plugins/Offering'");
				getConfiguration().setProperty("click-type","LEFT");
				getConfiguration().setProperty("altars.GOLD_BLOCK.gifts.SLIME_BALL.reward", "tp");
				getConfiguration().setProperty("altars.GOLD_BLOCK.gifts.CLAY_BALL.reward", "fullheal");
				getConfiguration().setProperty("altars.GOLD_BLOCK.gifts.DIAMOND_SWORD.reward", "zeus");
				getConfiguration().setProperty("rewards.tp.tp", "$x $y $z");
				getConfiguration().setProperty("rewards.zeus.lightning", "$x $y $z");
				getConfiguration().setProperty("rewards.fullheal.heal", 20);
				getConfiguration().save();
			}
			catch (IOException e) { }
		}
		clickType = getConfiguration().getString("click-type","LEFT");
		for (String rewardstring : getConfiguration().getKeys("rewards")) {
			String reward = rewardstring;
			HashMap<String,Parameter> pass = new HashMap();
			for (String key : getConfiguration().getKeys("rewards." + rewardstring)) {
				String val = getConfiguration().getString("rewards." + rewardstring + "." + key);
				String[] args;
				pass.put(key, new Parameter(val));
	
			}
			rewards.add(new Reward(rewardstring,pass));
		}
		for (String altarstring : getConfiguration().getKeys("altars")) {
			ArrayList<Gift> gifts = new ArrayList();
			for (String giftstring : getConfiguration().getKeys("altars." + altarstring + ".gifts")) {
				Material m2 = Material.getMaterial(giftstring);
				String reward = getConfiguration().getString("altars." + altarstring + ".gifts." + giftstring + ".reward");
				gifts.add(new Gift(m2,reward,rewards));
			}
			Material m = Material.getMaterial(altarstring);
			altars.add(new Altar(m,gifts));
		}	
	}
	
	public void console(String msg) {
		log.info("[Offering] " + msg);
	}
	
}
