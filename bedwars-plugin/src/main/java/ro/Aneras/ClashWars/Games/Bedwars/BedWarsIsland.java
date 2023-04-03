package ro.Aneras.ClashWars.Games.Bedwars;

import org.bukkit.Location;
import ro.Aneras.ClashWars.Games.GameTeam;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;

public class BedWarsIsland extends GameTeam {
	
	private int haste;
	private int armor;
	private Location bed;
	private Location iron;
	private int alive = 0;
	private int forge = 0;
	private boolean regen;
	private Location shop;
	private boolean hasbed;
	private boolean sharp;
	private Location spawn;
	private boolean isActive;
	private Location upgrade;
	private boolean trap_slow;
	private boolean blind_trap;
	
	public BedWarsIsland(TeamColor color, Location spawn, Location shop, Location upgrade, Location iron, Location bed) {
		super(color);
		this.shop = shop;
		this.upgrade = upgrade;
		this.iron = iron;
		this.bed = bed;
		this.spawn = spawn;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void isActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean hasBed() {
		return hasbed;
	}
	
	public void hasBed(boolean bed) {
		hasbed = bed;
	}
	
	public boolean isSharpened() {
		return sharp;
	}
	
	public void setSharpened(boolean sharp) {
		this.sharp = sharp;
	}
	
	public boolean isRegenerating() {
		return regen;
	}
	
	public void setRegenerating(boolean regen) {
		this.regen = regen;
	}
	
	public boolean hasSlowTrap() {
		return trap_slow;
	}
	
	public void setSlowTrap(boolean slow) {
		this.trap_slow = slow;
	}
	
	public int getHaste() {
		return haste;
	}
	
	public void setHaste(int haste) {
		this.haste = haste;
	}
	
	public int getForge() {
		return forge;
	}
	
	public void setForge(int forge) {
		this.forge = forge;
	}
	
	public int getArmor() {
		return armor;
	}
	
	public void setArmor(int armor) {
		this.armor = armor;
	}
	
	public boolean hasBlindTrap() {
		return blind_trap;
	}
	
	public void setBlindTrap(boolean blind) {
		this.blind_trap = blind;
	}
	
	public Location getBed() {
		return bed;
	}
	
	public int getAlive() {
		return alive;
	}
	
	public Location getIron() {
		return iron;
	}
	
	public Location getShop() {
		return shop;
	}
	
	public Location getUpgrade() {
		return upgrade;
	}
	
	public Location getSpawn() {
		return spawn;
	}
	
	public void setAlive(int alive) {
		this.alive = alive;
	}
	
}
