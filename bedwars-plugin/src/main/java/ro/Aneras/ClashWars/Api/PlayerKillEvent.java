package ro.Aneras.ClashWars.Api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKillEvent extends Event {
	
	private Player p;
	private boolean value;
	private static final HandlerList handlers = new HandlerList();

	public PlayerKillEvent(Player p, boolean value) {
		this.p = p;
		this.value = value;
	}
	
	public Player getPlayer() {
		return p;
	}

	public boolean isRespawing() {
		return value;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
