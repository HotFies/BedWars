package ro.Aneras.ClashWars.Games.Bedwars.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameState;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;

public class BedWarsStart implements CommandInterface {

	private Main main;

	public BedWarsStart(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "start";
	}

	@Override
	public String[] getArguments() {
		return new String[0];
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return p.hasPermission("bw.moderator") && p.hasPermission("bw.admin");
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		try {
			Game g = main.getManager().getGame(p);
			if (g == null) {
				p.sendMessage(Messages.PREFIX + " §cNo game found with this ID.");
			} else {
				if (g.getState() == GameState.WAITING) {
					if (g.getPlayers().size() >= 2) {
						g.setTimer(0);
						g.isGameStarted(true);
					} else {
						p.sendMessage(Messages.PREFIX + " §cThe game can't start without a minimum of 2 players!");
					}
				} else {
					p.sendMessage(Messages.PREFIX + " §cThe game is already started!");
				}
			}
		} catch (NumberFormatException e) {
			p.sendMessage(Messages.PREFIX + " §cMust be a number!");
		}
	}

}