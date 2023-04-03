package ro.Aneras.ClashWars.Handler.Bungee;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import ro.Aneras.ClashWars.Api.GameLeaveEvent;
import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Handler.GameState;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

public class AutoScaleAutoJoin implements AutoJoin, Listener {

	private Main arcade;
	private boolean kickPlayer;
	private String hub_server;
	
	public AutoScaleAutoJoin(Main arcade, String hub_server, boolean kickPlayer) {
		this.arcade = arcade;
		this.kickPlayer = kickPlayer;
		this.hub_server = hub_server;
		arcade.getServer().getPluginManager().registerEvents(this, arcade);
		arcade.getServer().getMessenger().registerOutgoingPluginChannel(arcade, "BungeeCord");
		arcade.getServer().getOnlinePlayers().forEach(p -> p.kickPlayer("§cAuto Scale AutoJoin has been enabled!"));
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		Player p = e.getPlayer();
		Bukkit.getScheduler().runTask(arcade, () -> {
			Game found = arcade.getManager().findGame(GameType.BEDWARS, p,-1);
			arcade.getManager().addPlayer(found, p);
		});
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent e) {
		if (arcade.getManager().getGames().isEmpty()) {
			e.setMotd("§c§lYou need to have games to activate AutoJoin");
		} else {
			Game found = arcade.getManager().findGame(GameType.BEDWARS, null, -1);
			e.setMaxPlayers(found.getSettings().getMax());
			e.setMotd(found.getState().getState());
		}
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if (arcade.getManager().getGames().isEmpty()) {
			e.disallow(Result.KICK_OTHER, "You need to add games first!");
		} else {
			Game found = arcade.getManager().findGame(GameType.BEDWARS, e.getPlayer(), -1);
			if (found == null || found.getState() != GameState.WAITING) {
				e.disallow(Result.KICK_OTHER, Messages.GAME_NO_GAME.toString());
			} else if (found.getPlayers().size() >= found.getSettings().getMax()) {
				e.disallow(Result.KICK_OTHER, Messages.GAME_FULL.toString());
			}
		}
	}

	@EventHandler
	public void onLeave(GameLeaveEvent e) {
		sendToHub(e.getPlayer());
	}
	
	@Override
	public void unregister() {
		HandlerList.unregisterAll(this);
	}
	
	public void onStop(List<Player> players) {
		if (arcade.isStopping()) {
			return;
		}
		for (Player p : players) {
			Game found = arcade.getManager().findGame(GameType.BEDWARS, p, -1);
			if (found.getState() == GameState.WAITING && found.getPlayers().size() < found.getSettings().getMax()) {
				arcade.getManager().addPlayer(found, p);
			} else {
				sendToHub(p);
			}
		}
	}
	
	private void sendToHub(Player p) {
		try (ByteArrayOutputStream data = new ByteArrayOutputStream(); DataOutputStream out = new DataOutputStream(data);) {
			out.writeUTF("Connect");
			out.writeUTF(hub_server);
			p.sendPluginMessage(arcade, "BungeeCord", data.toByteArray());
			if (kickPlayer && p.isOnline()) {
				arcade.getServer().getScheduler().runTaskLater(arcade, () -> {
					if (p.isOnline()) {
						p.kickPlayer("AutoJoin:Kicked");
					}
				}, 40L);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
