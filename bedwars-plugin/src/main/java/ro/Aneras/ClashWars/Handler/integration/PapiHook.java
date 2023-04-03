package ro.Aneras.ClashWars.Handler.integration;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Handler.GameState;

public class PapiHook extends PlaceholderExpansion implements PapiAdapter {

	protected Main main;
	
	public PapiHook(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean canRegister() {
		return true;
	}
	
	@Override
    public boolean persist() {
        return true;
    }
	
	@Override
	public String getAuthor() {
		return "Fr33styler";
	}
	
	@Override
	public String getIdentifier() {
		return "bedwars";
	}
	
	@Override
	public String getVersion() {
		return main.getDescription().getDescription();
	}
	
	public void onJoin(Player p) {}
	
	public void onLeave(Player p) {}
	
	public void onTick(long ticks) {}
	
	@Override
	public void enable() {
		register();
	}

	@Override
	public void disable() {
		unregister();
	}
	
	@Override
	public String onRequest(OfflinePlayer p, String identifier) {
		int ongoing = 0;
		int players = 0;
		for (Game game : main.getManager().getGames()) {
			if (identifier.equals(game.getID() + "_players")) {
				return String.valueOf(game.getPlayers().size());
			}
			if (identifier.equals(game.getID() + "_max_players")) {
				return String.valueOf(game.getSettings().getMax());
			}
			GameState state = game.getState();
			if (identifier.equals(game.getID() + "_status")) {
				return state.getState();
			}
			if (state == GameState.IN_GAME || state == GameState.END) {
				ongoing++;
			}
			players += game.getPlayers().size();
		}
		if (identifier.equals("allplayers")) {
			return String.valueOf(players);
		}
		if (identifier.equals("ongoing")) {
			return String.valueOf(ongoing);
		}
		return null;
	}
	
}
