package ro.Aneras.ClashWars.Games.Bedwars.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;
import ro.Aneras.ClashWars.Handler.Party.Party;

public class BedWarsAutoJoin implements CommandInterface {

	private Main main;

	public BedWarsAutoJoin(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "autojoin";
	}

	@Override
	public String[] getArguments() {
		return new String[] { "<teams>", "<team_max>" };
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return true;
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		int teams, teamMax;
		try {
			teams = Integer.valueOf(args[1]);
			teamMax = Integer.valueOf(args[2]);
		} catch (NumberFormatException e) {
			p.sendMessage("Must be a number!");
			return;
		}
		Party party = main.getPartyManager().getParty(p);
		if (party != null && party.getOwner() != p) {
			p.sendMessage(Messages.PREFIX + Messages.PARTY_OWNER_ONLY.toString());
		} else {
			Game found = main.getManager().findGame(GameType.BEDWARS, p, teams, teamMax);
			if (found == null) {
				p.sendMessage(Messages.PREFIX + " " + Messages.GAME_NO_GAME);
			} else {
				main.getManager().addPlayer(found, p);
			}
		}
	}

}
