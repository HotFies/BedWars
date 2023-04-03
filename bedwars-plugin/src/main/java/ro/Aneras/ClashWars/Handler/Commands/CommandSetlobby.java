package ro.Aneras.ClashWars.Handler.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Handler.Configuration.Configuration;
import ro.Aneras.ClashWars.Handler.Tools.Serializer;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;

public class CommandSetlobby implements CommandInterface {

	private final Main main;

	public CommandSetlobby(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "setlobby";
	}

	@Override
	public String[] getArguments() {
		return new String[0];
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return p.hasPermission("bw.admin");
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		Configuration config = main.getConfiguration();
		config.set("Game.Lobby", Serializer.getSerializedLocation(p.getLocation(), false));
		config.save();
		main.getManager().spawn = p.getLocation().clone();
		p.sendMessage(Messages.PREFIX+" §aLobby location has been set.");
	}

}
