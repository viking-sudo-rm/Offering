package wimerrill.Offering;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

public class OfferingPlayerListener extends PlayerListener {
	private Offering plugin;
	
	public OfferingPlayerListener(Offering instance) {
		plugin = instance;
	}
	
	public void onPlayerInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final Action action = event.getAction();
		final Block block = event.getClickedBlock();
		final ItemStack item = event.getItem();
		boolean check = false;
		if (plugin.clickType.equalsIgnoreCase("left"))
			check |= action.equals(Action.LEFT_CLICK_BLOCK);
		if (plugin.clickType.equalsIgnoreCase("right"))
			check |= action.equals(Action.RIGHT_CLICK_BLOCK);
		
		if (check) {
			for (Altar altar : plugin.altars) {
				if (altar.getMaterial().equals(block.getType())) {
					for (Gift gift : altar.getGifts()) {
						if (gift.material.equals(item.getType())) {
							final RewardAction rewardAction = new RewardAction();
							if (gift.getReward() != null) {
								if (plugin.can(player,gift.rewardname)) {
									player.sendMessage(ChatColor.GREEN + "You received the reward " + gift.rewardname);
									Set<String> args = gift.getReward().getKeys();
									for (String name : args) {
										Parameter params = gift.getReward().getAttribute(name);
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
	
}
