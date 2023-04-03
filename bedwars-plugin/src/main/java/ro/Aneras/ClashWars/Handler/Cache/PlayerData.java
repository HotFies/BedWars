package ro.Aneras.ClashWars.Handler.Cache;

import java.util.Collection;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

import ro.Aneras.ClashWars.Main;

public class PlayerData {

	private Main main;
	private final float xp;
	private final int food;
	private final int level;
	private final Player p;
	private final long how_long;
	private final Location loc;
	private final GameMode mode;
	private final double health;
	private final Scoreboard board;
	private final int fireticks;
	private final float flyspeed;
	private final float walkspeed;
	private final boolean isFlying;
	private final ItemStack[] ender;
	private final float fallDistance;
	private final ItemStack[] armour;
	private ItemStack[] inventory;
	private final Collection<PotionEffect> effects;

	public PlayerData(Main main, Player p) {
		this.p = p;
		if (p.isDead()) {
			p.spigot().respawn();
		}
		xp = p.getExp();
		this.main = main;
		level = p.getLevel();
		health = p.getHealth();
		mode = p.getGameMode();
		food = p.getFoodLevel();
		board = p.getScoreboard();
		ender = p.getEnderChest().getContents().clone();
		isFlying = p.getAllowFlight();
		flyspeed = p.getFlySpeed();
		fireticks = p.getFireTicks();
		walkspeed = p.getWalkSpeed();
		loc = p.getLocation().clone();
		fallDistance = p.getFallDistance();
		effects = p.getActivePotionEffects();
		inventory = p.getInventory().getContents().clone();
		armour = p.getInventory().getArmorContents();
		how_long = System.currentTimeMillis();
		main.getManager().reset(p);
	}
	
	public double getHowLong() {
		return Math.floor((System.currentTimeMillis()-how_long)/1000);
	}

	public void restore(boolean teleport) {
		p.setExp(xp);
		p.setLevel(level);
		p.setGameMode(mode);
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
		p.setHealth(health > 20 ? 20 : health);
		p.setFoodLevel(food);
		if (isFlying) {
			p.setAllowFlight(true);
			p.setFlying(true);
		} else {
			p.setAllowFlight(true);
			p.setFlying(false);
			p.setAllowFlight(false);
		}
		p.setFlySpeed(flyspeed);
		p.setFireTicks(fireticks);
		p.setWalkSpeed(walkspeed);
		p.getEnderChest().setContents(ender);
		p.getInventory().setArmorContents(armour);
		p.getInventory().setContents(inventory);
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
		for (PotionEffect effect : effects) {
			p.addPotionEffect(effect);
		}
		if (main.getManager().spawn != null) {
			main.getManager().spawn.getChunk().load();
			p.teleport(main.getManager().spawn);
		} else if (teleport) {
			loc.getChunk().load();
			p.teleport(loc);
		}
		p.setFallDistance(fallDistance);
		p.setScoreboard(board);
	}
}