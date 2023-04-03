package ro.Aneras.ClashWars.Handler.Bungee;

import org.bukkit.entity.Player;

import java.util.List;

public interface AutoJoin {

	void onStop(List<Player> players);

	void unregister();
}