package ro.Aneras.ClashWars.Games.Bedwars.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;
import ro.Aneras.ClashWars.Handler.Party.Party;

public class BedWarsQuickJoin implements CommandInterface {

	private Main main;

	public BedWarsQuickJoin(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "quickjoin";
	}

	@Override
	public String[] getArguments() {
		return new String[] { "<mode>" };
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return true;
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		int players = 0;
		String mode = args[1];
		int x = 0;
		if (mode.equalsIgnoreCase("solo")) {
			x = 1;
		} else if (mode.equalsIgnoreCase("doubles")) {
			x = 2;
		} else if (mode.equalsIgnoreCase("3v3v3v3")) {
			x = 3;
		} else if (mode.equalsIgnoreCase("4v4v4v4")) {
			x = 4;
		} else if (mode.equalsIgnoreCase("custom")) {
			x = 5;
		}
		if (x == 0) {
			p.sendMessage(Messages.PREFIX + " §cInvalid gamemode, valid gamemodes are:");
			p.sendMessage(Messages.PREFIX + " §6solo§e,§6 doubles§e,§6 3v3v3v3§e,§6 4v4v4v4§e,§6 custom");
			return;
		}
		Party party = main.getPartyManager().getParty(p);
		if (party != null && party.getOwner() != p) {
			p.sendMessage(Messages.PREFIX + Messages.PARTY_OWNER_ONLY.toString());
		} else {
			Game found = main.getManager().findGame(GameType.BEDWARS, p, x);
			if (found == null) {
				p.sendMessage(Messages.PREFIX + " " + Messages.GAME_NO_GAME);
			} else {
				main.getManager().addPlayer(found, p);
			}
		}
	}

}
