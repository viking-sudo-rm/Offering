package wimerrill.Offering;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
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
	private final OfferingPlayerListener playerListener = new OfferingPlayerListener(this);
	public final ArrayList<Altar> altars = new ArrayList<Altar>();
	private final ArrayList<Reward> rewards = new ArrayList<Reward>();
	public String clickType;
	public boolean decay;
	//private boolean UsePermissions;
	private static PermissionHandler Permissions;
	
	public void onEnable() {
		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		
		console("Plugin enabled");
		
		setupPermissions();
		loadConfig();
	}
	
	public void onDisable() {
		console("Plugin disabled");
	}
	
	private void setupPermissions() {
	    if (Permissions == null) {
	    	final Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
	        if (test != null) {
	            Permissions = ((Permissions) test).getHandler();
	            this.console("Permissions detected");
	        } else {
	        	this.console("Permissions not detected; allowing anyone to access any altar");
	        }
	    }
	}
	
	public boolean can(Player player, String p) {
		if (Permissions != null) {
			return Permissions.has(player,"Offering.rewards." + p);
		}
		return true;
	}
	
	public void loadConfig() {
		final File dir = new File("plugins/Offering");
		if (!dir.exists()) {
			dir.mkdir();
			console("Data folder 'Offering' created in '/plugins'");
		}
		final File config = new File(dir,"config.yml");
		if (!config.exists()) {
			try {
				config.createNewFile();
				console("Default config file 'config.yml' created in '/plugins/Offering'");
				getConfiguration().setProperty("click-type","LEFT");
				getConfiguration().setProperty("decay",true);
				getConfiguration().setProperty("altars.GOLD_BLOCK.gifts.SLIME_BALL.reward", "tp");
				getConfiguration().setProperty("altars.GOLD_BLOCK.gifts.CLAY_BALL.reward", "fullheal");
				getConfiguration().setProperty("altars.GOLD_BLOCK.gifts.DIAMOND_SWORD.reward", "zeus");
				getConfiguration().setProperty("altars.GOLD_BLOCK.gifts.CAKE.reward", "cookies");
				getConfiguration().setProperty("altars.GOLD_BLOCK.gifts.WATCH.reward", "day");
				getConfiguration().setProperty("altars.GOLD_BLOCK.gifts.WATCH.amount", 3);
				getConfiguration().setProperty("rewards.tp.tp", "$x $y $z");
				getConfiguration().setProperty("rewards.zeus.lightning", "$x $y $z");
				getConfiguration().setProperty("rewards.fullheal.heal", 20);
				getConfiguration().setProperty("rewards.cookies.give", "COOKIE 5");
				getConfiguration().setProperty("rewards.day.time", 0);
				getConfiguration().save();
			}
			catch (IOException e) { }
		}
		clickType = getConfiguration().getString("click-type","LEFT");
		decay = getConfiguration().getBoolean("decay", true);
		for (final String rewardstring : getConfiguration().getKeys("rewards")) {
			ConcurrentHashMap<String,Parameter> pass = new ConcurrentHashMap<String, Parameter>();
			for (final String key : getConfiguration().getKeys("rewards." + rewardstring)) {
				String val = getConfiguration().getString("rewards." + rewardstring + "." + key);
				pass.put(key, new Parameter(val));
			}
			rewards.add(new Reward(rewardstring,pass));
		}
		for (String altarstring : getConfiguration().getKeys("altars")) {
			ArrayList<Gift> gifts = new ArrayList<Gift>();
			for (String giftstring : getConfiguration().getKeys("altars." + altarstring + ".gifts")) {
				Material m2 = Material.getMaterial(giftstring);
				String reward = getConfiguration().getString("altars." + altarstring + ".gifts." + giftstring + ".reward");
				int amount = getConfiguration().getInt("altars." + altarstring + ".gifts." + giftstring + ".amount",1);
				gifts.add(new Gift(m2,reward,rewards,amount));
			}
			Material m = Material.getMaterial(altarstring);
			altars.add(new Altar(m,gifts));
		}	
	}
	
	public void console(String msg) {
		log.info("[Offering] " + msg);
	}
	
}
