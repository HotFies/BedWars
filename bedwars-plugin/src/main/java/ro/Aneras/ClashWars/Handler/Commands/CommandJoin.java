package ro.Aneras.ClashWars.Handler.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Handler.GameType;

public class CommandJoin implements CommandInterface {

	private Main main;
	private GameType type;

	public CommandJoin(Main main, GameType type) {
		this.main = main;
		this.type = type;
	}
	
	@Override
	public String getCommand() {
		return "join";
	}

	@Override
	public String[] getArguments() {
		return new String[] { "<id>" };
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return true;
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		try {
			int id = Integer.parseInt(args[1]);
			Game g = main.getManager().getGame(id, type);
			if (g != null) {
				main.getManager().addPlayer(g, p);
			} else {
				p.sendMessage(Messages.GAME_NO_GAME.toString());
			}
		} catch (NumberFormatException e) {
			p.sendMessage(Messages.PREFIX + "§c Must be a number!");
		}
	}

}
