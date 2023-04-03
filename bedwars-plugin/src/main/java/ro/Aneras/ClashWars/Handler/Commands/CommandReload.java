package ro.Aneras.ClashWars.Handler.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;

public class CommandReload implements CommandInterface {

	private Main main;
	public CommandReload(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "reload";
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
		main.onDisable();
		main.onEnable();
		p.sendMessage(Messages.PREFIX + " §aBedWars has been reloaded succesfuly!");
	}

}
