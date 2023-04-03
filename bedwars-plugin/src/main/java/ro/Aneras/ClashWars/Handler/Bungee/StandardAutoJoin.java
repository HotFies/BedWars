package ro.Aneras.ClashWars.Handler.Bungee;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import ro.Aneras.ClashWars.Api.GameLeaveEvent;
import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Handler.GameState;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

public class StandardAutoJoin implements AutoJoin, Listener {

    private int count;
    private Main main;
    private String server;
    private boolean shutdownAtEnd;
    private boolean kickPlayer;

    public StandardAutoJoin(Main main, boolean shutdownAtEnd, String server, boolean kickPlayer) {
        this.main = main;
        this.server = server;
        this.kickPlayer = kickPlayer;
        this.shutdownAtEnd = shutdownAtEnd;
        main.getServer().getPluginManager().registerEvents(this, main);
        main.getServer().getMessenger().registerOutgoingPluginChannel(main, "BungeeCord");
        main.getServer().getOnlinePlayers().forEach(p -> p.kickPlayer("§cAutoJoin has been enabled!"));
    }

    private Game getGame() {
        if (main.getManager().getGames().isEmpty()) {
            return null;
        }
        return main.getManager().getGames().get(count % main.getManager().getGames().size());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Bukkit.getScheduler().runTask(main, () -> {
            main.getManager().addPlayer(getGame(), e.getPlayer());
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        Game found = getGame();
        if (found == null) {
            e.setMotd("§c§lYou need to have games to activate AutoJoin");
        } else {
            e.setMaxPlayers(found.getSettings().getMax());
            e.setMotd(found.getState().getState());
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Game found = getGame();
        if (found == null || found.getState() != GameState.WAITING) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Messages.GAME_NO_GAME.toString());
        } else if (found.getPlayers().size() >= found.getSettings().getMax()) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Messages.GAME_FULL.toString());
        }
    }

    @EventHandler
    public void onLeave(GameLeaveEvent e) {
        sendToHub(e.getPlayer());
    }

    public void onStop(List<Player> players) {
        if (main.isStopping()) {
            return;
        }
        count++;
        players.forEach(p -> sendToHub(p));
        if (shutdownAtEnd) {
            main.getServer().shutdown();
        }
    }

    private void sendToHub(Player p) {
        try (ByteArrayOutputStream data = new ByteArrayOutputStream(); DataOutputStream out = new DataOutputStream(data);) {
            out.writeUTF("Connect");
            out.writeUTF(server);
            p.sendPluginMessage(main, "BungeeCord", data.toByteArray());
            if (kickPlayer && p.isOnline()) {
                main.getServer().getScheduler().runTaskLater(main, () -> {
                    if (p.isOnline()) {
                        p.kickPlayer("AutoJoin:Kicked");
                    }
                }, 40L);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
