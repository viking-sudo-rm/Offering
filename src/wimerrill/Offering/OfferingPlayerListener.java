package wimerrill.Offering;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OfferingPlayerListener extends PlayerListener {
	Offering plugin;
	
	public OfferingPlayerListener(Offering instance) {
		plugin = instance;
	}
	
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		ItemStack item = event.getItem();
		
		if (action.toString().equals(plugin.clickType + "_CLICK_BLOCK")) {
			for (Altar altar : plugin.altars) {
				for (Gift gift : altar.gifts) {
					if (altar.material.equals(block.getType()) && gift.material.equals(item.getType())) {
						
						RewardAction rewardAction = new RewardAction();
						if (gift.getReward() != null) {
							if (plugin.can(player,gift.rewardname)) {
								player.sendMessage(ChatColor.GREEN + "You received the reward " + gift.rewardname);
								Set<String> args = gift.getReward().getKeys();
								for (String name : args) {
									Parameter params = gift.getReward().getAttr(name);
									rewardAction.process(name, player, params, block);
								}
								if (plugin.decay) {
									int id = gift.material.getId();
									player.getInventory().removeItem(new ItemStack(id, gift.amount));
								}
							}
							else {
								player.sendMessage(ChatColor.RED + "You don't have permission to receive this reward");
							}
						}
						else {
							player.sendMessage(ChatColor.RED + "Invalid reward " + gift.rewardname + " (alert server admin)");
						}
					}
				}
			}
		}
		
	}
	
}
