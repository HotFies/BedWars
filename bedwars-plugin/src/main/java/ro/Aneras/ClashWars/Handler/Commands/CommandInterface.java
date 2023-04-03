package ro.Aneras.ClashWars.Handler.Commands;

import org.bukkit.entity.Player;

public interface CommandInterface {

	public String getCommand();
	
	public String[] getArguments();
	
	public boolean hasPermission(Player p);
	
	public void executeCommand(Player p, String[] args);
	
}
