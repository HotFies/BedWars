package ro.Aneras.ClashWars.Api;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends Event {
	
	private Player[] top;
	private List<Player> players;
	private static final HandlerList handlers = new HandlerList();

	public GameEndEvent(List<Player> players, Player[] top) {
		this.top = top;
		this.players = players;
	}

	public Player[] getTop() {
		return top;
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
