package ro.Aneras.ClashWars.Games.Bedwars.GUI;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ro.Aneras.ClashWars.Games.Bedwars.BedWars;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Games.Bedwars.Cache.PlayerData;

public abstract class GUI {

	protected int menu;
	protected Player p;
	protected BedWars bw;
	protected boolean team;
	protected Inventory inv;
	protected PlayerData data;
	
	public GUI(BedWars bw, PlayerData data, Player p) {
		this.p = p;
		this.bw = bw;
		this.data = data;
	}
	
	public Inventory getMenu() {
		return inv;
	}
	
	public boolean isSwitching() {
		return false;
	}
	
	public abstract void openGUI(int x);

	public abstract void onClick(int slot);
	
	public void ownMessage() {
		p.sendMessage(Messages.BEDWARS_ALREADY_OWN.toString());
		p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
		p.closeInventory();
	}
	
	public boolean canPurchase(Material m, int amount, String name) {
		name = name.substring(2, name.length());
		int missing = getMissing(m, amount);
		if (missing <= 0) {
			removeAmount(m, amount);
			String color = data.getIsland().getColor().getTeamColor();
			p.playSound(p.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);
			if (team) {
				for (Player p : data.getIsland().getTeam()) {
					p.sendMessage(Messages.BEDWARS_UPGRADE_PURCHASE.toString().replace("%player%", this.p.getName()).replace("%upgrade%", name));
				}
			} else {
				p.sendMessage(Messages.BEDWARS_ITEM_PURCHASE.toString().replace("%player%", color+p.getName()).replace("%item%", name));
			}
			openGUI(menu);
			return true;
		} else {
			p.closeInventory();
			p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
			p.sendMessage(Messages.BEDWARS_NOT_ENOUGH.toString()+Messages.BEDWARS_NOT_ENOUGH_MSG.toString().replace("%missing%", String.valueOf(missing)));
			return false;
		}
	}
	
	public void setPrice(Material resource, int amount, int slot, int tier, ItemStack is, boolean maxed) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		for (int i = 0; i < lore.size(); i++) {
			ListIterator<String> it = lore.listIterator();
			while (it.hasNext()) {
				String next = it.next();
				next = next.replace("%money%", ""+amount);
				if (tier > 0) {
					next = next.replace("%tier%", ""+tier);
				}
				it.set(next);
			}
		}
		if (maxed) {
			im.setDisplayName("§c" + im.getDisplayName());
			lore.add("");
			lore.add(Messages.BEDWARS_UNLOCKED.toString());
			im.setLore(lore);
			is.setItemMeta(im);
			inv.setItem(slot, is);
		} else {
			int missing = getMissing(resource, amount);
			if (missing <= 0) {
				im.setDisplayName("§a" + im.getDisplayName());
				lore.add("");
				lore.add(Messages.BEDWARS_PURCHASE.toString());
				im.setLore(lore);
			} else {
				im.setDisplayName("§c" + im.getDisplayName());
				lore.add("");
				lore.add(Messages.BEDWARS_NOT_ENOUGH.toString());
				im.setLore(lore);
			}
			is.setItemMeta(im);
			inv.setItem(slot, is);
		}
	}
	
	private int getMissing(Material type, int amount) {
		for (ItemStack i : p.getInventory().getContents()) {
			if (i != null && i.getType() == type) {
				amount -= i.getAmount();
			}
		}
		return amount;
	}
	
	private void removeAmount(Material type, int amount) {
		ItemStack[] contents = p.getInventory().getContents();
		for (int x = 0; x < contents.length; x++) {
			ItemStack item = contents[x];
			if (item != null && item.getType() == type) {
				if (item.getAmount() <= amount) {
					amount -= item.getAmount();
					contents[x] = null;
				} else {
					item.setAmount(item.getAmount() - amount);
					amount = 0;
				}
				if (amount <= 0) {
					break;
				}
			}
		}
		p.getInventory().setContents(contents);
	}
	
	
	
}
