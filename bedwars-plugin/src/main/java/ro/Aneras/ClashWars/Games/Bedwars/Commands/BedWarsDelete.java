package ro.Aneras.ClashWars.Games.Bedwars.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Handler.Configuration.Configuration;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;

public class BedWarsDelete implements CommandInterface {

	private Main main;

	public BedWarsDelete(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "delete";
	}

	@Override
	public String[] getArguments() {
		return new String[] { "<id>" };
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return p.hasPermission("bw.admin");
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		try {
			int id = Integer.parseInt(args[1]);
			GameType type = GameType.BEDWARS;
			Game g = main.getManager().getGame(id, type);
			if (g == null) {
				p.sendMessage(Messages.PREFIX + " §cNo game found with this ID.");
			} else {
				Configuration c = main.getDatabase(type);
				main.getManager().stopGame(g);
				main.getManager().removeGame(g);
				c.set("Game." + id, null);
				c.save();
				p.sendMessage(Messages.PREFIX + " §aGame was removed succesfuly.");
			}
		} catch (NumberFormatException e) {
			p.sendMessage(Messages.PREFIX + " §cMust be a number!");
		}
	}

}