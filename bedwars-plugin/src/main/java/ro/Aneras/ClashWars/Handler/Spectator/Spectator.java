package ro.Aneras.ClashWars.Handler.Spectator;

import org.bukkit.inventory.Inventory;

import ro.Aneras.ClashWars.Games.Game;

public class Spectator {
	
	private boolean respawn;
	private Inventory opened;
	private final long how_long;

	public Spectator(Game g, boolean respawn) {
		how_long = System.currentTimeMillis();
		this.respawn = respawn;
	}
	
	public boolean isRespawning() {
		return respawn;
	}
	
	public Inventory getOpened() {
		return opened;
	}
	
	public double getHowLong() {
		return Math.floor((System.currentTimeMillis()-how_long)/1000);
	}
	
}
