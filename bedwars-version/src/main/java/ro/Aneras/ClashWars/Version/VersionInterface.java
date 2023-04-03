package ro.Aneras.ClashWars.Version;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public interface VersionInterface {

	void removeStuckArrows(Player p);

	ArmorStand createGenerator(Location l);
	
	default void sendActionBar(Player p, String message) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}
	
	PlayerHologram createHologram(Player p, Location l);

	void isArmorVisible(boolean value, Player p, List<Player> players);
	
	void sendTitle(Player p, int fadeIn, int stay, int fadeOut, String title, String subtitle);
	
	void removeFromShittyCollection(Scoreboard scoreboard);

	Fireball launchFireball(Player p);

	void respawn(Player p);
	
}