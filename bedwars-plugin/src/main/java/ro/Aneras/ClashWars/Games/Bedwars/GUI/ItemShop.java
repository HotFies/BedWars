package ro.Aneras.ClashWars.Games.Bedwars.GUI;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ro.Aneras.ClashWars.Games.Bedwars.BedWars;
import ro.Aneras.ClashWars.Games.Bedwars.Cache.PlayerData;
import ro.Aneras.ClashWars.Handler.Tools.ItemBuilder;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;

public class ItemShop extends GUI {

	private boolean switching;
	
	public ItemShop(BedWars bw, PlayerData data, Player p) {
		super(bw, data, p);
		openGUI(0);
	}
	
	@Override
	public boolean isSwitching() {
		return switching;
	}

	@Override
	public void onClick(int slot) {
		String name = inv.getItem(slot).getItemMeta().getDisplayName();
		p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1f);
		if (menu == 0) {
			if (slot == 19 && super.canPurchase(Material.IRON_INGOT, 4, name)) {
				p.getInventory().addItem(new ItemStack(data.getIsland().getColor().getRealMaterial(), 16));
			}
			if (slot == 28 && super.canPurchase(Material.GOLD_INGOT, 4, name)) {
				p.getInventory().addItem(new ItemStack(Material.OAK_PLANKS, 16));
			}
			if (slot == 37 && super.canPurchase(Material.EMERALD, 4, name)) {
				p.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 4));
			}

			boolean sharp = data.getIsland().isSharpened();
			if (slot == 20 && super.canPurchase(Material.IRON_INGOT, 10, name)) {
				p.getInventory().remove(Material.WOODEN_SWORD);
				ItemStack is = ItemBuilder.create(Material.STONE_SWORD, null);
				if (sharp) {
					is.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				}
				p.getInventory().addItem(is);
			}
			if (slot == 29 && super.canPurchase(Material.GOLD_INGOT, 7, name)) {
				p.getInventory().remove(Material.WOODEN_SWORD);
				ItemStack is = ItemBuilder.create(Material.IRON_SWORD, null);
				if (sharp) {
					is.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				}
				p.getInventory().addItem(is);
			}
			if (slot == 38 && super.canPurchase(Material.EMERALD, 4, name)) {
				p.getInventory().remove(Material.WOODEN_SWORD);
				ItemStack is = ItemBuilder.create(Material.DIAMOND_SWORD, null);
				if (sharp) {
					is.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				}
				p.getInventory().addItem(is);
			}

			boolean bought = true;
			TeamColor color = data.getIsland().getColor();
			if (slot == 21) {
				if (data.getArmor() > 0) {
					p.sendMessage(Messages.BEDWARS_ALREADY_OWN.toString());
					p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
					p.closeInventory();
				} else if (super.canPurchase(Material.IRON_INGOT, 40, name)) {
					data.setArmor(1);
					p.getInventory().setBoots(ItemBuilder.create(Material.CHAINMAIL_BOOTS, color.getTeamColor() + color.getName()));
					p.getInventory().setLeggings(ItemBuilder.create(Material.CHAINMAIL_LEGGINGS, color.getTeamColor() + color.getName()));
				}
			} else if (slot == 30) {
				if (data.getArmor() > 1) {
					p.sendMessage(Messages.BEDWARS_ALREADY_OWN.toString());
					p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
					p.closeInventory();
				} else if (super.canPurchase(Material.GOLD_INGOT, 12, name)) {
					data.setArmor(2);
					p.getInventory().setBoots(ItemBuilder.create(Material.IRON_BOOTS, color.getTeamColor() + color.getName()));
					p.getInventory().setLeggings(ItemBuilder.create(Material.IRON_LEGGINGS, color.getTeamColor() + color.getName()));
				}
			} else if (slot == 39) {
				if (data.getArmor() > 2) {
					p.sendMessage(Messages.BEDWARS_ALREADY_OWN.toString());
					p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
					p.closeInventory();
				} else if (super.canPurchase(Material.EMERALD, 6, name)) {
					data.setArmor(3);
					p.getInventory().setBoots(ItemBuilder.create(Material.DIAMOND_BOOTS, color.getTeamColor() + color.getName()));
					p.getInventory().setLeggings(ItemBuilder.create(Material.DIAMOND_LEGGINGS, color.getTeamColor() + color.getName()));
				}
			} else {
				bought = false;
			}
			if (bought && data.getIsland().getArmor() > 0) {
				ItemStack[] is = p.getInventory().getArmorContents();
				for (int x = 0; x < is.length; x++) {
					ItemStack item = is[x];
					if (item != null) {
						item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, data.getIsland().getArmor());
					}
					is[x] = item;
				}
				p.getInventory().setArmorContents(is);
			}

			if (slot == 22) {
				if (data.getShearTier() == 1) {
					p.sendMessage(Messages.BEDWARS_ALREADY_OWN.toString());
					p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
					p.closeInventory();
					return;
				} else if (super.canPurchase(Material.IRON_INGOT, 20, name)) {
					data.setShearTier(1);
					p.getInventory().addItem(new ItemStack(Material.SHEARS));
					openGUI(0);
				}
			}
			if (slot == 31) {
				if (data.getPickAxeTier() == 4) {
					super.ownMessage();
					return;
				}
				if (data.getPickAxeTier() == 3 && super.canPurchase(Material.GOLD_INGOT, 6, name)) {
					data.setPickAxeTier(4);
					p.getInventory().remove(Material.IRON_PICKAXE);
					ItemStack is = new ItemStack(Material.DIAMOND_PICKAXE);
					is.addEnchantment(Enchantment.DIG_SPEED, 3);
					p.getInventory().addItem(is);
					openGUI(0);
				}
				if (data.getPickAxeTier() == 2 && super.canPurchase(Material.GOLD_INGOT, 3, name)) {
					data.setPickAxeTier(3);
					p.getInventory().remove(Material.STONE_PICKAXE);
					ItemStack is = new ItemStack(Material.IRON_PICKAXE);
					is.addEnchantment(Enchantment.DIG_SPEED, 2);
					p.getInventory().addItem(is);
					openGUI(0);
				}
				if (data.getPickAxeTier() == 1 && super.canPurchase(Material.IRON_INGOT, 10, name)) {
					data.setPickAxeTier(2);
					p.getInventory().remove(Material.WOODEN_PICKAXE);
					ItemStack is = new ItemStack(Material.STONE_PICKAXE);
					is.addEnchantment(Enchantment.DIG_SPEED, 1);
					p.getInventory().addItem(is);
					openGUI(0);
				}
				if (data.getPickAxeTier() == 0 && super.canPurchase(Material.IRON_INGOT, 10, name)) {
					data.setPickAxeTier(1);
					ItemStack is = new ItemStack(Material.WOODEN_PICKAXE);
					is.addEnchantment(Enchantment.DIG_SPEED, 1);
					p.getInventory().addItem(is);
					openGUI(0);
				}
			}
			if (slot == 40) {
				if (data.getAxeTier() == 4) {
					super.ownMessage();
					return;
				}
				if (data.getAxeTier() == 3 && super.canPurchase(Material.GOLD_INGOT, 6, name)) {
					data.setAxeTier(4);
					p.getInventory().remove(Material.IRON_AXE);
					ItemStack is = ItemBuilder.create(Material.DIAMOND_AXE, null);
					is.addEnchantment(Enchantment.DIG_SPEED, 3);
					p.getInventory().addItem(is);
					openGUI(0);
				}
				if (data.getAxeTier() == 2 && super.canPurchase(Material.GOLD_INGOT, 3, name)) {
					data.setAxeTier(3);
					p.getInventory().remove(Material.STONE_AXE);
					ItemStack is = ItemBuilder.create(Material.IRON_AXE, null);
					is.addEnchantment(Enchantment.DIG_SPEED, 2);
					p.getInventory().addItem(is);
					openGUI(0);
				}
				if (data.getAxeTier() == 1 && super.canPurchase(Material.IRON_INGOT, 10, name)) {
					data.setAxeTier(2);
					p.getInventory().remove(Material.WOODEN_AXE);
					ItemStack is = ItemBuilder.create(Material.STONE_AXE, null);
					is.addEnchantment(Enchantment.DIG_SPEED, 1);
					p.getInventory().addItem(is);
					openGUI(0);
				}
				if (data.getAxeTier() == 0 && super.canPurchase(Material.IRON_INGOT, 10, name)) {
					data.setAxeTier(1);
					ItemStack is = ItemBuilder.create(Material.WOODEN_AXE, null);
					is.addEnchantment(Enchantment.DIG_SPEED, 1);
					p.getInventory().addItem(is);
					openGUI(0);
				}
			}

			if (slot == 23 && super.canPurchase(Material.GOLD_INGOT, 2, name)) {
				p.getInventory().addItem(new ItemStack(Material.ARROW, 8));
			}
			if (slot == 32 && super.canPurchase(Material.GOLD_INGOT, 12, name)) {
				p.getInventory().addItem(ItemBuilder.create(Material.BOW, 1));
			}
			if (slot == 41 && super.canPurchase(Material.EMERALD, 6, name)) {
				ItemStack is = ItemBuilder.create(Material.BOW, 1);
				is.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
				is.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
				p.getInventory().addItem(is);
			}

			if (slot == 24 && super.canPurchase(Material.EMERALD, 1, name)) {
				ItemStack is = ItemBuilder.create(Material.POTION, ChatColor.AQUA + name);
				PotionMeta meta = (PotionMeta) is.getItemMeta();
				meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 900, 1), true);
				meta.setColor(Color.GRAY);
				is.setItemMeta(meta);
				p.getInventory().addItem(is);
			}
			if (slot == 33 && super.canPurchase(Material.EMERALD, 1, name)) {
				ItemStack is = ItemBuilder.create(Material.POTION, ChatColor.AQUA + name);
				PotionMeta meta = (PotionMeta) is.getItemMeta();
				meta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 900, 4), true);
				meta.setColor(Color.LIME);
				is.setItemMeta(meta);
				p.getInventory().addItem(is);
			}
			if (slot == 42 && super.canPurchase(Material.EMERALD, 2, name)) {
				ItemStack is = ItemBuilder.create(Material.POTION, ChatColor.AQUA + name);
				PotionMeta meta = (PotionMeta) is.getItemMeta();
				meta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 1), true);
				meta.setColor(Color.OLIVE);
				is.setItemMeta(meta);
				p.getInventory().addItem(is);
			}

			if (slot == 25 && super.canPurchase(Material.GOLD_INGOT, 3, name)) {
				p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
			}
			if (slot == 34 && super.canPurchase(Material.GOLD_INGOT, 4, name)) {
				p.getInventory().addItem(new ItemStack(Material.TNT));
			}
			if (slot == 43 && super.canPurchase(Material.GOLD_INGOT, 4, name)) {
				p.getInventory().addItem(ItemBuilder.create(Material.MILK_BUCKET, ChatColor.AQUA + name));
			}
		} else if (menu == 3) {
			boolean bought = true;
			TeamColor color = data.getIsland().getColor();
			if (slot == 19) {
				if (data.getArmor() > 0) {
					p.sendMessage(Messages.BEDWARS_ALREADY_OWN.toString());
					p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
					p.closeInventory();
				} else if (super.canPurchase(Material.IRON_INGOT, 40, name)) {
					data.setArmor(1);
					p.getInventory().setBoots(ItemBuilder.create(Material.CHAINMAIL_BOOTS, color.getTeamColor() + color.getName()));
					p.getInventory().setLeggings(ItemBuilder.create(Material.CHAINMAIL_LEGGINGS, color.getTeamColor() + color.getName()));
				}
			} else if (slot == 20) {
				if (data.getArmor() > 1) {
					p.sendMessage(Messages.BEDWARS_ALREADY_OWN.toString());
					p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
					p.closeInventory();
				} else if (super.canPurchase(Material.GOLD_INGOT, 12, name)) {
					data.setArmor(2);
					p.getInventory().setBoots(ItemBuilder.create(Material.IRON_BOOTS, color.getTeamColor() + color.getName()));
					p.getInventory().setLeggings(ItemBuilder.create(Material.IRON_LEGGINGS, color.getTeamColor() + color.getName()));
				}
			} else if (slot == 21) {
				if (data.getArmor() > 2) {
					p.sendMessage(Messages.BEDWARS_ALREADY_OWN.toString());
					p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
					p.closeInventory();
				} else if (super.canPurchase(Material.EMERALD, 6, name)) {
					data.setArmor(3);
					p.getInventory().setBoots(ItemBuilder.create(Material.DIAMOND_BOOTS, color.getTeamColor() + color.getName()));
					p.getInventory().setLeggings(ItemBuilder.create(Material.DIAMOND_LEGGINGS, color.getTeamColor() + color.getName()));
				}
			} else {
				bought = false;
			}
			if (bought && data.getIsland().getArmor() > 0) {
				ItemStack[] is = p.getInventory().getArmorContents();
				for (int x = 0; x < is.length; x++) {
					ItemStack item = is[x];
					if (item != null) {
						item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, data.getIsland().getArmor());
					}
					is[x] = item;
				}
				p.getInventory().setArmorContents(is);
			}
		} else if (menu == 2) {
			boolean sharp = data.getIsland().isSharpened();
			if (slot == 19 && super.canPurchase(Material.IRON_INGOT, 10, name)) {
				p.getInventory().remove(Material.WOODEN_SWORD);
				ItemStack is = ItemBuilder.create(Material.STONE_SWORD, null);
				if (sharp) {
					is.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				}
				p.getInventory().addItem(is);
			}
			if (slot == 20 && super.canPurchase(Material.GOLD_INGOT, 7, name)) {
				p.getInventory().remove(Material.WOODEN_SWORD);
				ItemStack is = ItemBuilder.create(Material.IRON_SWORD, null);
				if (sharp) {
					is.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				}
				p.getInventory().addItem(is);
			}
			if (slot == 21 && super.canPurchase(Material.EMERALD, 4, name)) {
				p.getInventory().remove(Material.WOODEN_SWORD);
				ItemStack is = ItemBuilder.create(Material.DIAMOND_SWORD, null);
				if (sharp) {
					is.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				}
				p.getInventory().addItem(is);
			}
			if (slot == 22 && super.canPurchase(Material.GOLD_INGOT, 5, name)) {
				ItemStack is = new ItemStack(Material.STICK);
				is.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
				p.getInventory().addItem(is);
			}
		} else if (menu == 1) {
			if (slot == 19 && super.canPurchase(Material.IRON_INGOT, 4, name)) {
				p.getInventory().addItem(new ItemStack(data.getIsland().getColor().getRealMaterial(), 16));
			}
			if (slot == 20 && super.canPurchase(Material.IRON_INGOT, 12, name)) {
				p.getInventory().addItem(new ItemStack(data.getIsland().getColor().clay, 16));
			}
			if (slot == 21 && super.canPurchase(Material.IRON_INGOT, 12, name)) {
				p.getInventory().addItem(new ItemStack(data.getIsland().getColor().stained, 4));
			}
			if (slot == 22 && super.canPurchase(Material.IRON_INGOT, 24, name)) {
				p.getInventory().addItem(new ItemStack(Material.END_STONE, 12));
			}
			if (slot == 23 && super.canPurchase(Material.IRON_INGOT, 4, name)) {
				p.getInventory().addItem(new ItemStack(Material.LADDER, 16));
			}
			if (slot == 24 && super.canPurchase(Material.GOLD_INGOT, 4, name)) {
				p.getInventory().addItem(new ItemStack(Material.OAK_PLANKS, 16));
			}
			if (slot == 25 && super.canPurchase(Material.EMERALD, 4, name)) {
				p.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 4));
			}
		} else if (menu == 5) {
			if (slot == 19 && super.canPurchase(Material.GOLD_INGOT, 2, name)) {
				p.getInventory().addItem(new ItemStack(Material.ARROW, 8));
			}
			if (slot == 20 && super.canPurchase(Material.GOLD_INGOT, 12, name)) {
				p.getInventory().addItem(ItemBuilder.create(Material.BOW, 1));
			}
			if (slot == 21 && super.canPurchase(Material.GOLD_INGOT, 24, name)) {
				ItemStack is = ItemBuilder.create(Material.BOW, 1);
				is.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
				p.getInventory().addItem(is);
			}
			if (slot == 22 && super.canPurchase(Material.EMERALD, 6, name)) {
				ItemStack is = ItemBuilder.create(Material.BOW, 1);
				is.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
				is.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
				p.getInventory().addItem(is);
			}
		} else if (menu == 4) {
			if (slot == 19) {
				if (data.getShearTier() == 1) {
					p.sendMessage(Messages.BEDWARS_ALREADY_OWN.toString());
					p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
					p.closeInventory();
					return;
				} else if (super.canPurchase(Material.IRON_INGOT, 20, name)) {
					data.setShearTier(1);
					p.getInventory().addItem(new ItemStack(Material.SHEARS));
					openGUI(4);
				}
			}
			if (slot == 20) {
				if (data.getPickAxeTier() == 4) {
					super.ownMessage();
					return;
				}
				if (data.getPickAxeTier() == 3 && super.canPurchase(Material.GOLD_INGOT, 6, name)) {
					data.setPickAxeTier(4);
					p.getInventory().remove(Material.IRON_PICKAXE);
					ItemStack is = new ItemStack(Material.DIAMOND_PICKAXE);
					is.addEnchantment(Enchantment.DIG_SPEED, 3);
					p.getInventory().addItem(is);
					openGUI(4);
				}
				if (data.getPickAxeTier() == 2 && super.canPurchase(Material.GOLD_INGOT, 3, name)) {
					data.setPickAxeTier(3);
					p.getInventory().remove(Material.STONE_PICKAXE);
					ItemStack is = new ItemStack(Material.IRON_PICKAXE);
					is.addEnchantment(Enchantment.DIG_SPEED, 2);
					p.getInventory().addItem(is);
					openGUI(4);
				}
				if (data.getPickAxeTier() == 1 && super.canPurchase(Material.IRON_INGOT, 10, name)) {
					data.setPickAxeTier(2);
					p.getInventory().remove(Material.WOODEN_PICKAXE);
					ItemStack is = new ItemStack(Material.STONE_PICKAXE);
					is.addEnchantment(Enchantment.DIG_SPEED, 1);
					p.getInventory().addItem(is);
					openGUI(4);
				}
				if (data.getPickAxeTier() == 0 && super.canPurchase(Material.IRON_INGOT, 10, name)) {
					data.setPickAxeTier(1);
					ItemStack is = new ItemStack(Material.WOODEN_PICKAXE);
					is.addEnchantment(Enchantment.DIG_SPEED, 1);
					p.getInventory().addItem(is);
					openGUI(4);
				}
			}
			if (slot == 21) {
				if (data.getAxeTier() == 4) {
					super.ownMessage();
					return;
				}
				if (data.getAxeTier() == 3 && super.canPurchase(Material.GOLD_INGOT, 6, name)) {
					data.setAxeTier(4);
					p.getInventory().remove(Material.IRON_AXE);
					ItemStack is = ItemBuilder.create(Material.DIAMOND_AXE, null);
					is.addEnchantment(Enchantment.DIG_SPEED, 3);
					p.getInventory().addItem(is);
					openGUI(4);
				}
				if (data.getAxeTier() == 2 && super.canPurchase(Material.GOLD_INGOT, 3, name)) {
					data.setAxeTier(3);
					p.getInventory().remove(Material.STONE_AXE);
					ItemStack is = ItemBuilder.create(Material.IRON_AXE, null);
					is.addEnchantment(Enchantment.DIG_SPEED, 2);
					p.getInventory().addItem(is);
					openGUI(4);
				}
				if (data.getAxeTier() == 1 && super.canPurchase(Material.IRON_INGOT, 10, name)) {
					data.setAxeTier(2);
					p.getInventory().remove(Material.WOODEN_AXE);
					ItemStack is = ItemBuilder.create(Material.STONE_AXE, null);
					is.addEnchantment(Enchantment.DIG_SPEED, 1);
					p.getInventory().addItem(is);
					openGUI(4);
				}
				if (data.getAxeTier() == 0 && super.canPurchase(Material.IRON_INGOT, 10, name)) {
					data.setAxeTier(1);
					ItemStack is = ItemBuilder.create(Material.WOODEN_AXE, null);
					is.addEnchantment(Enchantment.DIG_SPEED, 1);
					p.getInventory().addItem(is);
					openGUI(4);
				}
			}
		} else if (menu == 6) {
			if (slot == 19 && super.canPurchase(Material.EMERALD, 1, name)) {
				ItemStack is = ItemBuilder.create(Material.POTION, ChatColor.AQUA + name);
				PotionMeta meta = (PotionMeta) is.getItemMeta();
				meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 900, 1), true);
				meta.setColor(Color.GRAY);
				is.setItemMeta(meta);
				p.getInventory().addItem(is);
			}
			if (slot == 20 && super.canPurchase(Material.EMERALD, 1, name)) {
				ItemStack is = ItemBuilder.create(Material.POTION, ChatColor.AQUA + name);
				PotionMeta meta = (PotionMeta) is.getItemMeta();
				meta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 900, 4), true);
				meta.setColor(Color.LIME);
				is.setItemMeta(meta);
				p.getInventory().addItem(is);
			}
			if (slot == 21 && super.canPurchase(Material.EMERALD, 2, name)) {
				ItemStack is = ItemBuilder.create(Material.POTION, ChatColor.AQUA + name);
				PotionMeta meta = (PotionMeta) is.getItemMeta();
				meta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 1), true);
				meta.setColor(Color.OLIVE);
				is.setItemMeta(meta);
				p.getInventory().addItem(is);
			}
		} else if (menu == 7) {
			if (slot == 19 && super.canPurchase(Material.GOLD_INGOT, 3, name)) {
				p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
			}
			if (slot == 20 && super.canPurchase(Material.IRON_INGOT, 40, name)) {
				p.getInventory().addItem(new ItemStack(Material.FIRE_CHARGE));
			}
			if (slot == 21 && super.canPurchase(Material.GOLD_INGOT, 4, name)) {
				p.getInventory().addItem(new ItemStack(Material.TNT));
			}
			if (slot == 22 && super.canPurchase(Material.EMERALD, 4, name)) {
				p.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
			}/*
			if (slot == 23 && super.canPurchase(Material.EMERALD, 1, name)) {
				p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
			}*/
			if (slot == 24 && super.canPurchase(Material.EMERALD, bw.getMode() > 2 ? 4 : 2, name)) {
				p.getInventory().addItem(ItemBuilder.create(Material.EGG, ChatColor.AQUA + name));
			}
			if (slot == 25 && super.canPurchase(Material.IRON_INGOT, 24, name)) {
				p.getInventory().addItem(ItemBuilder.create(Material.CHEST, ChatColor.AQUA + name));
			}/*
			if (slot == 28 && super.canPurchase(Material.GOLD_INGOT, 4, name)) {
				p.getInventory().addItem(ItemBuilder.create(Material.MILK_BUCKET, ChatColor.AQUA + name));
			}*/
			if (slot == 23 && super.canPurchase(Material.GOLD_INGOT, 4, name)) {
				p.getInventory().addItem(ItemBuilder.create(Material.MILK_BUCKET, ChatColor.AQUA + name));
			}
		}
		if (slot == 0) {
			openGUI(0);
		} else if (slot == 1) {
			openGUI(1);
		} else if (slot == 2) {
			openGUI(2);
		} else if (slot == 3) {
			openGUI(3);
		} else if (slot == 4) {
			openGUI(4);
		} else if (slot == 5) {
			openGUI(5);
		} else if (slot == 6) {
			openGUI(6);
		} else if (slot == 7) {
			openGUI(7);
		}
	}
	
	@Override
	public void openGUI(int x) {
		menu = x;
		switching = true;
		if (x == 0) {
			inv = Bukkit.createInventory(null, 54, Messages.BEDWARS_SHOP_ITEM.toString());
			addItem(Material.IRON_INGOT, 4, 19, ItemBuilder.create(Material.WHITE_WOOL, 16, Messages.BEDWARS_SHOP_BLOCKS_1.toString(), Messages.BEDWARS_SHOP_BLOCKS_1_LORE.getList()));
			addItem(Material.GOLD_INGOT, 4, 28, ItemBuilder.create(Material.OAK_PLANKS, 16, Messages.BEDWARS_SHOP_BLOCKS_6.toString(), Messages.BEDWARS_SHOP_BLOCKS_6_LORE.getList()));
			addItem(Material.EMERALD, 4, 37, ItemBuilder.create(Material.OBSIDIAN, 4, Messages.BEDWARS_SHOP_BLOCKS_7.toString(), Messages.BEDWARS_SHOP_BLOCKS_7_LORE.getList()));

			boolean sharp = data.getIsland().isSharpened();
			addItem(Material.IRON_INGOT, 10, 20, ItemBuilder.create(Material.STONE_SWORD, sharp, Messages.BEDWARS_SHOP_MELEE_1.toString(), Messages.BEDWARS_SHOP_MELEE_1_LORE.getList()));
			addItem(Material.GOLD_INGOT, 7, 29, ItemBuilder.create(Material.IRON_SWORD, sharp, Messages.BEDWARS_SHOP_MELEE_2.toString(), Messages.BEDWARS_SHOP_MELEE_2_LORE.getList()));
			addItem(Material.EMERALD, 4, 38, ItemBuilder.create(Material.DIAMOND_SWORD, sharp, Messages.BEDWARS_SHOP_MELEE_3.toString(), Messages.BEDWARS_SHOP_MELEE_3_LORE.getList()));

			boolean armor = data.getIsland().getArmor() > 0;
			addItem(Material.IRON_INGOT, 40, 21, ItemBuilder.create(Material.CHAINMAIL_BOOTS, armor, Messages.BEDWARS_SHOP_ARMOR_1.toString(), Messages.BEDWARS_SHOP_ARMOR_1_LORE.getList()));
			addItem(Material.GOLD_INGOT, 12, 30, ItemBuilder.create(Material.IRON_BOOTS, armor, Messages.BEDWARS_SHOP_ARMOR_2.toString(), Messages.BEDWARS_SHOP_ARMOR_2_LORE.getList()));
			addItem(Material.EMERALD, 6, 39, ItemBuilder.create(Material.DIAMOND_BOOTS, armor, Messages.BEDWARS_SHOP_ARMOR_3.toString(), Messages.BEDWARS_SHOP_ARMOR_3_LORE.getList()));

			addItem(Material.IRON_INGOT, 20, 22, ItemBuilder.create(Material.SHEARS, 1, Messages.BEDWARS_SHOP_TOOLS_1.toString(), Messages.BEDWARS_SHOP_TOOLS_1_LORE.getList()));
			if (data.getPickAxeTier() == 0) {
				addItem(Material.IRON_INGOT, 10, 31, ItemBuilder.create(Material.WOODEN_PICKAXE, true, Messages.BEDWARS_SHOP_PICKAXE_TIER1.toString(), Messages.BEDWARS_SHOP_PICKAXE_TIER1_LORE.getList()));
			} else if (data.getPickAxeTier() == 1) {
				addItem(Material.IRON_INGOT, 10, 31, ItemBuilder.create(Material.STONE_PICKAXE, true, Messages.BEDWARS_SHOP_PICKAXE_TIER2.toString(), Messages.BEDWARS_SHOP_PICKAXE_TIER2_LORE.getList()));
			} else if (data.getPickAxeTier() == 2) {
				addItem(Material.GOLD_INGOT, 3, 31, ItemBuilder.create(Material.IRON_PICKAXE, true, Messages.BEDWARS_SHOP_PICKAXE_TIER3.toString(), Messages.BEDWARS_SHOP_PICKAXE_TIER3_LORE.getList()));
			} else if (data.getPickAxeTier() == 3 || data.getPickAxeTier() == 4) {
				addItem(Material.GOLD_INGOT, 6, 31, ItemBuilder.create(Material.DIAMOND_PICKAXE, true, Messages.BEDWARS_SHOP_PICKAXE_TIER4.toString(), Messages.BEDWARS_SHOP_PICKAXE_TIER4_LORE.getList()));
			}
			if (data.getAxeTier() == 0) {
				addItem(Material.IRON_INGOT, 10, 40, ItemBuilder.create(Material.WOODEN_AXE, true, Messages.BEDWARS_SHOP_AXE_TIER1.toString(), Messages.BEDWARS_SHOP_AXE_TIER1_LORE.getList()));
			} else if (data.getAxeTier() == 1) {
				addItem(Material.IRON_INGOT, 10, 40, ItemBuilder.create(Material.STONE_AXE, true, Messages.BEDWARS_SHOP_AXE_TIER2.toString(), Messages.BEDWARS_SHOP_AXE_TIER2_LORE.getList()));
			} else if (data.getAxeTier() == 2) {
				addItem(Material.GOLD_INGOT, 3, 40, ItemBuilder.create(Material.IRON_AXE, true, Messages.BEDWARS_SHOP_AXE_TIER3.toString(), Messages.BEDWARS_SHOP_AXE_TIER3_LORE.getList()));
			} else if (data.getAxeTier() == 3 || data.getAxeTier() == 4) {
				addItem(Material.GOLD_INGOT, 6, 40, ItemBuilder.create(Material.DIAMOND_AXE, true, Messages.BEDWARS_SHOP_AXE_TIER4.toString(), Messages.BEDWARS_SHOP_AXE_TIER4_LORE.getList()));
			}

			addItem(Material.GOLD_INGOT, 2, 23, ItemBuilder.create(Material.ARROW, 8, Messages.BEDWARS_SHOP_RANGED_1.toString(), Messages.BEDWARS_SHOP_RANGED_1_LORE.getList()));
			addItem(Material.GOLD_INGOT, 12, 32, ItemBuilder.create(Material.BOW, 1, Messages.BEDWARS_SHOP_RANGED_2.toString(), Messages.BEDWARS_SHOP_RANGED_2_LORE.getList()));
			addItem(Material.EMERALD, 6, 41, ItemBuilder.create(Material.BOW, true, Messages.BEDWARS_SHOP_RANGED_4.toString(), Messages.BEDWARS_SHOP_RANGED_4_LORE.getList()));

			addItem(Material.EMERALD, 1, 24, ItemBuilder.createPotion(new PotionEffect(PotionEffectType.SPEED, 900, 1), Color.GRAY, 1, Messages.BEDWARS_SHOP_POTIONS_1.toString(), Messages.BEDWARS_SHOP_POTIONS_1_LORE.getList()));
			addItem(Material.EMERALD, 1, 33, ItemBuilder.createPotion(new PotionEffect(PotionEffectType.JUMP, 900, 1), Color.LIME, 1, Messages.BEDWARS_SHOP_POTIONS_2.toString(), Messages.BEDWARS_SHOP_POTIONS_2_LORE.getList()));
			addItem(Material.EMERALD, 2, 42, ItemBuilder.createPotion(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 1), Color.OLIVE, 1, Messages.BEDWARS_SHOP_POTIONS_3.toString(), Messages.BEDWARS_SHOP_POTIONS_3_LORE.getList()));

			addItem(Material.GOLD_INGOT, 3, 25, ItemBuilder.create(Material.GOLDEN_APPLE, 1, Messages.BEDWARS_SHOP_UTILITY_1.toString(), Messages.BEDWARS_SHOP_UTILITY_1_LORE.getList()));
			addItem(Material.GOLD_INGOT, 4, 34, ItemBuilder.create(Material.TNT, 1, Messages.BEDWARS_SHOP_UTILITY_5.toString(), Messages.BEDWARS_SHOP_UTILITY_5_LORE.getList()));
			addItem(Material.GOLD_INGOT, 4, 43, ItemBuilder.create(Material.MILK_BUCKET, 1, Messages.BEDWARS_SHOP_UTILITY_10.toString(), Messages.BEDWARS_SHOP_UTILITY_10_LORE.getList()));
		} else if (x == 1) {
			inv = Bukkit.createInventory(null, 54, Messages.BEDWARS_SHOP_BLOCKS.toString());
			addItem(Material.IRON_INGOT, 4, 19, ItemBuilder.create(Material.WHITE_WOOL, 16, Messages.BEDWARS_SHOP_BLOCKS_1.toString(), Messages.BEDWARS_SHOP_BLOCKS_1_LORE.getList()));
			addItem(Material.IRON_INGOT, 12, 20, ItemBuilder.create(Material.TERRACOTTA, 16, Messages.BEDWARS_SHOP_BLOCKS_2.toString(), Messages.BEDWARS_SHOP_BLOCKS_2_LORE.getList()));
			addItem(Material.IRON_INGOT, 12, 21, ItemBuilder.create(Material.GLASS, 4, Messages.BEDWARS_SHOP_BLOCKS_3.toString(), Messages.BEDWARS_SHOP_BLOCKS_3_LORE.getList()));
			addItem(Material.IRON_INGOT, 24, 22, ItemBuilder.create(Material.END_STONE, 12,  Messages.BEDWARS_SHOP_BLOCKS_4.toString(), Messages.BEDWARS_SHOP_BLOCKS_4_LORE.getList()));
			addItem(Material.IRON_INGOT, 4, 23, ItemBuilder.create(Material.LADDER, 16, Messages.BEDWARS_SHOP_BLOCKS_5.toString(), Messages.BEDWARS_SHOP_BLOCKS_5_LORE.getList()));
			addItem(Material.GOLD_INGOT, 4, 24, ItemBuilder.create(Material.OAK_PLANKS, 16, Messages.BEDWARS_SHOP_BLOCKS_6.toString(), Messages.BEDWARS_SHOP_BLOCKS_6_LORE.getList()));
			addItem(Material.EMERALD, 4, 25, ItemBuilder.create(Material.OBSIDIAN, 4, Messages.BEDWARS_SHOP_BLOCKS_7.toString(), Messages.BEDWARS_SHOP_BLOCKS_7_LORE.getList()));
		} else if (x == 2) {
			boolean sharp = data.getIsland().isSharpened();
			inv = Bukkit.createInventory(null, 54, Messages.BEDWARS_SHOP_MELEE.toString());
			addItem(Material.IRON_INGOT, 10, 19, ItemBuilder.create(Material.STONE_SWORD, sharp, Messages.BEDWARS_SHOP_MELEE_1.toString(), Messages.BEDWARS_SHOP_MELEE_1_LORE.getList()));
			addItem(Material.GOLD_INGOT, 7, 20, ItemBuilder.create(Material.IRON_SWORD, sharp, Messages.BEDWARS_SHOP_MELEE_2.toString(), Messages.BEDWARS_SHOP_MELEE_2_LORE.getList()));
			addItem(Material.EMERALD, 4, 21, ItemBuilder.create(Material.DIAMOND_SWORD, sharp, Messages.BEDWARS_SHOP_MELEE_3.toString(), Messages.BEDWARS_SHOP_MELEE_3_LORE.getList()));
			addItem(Material.GOLD_INGOT, 5, 22, ItemBuilder.create(Material.STICK, true, Messages.BEDWARS_SHOP_MELEE_4.toString(), Messages.BEDWARS_SHOP_MELEE_4_LORE.getList()));
		} else if (x == 3) {
			inv = Bukkit.createInventory(null, 54, Messages.BEDWARS_SHOP_ARMOR.toString());
			boolean armor = data.getIsland().getArmor() > 0;
			addItem(Material.IRON_INGOT, 40, 19, ItemBuilder.create(Material.CHAINMAIL_BOOTS, armor, Messages.BEDWARS_SHOP_ARMOR_1.toString(), Messages.BEDWARS_SHOP_ARMOR_1_LORE.getList()));
			addItem(Material.GOLD_INGOT, 12, 20, ItemBuilder.create(Material.IRON_BOOTS, armor, Messages.BEDWARS_SHOP_ARMOR_2.toString(), Messages.BEDWARS_SHOP_ARMOR_2_LORE.getList()));
			addItem(Material.EMERALD, 6, 21, ItemBuilder.create(Material.DIAMOND_BOOTS, armor, Messages.BEDWARS_SHOP_ARMOR_3.toString(), Messages.BEDWARS_SHOP_ARMOR_3_LORE.getList()));
		} else if (x == 4) {
			inv = Bukkit.createInventory(null, 54, Messages.BEDWARS_SHOP_TOOLS.toString());
			addItem(Material.IRON_INGOT, 20, 19, ItemBuilder.create(Material.SHEARS, 1, Messages.BEDWARS_SHOP_TOOLS_1.toString(), Messages.BEDWARS_SHOP_TOOLS_1_LORE.getList()));
			if (data.getPickAxeTier() == 0) {
				addItem(Material.IRON_INGOT, 10, 20, ItemBuilder.create(Material.WOODEN_PICKAXE, true, Messages.BEDWARS_SHOP_PICKAXE_TIER1.toString(), Messages.BEDWARS_SHOP_PICKAXE_TIER1_LORE.getList()));
			} else if (data.getPickAxeTier() == 1) {
				addItem(Material.IRON_INGOT, 10, 20, ItemBuilder.create(Material.STONE_PICKAXE, true, Messages.BEDWARS_SHOP_PICKAXE_TIER2.toString(), Messages.BEDWARS_SHOP_PICKAXE_TIER2_LORE.getList()));
			} else if (data.getPickAxeTier() == 2) {
				addItem(Material.GOLD_INGOT, 3, 20, ItemBuilder.create(Material.IRON_PICKAXE, true, Messages.BEDWARS_SHOP_PICKAXE_TIER3.toString(), Messages.BEDWARS_SHOP_PICKAXE_TIER3_LORE.getList()));
			} else if (data.getPickAxeTier() == 3 || data.getPickAxeTier() == 4) {
				addItem(Material.GOLD_INGOT, 6, 20, ItemBuilder.create(Material.DIAMOND_PICKAXE, true, Messages.BEDWARS_SHOP_PICKAXE_TIER4.toString(), Messages.BEDWARS_SHOP_PICKAXE_TIER4_LORE.getList()));
			}
			if (data.getAxeTier() == 0) {
				addItem(Material.IRON_INGOT, 10, 21, ItemBuilder.create(Material.WOODEN_AXE, true, Messages.BEDWARS_SHOP_AXE_TIER1.toString(), Messages.BEDWARS_SHOP_AXE_TIER1_LORE.getList()));
			} else if (data.getAxeTier() == 1) {
				addItem(Material.IRON_INGOT, 10, 21, ItemBuilder.create(Material.STONE_AXE, true, Messages.BEDWARS_SHOP_AXE_TIER2.toString(), Messages.BEDWARS_SHOP_AXE_TIER2_LORE.getList()));
			} else if (data.getAxeTier() == 2) {
				addItem(Material.GOLD_INGOT, 3, 21, ItemBuilder.create(Material.IRON_AXE, true, Messages.BEDWARS_SHOP_AXE_TIER3.toString(), Messages.BEDWARS_SHOP_AXE_TIER3_LORE.getList()));
			} else if (data.getAxeTier() == 3 || data.getAxeTier() == 4) {
				addItem(Material.GOLD_INGOT, 6, 21, ItemBuilder.create(Material.DIAMOND_AXE, true, Messages.BEDWARS_SHOP_AXE_TIER4.toString(), Messages.BEDWARS_SHOP_AXE_TIER4_LORE.getList()));
			}
		} else if (x == 5) {
			inv = Bukkit.createInventory(null, 54, Messages.BEDWARS_SHOP_RANGED.toString());
			addItem(Material.GOLD_INGOT, 2, 19, ItemBuilder.create(Material.ARROW, 8, Messages.BEDWARS_SHOP_RANGED_1.toString(), Messages.BEDWARS_SHOP_RANGED_1_LORE.getList()));
			addItem(Material.GOLD_INGOT, 12, 20, ItemBuilder.create(Material.BOW, 1, Messages.BEDWARS_SHOP_RANGED_2.toString(), Messages.BEDWARS_SHOP_RANGED_2_LORE.getList()));
			addItem(Material.GOLD_INGOT, 24, 21, ItemBuilder.create(Material.BOW, true, Messages.BEDWARS_SHOP_RANGED_3.toString(), Messages.BEDWARS_SHOP_RANGED_3_LORE.getList()));
			addItem(Material.EMERALD, 6, 22, ItemBuilder.create(Material.BOW, true, Messages.BEDWARS_SHOP_RANGED_4.toString(), Messages.BEDWARS_SHOP_RANGED_4_LORE.getList()));
		} else if (x == 6) {
			inv = Bukkit.createInventory(null, 54, Messages.BEDWARS_SHOP_POTIONS.toString());
			addItem(Material.EMERALD, 1, 19, ItemBuilder.createPotion(new PotionEffect(PotionEffectType.SPEED, 900, 1), Color.GRAY, 1, Messages.BEDWARS_SHOP_POTIONS_1.toString(), Messages.BEDWARS_SHOP_POTIONS_1_LORE.getList()));
			addItem(Material.EMERALD, 1, 20, ItemBuilder.createPotion(new PotionEffect(PotionEffectType.JUMP, 900, 1), Color.LIME, 1, Messages.BEDWARS_SHOP_POTIONS_2.toString(), Messages.BEDWARS_SHOP_POTIONS_2_LORE.getList()));
			addItem(Material.EMERALD, 2, 21, ItemBuilder.createPotion(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 1), Color.OLIVE, 1, Messages.BEDWARS_SHOP_POTIONS_3.toString(), Messages.BEDWARS_SHOP_POTIONS_3_LORE.getList()));
		} else if (x == 7) {
			inv = Bukkit.createInventory(null, 54, Messages.BEDWARS_SHOP_UTILITY.toString());
			addItem(Material.GOLD_INGOT, 3, 19, ItemBuilder.create(Material.GOLDEN_APPLE, 1, Messages.BEDWARS_SHOP_UTILITY_1.toString(), Messages.BEDWARS_SHOP_UTILITY_1_LORE.getList()));
			addItem(Material.IRON_INGOT, 40, 20, ItemBuilder.create(Material.FIRE_CHARGE, 1, Messages.BEDWARS_SHOP_UTILITY_4.toString(), Messages.BEDWARS_SHOP_UTILITY_4_LORE.getList()));
			addItem(Material.GOLD_INGOT, 4, 21, ItemBuilder.create(Material.TNT, 1, Messages.BEDWARS_SHOP_UTILITY_5.toString(), Messages.BEDWARS_SHOP_UTILITY_5_LORE.getList()));
			addItem(Material.EMERALD, 4, 22, ItemBuilder.create(Material.ENDER_PEARL, 1, Messages.BEDWARS_SHOP_UTILITY_6.toString(), Messages.BEDWARS_SHOP_UTILITY_6_LORE.getList()));
			//addItem(Material.EMERALD, 1, 23, ItemBuilder.create(Material.WATER_BUCKET, 1, Messages.BEDWARS_SHOP_UTILITY_7.toString(), Messages.BEDWARS_SHOP_UTILITY_7_LORE.getList()));
			addItem(Material.EMERALD, bw.getMode() > 2 ? 4 : 2, 24, ItemBuilder.create(Material.EGG, 1, Messages.BEDWARS_SHOP_UTILITY_8.toString(), Messages.BEDWARS_SHOP_UTILITY_8_LORE.getList()));
			addItem(Material.IRON_INGOT, 24, 25, ItemBuilder.create(Material.CHEST, 1, Messages.BEDWARS_SHOP_UTILITY_9.toString(), Messages.BEDWARS_SHOP_UTILITY_9_LORE.getList()));
			//addItem(Material.GOLD_INGOT, 4, 28, ItemBuilder.create(Material.MILK_BUCKET, 1, Messages.BEDWARS_SHOP_UTILITY_10.toString(), Messages.BEDWARS_SHOP_UTILITY_10_LORE.getList()));
			addItem(Material.GOLD_INGOT, 4, 23, ItemBuilder.create(Material.MILK_BUCKET, 1, Messages.BEDWARS_SHOP_UTILITY_10.toString(), Messages.BEDWARS_SHOP_UTILITY_10_LORE.getList()));
		}

		inv.setItem(0, ItemBuilder.create(Material.NETHER_STAR, "§a"+Messages.BEDWARS_SHOP_ITEM.toString()));
		inv.setItem(1, ItemBuilder.create(Material.TERRACOTTA, "§a"+Messages.BEDWARS_SHOP_BLOCKS.toString(), Messages.BEDWARS_SHOP_BLOCKS_LORE.getList()));
		inv.setItem(2, ItemBuilder.create(Material.GOLDEN_SWORD, data.getIsland().isSharpened(), "§a"+Messages.BEDWARS_SHOP_MELEE.toString(), Messages.BEDWARS_SHOP_MELEE_LORE.getList()));
		inv.setItem(3, ItemBuilder.create(Material.CHAINMAIL_BOOTS, data.getIsland().getArmor() > 0, "§a"+Messages.BEDWARS_SHOP_ARMOR.toString(), Messages.BEDWARS_SHOP_ARMOR_LORE.getList()));
		inv.setItem(4, ItemBuilder.create(Material.STONE_PICKAXE, "§a"+Messages.BEDWARS_SHOP_TOOLS.toString(), Messages.BEDWARS_SHOP_TOOLS_LORE.getList()));
		inv.setItem(5, ItemBuilder.create(Material.BOW, "§a"+Messages.BEDWARS_SHOP_RANGED.toString(), Messages.BEDWARS_SHOP_RANGED_LORE.getList()));
		inv.setItem(6, ItemBuilder.create(Material.BREWING_STAND, "§a"+Messages.BEDWARS_SHOP_POTIONS.toString(), Messages.BEDWARS_SHOP_POTIONS_LORE.getList()));
		inv.setItem(7, ItemBuilder.create(Material.TNT, "§a"+Messages.BEDWARS_SHOP_UTILITY.toString(), Messages.BEDWARS_SHOP_UTILITY_LORE.getList()));
		
		for (int i = 9; i < 18; i++) {
			inv.setItem(i, ItemBuilder.create(x == i-9 ? Material.GREEN_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE, Messages.BEDWARS_SHOP_CATEGORIES.toString(), Arrays.asList(Messages.BEDWARS_SHOP_ITEMS.toString())));
		}
		
		p.openInventory(inv);
		
		switching = false;
	}
	
	private void addItem(Material resource, int amount, int slot, ItemStack is) {
		boolean maxed = false;
		if (menu == 4) {
			if (slot == 10 && data.getShearTier() == 1) {
				maxed = true;
			}
			if (slot == 11 && data.getPickAxeTier() == 4) {
				maxed = true;
			}
			if (slot == 12 && data.getAxeTier() == 4) {
				maxed = true;
			}
		}
		super.setPrice(resource, amount, slot, 0, is, maxed);
	}
	
}
