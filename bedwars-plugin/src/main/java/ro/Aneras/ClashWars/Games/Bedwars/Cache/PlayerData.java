package ro.Aneras.ClashWars.Games.Bedwars.Cache;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ro.Aneras.ClashWars.Games.Bedwars.BedWarsIsland;
import ro.Aneras.ClashWars.Games.Bedwars.GUI.GUI;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Version.PlayerHologram;

public class PlayerData {

	private int timer;

	private GUI gui;
	private int[] stats;
	private int axe = 0;
	private int shears = 0;
	private int pickaxe = 0;
	private Player last_dmg;
	private ItemStack[] respawn_armor;
	private boolean isInvisible;
	private int trapImmunity;
	private BedWarsIsland island;
	private PlayerHologram playerHologram;
	private long immune = System.currentTimeMillis();
	private int armor;
	private int lastDamagerTimer;
	
	public PlayerData(Main main, Location l, BedWarsIsland island) {
		stats = new int[6];
		this.island = island;
	}
	
	public void respawn(Player p) {
		respawn_armor = p.getInventory().getArmorContents();
		this.timer = 5;
		last_dmg = null;
	}
	
	public boolean isImmune() {
		return (System.currentTimeMillis()-immune) < 1500;
	}
	
	public int getTimer() {
		return timer;
	}
	
	public int getAxeTier() {
		return axe;
	}
	
	public int getTrapImmunity() {
		return trapImmunity;
	}
	
	public void setTrapImmunity(int trapImmunity) {
		this.trapImmunity = trapImmunity;
	}
	
	public int getLastDamagerTimer() {
		return lastDamagerTimer;
	}

	public void setLastDamagerTimer(int lastDamagerTimer) {
		this.lastDamagerTimer = lastDamagerTimer;
	}
	
	public int[] getStats() {
		return stats;
	}
	
	public void setAxeTier(int axe) {
		this.axe = axe;
	}
	
	public int getShearTier() {
		return shears;
	}
	
	public PlayerHologram getHologram() {
		return playerHologram;
	}
	
	public void setHologram(PlayerHologram playerHologram) {
		this.playerHologram = playerHologram;
	}
	
	public void setShearTier(int shears) {
		this.shears = shears;
	}
	
	public int getPickAxeTier() {
		return pickaxe;
	}
	
	public void setPickAxeTier(int pickaxe) {
		this.pickaxe = pickaxe;
	}
	
	public BedWarsIsland getIsland() {
		return island;
	}
	
	public GUI getGUI() {
		return gui;
	}
	
	public void setGUI(GUI gui) {
		this.gui = gui;
	}
	
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
	public Player getLastDamager() {
		return last_dmg;
	}
	
	public void setLastDamager(Player dmg) {
		last_dmg = dmg;
		lastDamagerTimer = 15;
	}
	
	public ItemStack[] getRespawnItems() {
		return respawn_armor;
	}
	
	public void setRespawnItems(ItemStack[] armor) {
		immune = System.currentTimeMillis();
		this.respawn_armor = armor;
	}
	
	public void isInvisible(boolean invisible) {
		this.isInvisible = invisible;
	}
	
	public boolean isInvisible() {
		return isInvisible;
	}

	public int getArmor() {
		return armor;
	}
	
	public void setArmor(int armor) {
		this.armor = armor; 
	}
	
}
