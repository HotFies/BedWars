package ro.Aneras.ClashWars.Handler.Tools;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class ItemBuilder {

	public static ItemStack[] create(String name, Color color) {
		ItemStack[] is = new ItemStack[4];
		is[3] = create(Material.LEATHER_HELMET, 1, name, null, false, color, true);
		is[2] = create(Material.LEATHER_CHESTPLATE, 1, name, null, false, color, true);
		is[1] = create(Material.LEATHER_LEGGINGS, 1, name, null, false, color, true);
		is[0] = create(Material.LEATHER_BOOTS, 1, name, null, false, color, true);
		return is;
	}
	
	public static ItemStack create(Material m, int amount) {
		return create(m, amount, null, null, false, null, true);
	}
	
	public static ItemStack create(Material m, String name) {
		return create(m, 1, name, null, false, null, true);
	}
	
	public static ItemStack create(Material m, Color color, String name) {
		return create(m, 1, name, null, false, color, true);
	}
	
	public static ItemStack create(Material m, boolean glow, String name) {
		return create(m, 1, name, null, glow, null, true);
	}
	
	public static ItemStack create(Material m, boolean glow, String name, List<String> lore) {
		return create(m, 1, name, lore, glow, null, true);
	}
	
	public static ItemStack create(Material m, int amount, String name, List<String> lore) {
		return create(m, amount, name, lore, false, null, true);
	}
	
	public static ItemStack createPotion(PotionEffect effect, Color color, int amount, String name, List<String> lore) {
		ItemStack stack = create(Material.POTION, amount, name, lore, false, null, true);
		PotionMeta meta = (PotionMeta) stack.getItemMeta();
		meta.addCustomEffect(effect, true);
		meta.setColor(color);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack create(Material m, String name, List<String> lore) {
		return create(m, 1, name, lore, false, null, true);
	}
	
	public static ItemStack create(Material m, int number, String name, String lore) {
		return create(m, number, name, Arrays.asList(lore), false, null, true);
	}
	
	private static ItemStack create(Material m, int number, String name, List<String> lore, boolean glow, Color color, boolean hide) {
		ItemStack is = new ItemStack(m, number);
		ItemMeta im = is.getItemMeta();
		if (glow) {
			im.addEnchant(Enchantment.DURABILITY, 1, true);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		im.setUnbreakable(true);
		im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		if (hide) {
			im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		}
		if (name != null) {
			im.setDisplayName(name.replace("&", "§"));
		}
		if (lore != null) {
			im.setLore(lore);
		}
		if (color != null) {
			LeatherArmorMeta meta = (LeatherArmorMeta) im;
			meta.setColor(color);
		}
		is.setItemMeta(im);

		return is;
	}
	
}