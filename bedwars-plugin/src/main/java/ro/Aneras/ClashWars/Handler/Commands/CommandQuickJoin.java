package ro.Aneras.ClashWars.Handler.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Party.Party;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;

public class CommandQuickJoin implements CommandInterface {

	private Main main;

	public CommandQuickJoin(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "quickjoin";
	}

	@Override
	public String[] getArguments() {
		return new String[] { "<GameType>" };
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return true;
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		if (main.getManager().getGame(p) != null) {
			return;
		}
		String name = args[1];
		GameType type = GameType.getEnum(name);
		if (type == null) {
			p.sendMessage(Messages.PREFIX + " §cThis game dosen't exists.");
			p.sendMessage(Messages.PREFIX + " §cBelow is the list of available games:");
			int i = 1;
			String list = Messages.PREFIX + " §2";
			for (GameType ex : GameType.values()) {
				list = list + ex.name() + (i==GameType.values().length ? "§c." : "§c,§2 ");
				if (i % 3 == 0 || i == GameType.values().length) {
					p.sendMessage(list);
					list = Messages.PREFIX + " §2";
				}
				i++;
			}
		} else {
			Party party = main.getPartyManager().getParty(p);
			if (party != null && party.getOwner() != p) {
				p.sendMessage(Messages.PREFIX + Messages.PARTY_OWNER_ONLY.toString());
			} else {
				Game found = main.getManager().findGame(type, p, -1);
				if (found == null) {
					p.sendMessage(Messages.PREFIX + " " + Messages.GAME_NO_GAME);
				} else {
					main.getManager().addPlayer(found, p);
				}
			}
		}
	}

}
