package ro.Aneras.ClashWars.Api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.Aneras.ClashWars.Games.Game;

public class GameStartEvent extends Event {

    private final Game game;
    private static final HandlerList handlers = new HandlerList();

    public GameStartEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
