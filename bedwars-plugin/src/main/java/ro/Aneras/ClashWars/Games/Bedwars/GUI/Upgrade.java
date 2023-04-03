
package ro.Aneras.ClashWars.Games.Bedwars.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ro.Aneras.ClashWars.Games.Bedwars.BedWars;
import ro.Aneras.ClashWars.Games.Bedwars.BedWarsIsland;
import ro.Aneras.ClashWars.Games.Bedwars.Cache.PlayerData;
import ro.Aneras.ClashWars.Handler.Tools.ItemBuilder;
import ro.Aneras.ClashWars.Handler.Tools.Serializer;
import ro.Aneras.ClashWars.Messages;

public class Upgrade extends GUI {
	
	public Upgrade(BedWars bw, PlayerData data, Player p) {
		super(bw, data, p);
		team = true;
		inv = Bukkit.createInventory(null, 36, Messages.BEDWARS_UPGRADE_ITEM.toString());
		openGUI(0);
		p.openInventory(inv);
	}

	@Override
	public void onClick(int slot) {
		boolean team = bw.getMode() > 2;
		String name = inv.getItem(slot).getItemMeta().getDisplayName();
		if (slot == 11) {
			int level = data.getIsland().getForge();
			int price = 0;
			if (level == 0) {
				price = team ? 4 : 2;
			}
			if (level == 1) {
				price = team ? 8 : 4;
			}
			if (level == 2) {
				price = team ? 10 : 6;
			}
			if (level == 3) {
				super.ownMessage();
			} else if (super.canPurchase(Material.DIAMOND, price, name)) {
				data.getIsland().setForge(level+1);
				openGUI(0);
			}
		}
		if (slot == 12) {
			int level = data.getIsland().getHaste();
			if (level == 2) {
				super.ownMessage();
			} else if (super.canPurchase(Material.DIAMOND, (team ? 2 : 1) * (level == 0 ? 2 : 4), name)) {
				data.getIsland().setHaste(level+1);
				for (Player p : data.getIsland().getTeam()) {
					if (!bw.getSpectators().containsKey(p)) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, level), true);
					}
				}
				openGUI(0);
			}
		}
		if (slot == 13) {
			if (data.getIsland().isSharpened()) {
				super.ownMessage();
			} else if (super.canPurchase(Material.DIAMOND, team ? 8 : 4, name)) {
				data.getIsland().setSharpened(true);
				for (Player p : data.getIsland().getTeam()) {
					for (ItemStack is : p.getInventory().getContents()) {
						if (is != null && is.getType().name().contains("SWORD")) {
							is.addEnchantment(Enchantment.DAMAGE_ALL, 1);
						}
					}
				}
				openGUI(0);
			}
		}
		if (slot == 14) {
			int level = data.getIsland().getArmor()+1;
			int armor = Serializer.multiplyEach(team ? 5 : 2, level-1);
			armor = level >= 4 ? (team ? 30 : 16) : armor;
			if (level == 5) {
				super.ownMessage();
			} else if (super.canPurchase(Material.DIAMOND, armor, name)) {
				data.getIsland().setArmor(level);
				for (Player p : data.getIsland().getTeam()) {
					if (data.getRespawnItems() == null) {
						ItemStack[] is = p.getInventory().getArmorContents().clone();
						for (int x = 0; x < is.length; x++) {
							ItemStack item = is[x];
							if (item != null) {
								item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level);
								is[x] = item.clone();
							}
						}
						p.getInventory().setArmorContents(is);
					} else {
						ItemStack[] is = data.getRespawnItems();
						for (int x = 0; x < is.length; x++) {
							ItemStack item = is[x];
							if (item != null) {
								item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level);
								is[x] = item;
							}
						}
					}
				}
				openGUI(0);
			}
		}
		if (slot == 15) {
			if (data.getIsland().hasBlindTrap()) {
				super.ownMessage();
			} else if (super.canPurchase(Material.DIAMOND, 1, name)) {
				data.getIsland().setBlindTrap(true);
				openGUI(0);
			}
		}
		if (slot == 20) {
			if (data.getIsland().hasSlowTrap()) {
				super.ownMessage();
			} else if (super.canPurchase(Material.DIAMOND, team ? 3 : 2, name)) {
				data.getIsland().setSlowTrap(true);
				openGUI(0);
			}
		}
		if (slot == 21) {
			if (data.getIsland().isRegenerating()) {
				super.ownMessage();
			} else if (super.canPurchase(Material.DIAMOND, team ? 3 : 1, name)) {
				data.getIsland().setRegenerating(true);
				openGUI(0);
			}
		}
	}
	
	@Override
	public void openGUI(int x) {
		boolean team = bw.getMode() > 2;
		BedWarsIsland island = data.getIsland();
		int armor = Serializer.multiplyEach(team ? 5 : 2, island.getArmor());
		armor = island.getArmor() >= 3 ? (team ? 30 : 16) : armor;
		int haste = (team ? 2 : 1) * (island.getHaste() == 0 ? 2 : 4);
		if (island.getForge() == 0) {
			addItem(Material.DIAMOND, team ? 4 : 2, 11, ItemBuilder.create(Material.FURNACE, Messages.BEDWARS_UPGRADE_7_T1.toString(), Messages.BEDWARS_UPGRADE_7_LORE_T1.getList()));
		} else if (island.getForge() == 1) {
			addItem(Material.DIAMOND, team ? 8 : 4, 11, ItemBuilder.create(Material.FURNACE, Messages.BEDWARS_UPGRADE_7_T2.toString(), Messages.BEDWARS_UPGRADE_7_LORE_T2.getList()));
		} else if (island.getForge() == 2 || island.getForge() == 3) {
			addItem(Material.DIAMOND, team ? 10 : 6, 11, ItemBuilder.create(Material.FURNACE, Messages.BEDWARS_UPGRADE_7_T3.toString(), Messages.BEDWARS_UPGRADE_7_LORE_T3.getList()));
		}
		addItem(Material.DIAMOND, team ? 3 : 1, 21, ItemBuilder.create(Material.BEACON, Messages.BEDWARS_UPGRADE_1.toString(), Messages.BEDWARS_UPGRADE_1_LORE.getList()));
		addItem(Material.DIAMOND, team ? 3 : 2, 20, ItemBuilder.create(Material.IRON_PICKAXE, Messages.BEDWARS_UPGRADE_2.toString(), Messages.BEDWARS_UPGRADE_2_LORE.getList()));
		addItem(Material.DIAMOND, 1, 15, ItemBuilder.create(Material.TRIPWIRE_HOOK, Messages.BEDWARS_UPGRADE_3.toString(), Messages.BEDWARS_UPGRADE_3_LORE.getList()));
		addItem(Material.DIAMOND, team ? 8 : 4, 13, ItemBuilder.create(Material.IRON_SWORD, Messages.BEDWARS_UPGRADE_4.toString(), Messages.BEDWARS_UPGRADE_4_LORE.getList()));
		addItem(Material.DIAMOND, haste, 12, ItemBuilder.create(Material.GOLDEN_PICKAXE, Messages.BEDWARS_UPGRADE_5.toString(), Messages.BEDWARS_UPGRADE_5_LORE.getList()));
		addItem(Material.DIAMOND, armor, 14, ItemBuilder.create(Material.IRON_CHESTPLATE, Messages.BEDWARS_UPGRADE_6.toString(), Messages.BEDWARS_UPGRADE_6_LORE.getList()));
	}
	
	private void addItem(Material resource, int amount, int slot, ItemStack is) {
		int tier = 0;
		boolean maxed = false;
		if (slot == 21 && data.getIsland().isRegenerating()) {
			maxed = true;
		}
		if (slot == 20 && data.getIsland().hasSlowTrap()) {
			maxed = true;
		}
		if (slot == 15 && data.getIsland().hasBlindTrap()) {
			maxed = true;
		}
		if (slot == 14) {
			tier = data.getIsland().getArmor();
			if (tier == 4) {
				maxed = true;
			} else {
				tier++;
			}
		}
		if (slot == 13 && data.getIsland().isSharpened()) {
			maxed = true;
		}
		if (slot == 12) {
			tier = data.getIsland().getHaste();
			if (tier == 2) {
				maxed = true;
			} else {
				tier++;
			}
		}
		if (slot == 11 && data.getIsland().getForge() == 3) {
			maxed = true;
		}
		super.setPrice(resource, amount, slot, tier, is, maxed);
	}
	
}