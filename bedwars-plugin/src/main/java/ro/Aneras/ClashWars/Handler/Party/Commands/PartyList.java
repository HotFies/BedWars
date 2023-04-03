package ro.Aneras.ClashWars.Handler.Party.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;
import ro.Aneras.ClashWars.Handler.Party.Party;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;

public class PartyList implements CommandInterface {

	private Main main;

	public PartyList(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "list";
	}

	@Override
	public String[] getArguments() {
		return new String[0];
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return true;
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		Party party = main.getPartyManager().getParty(p);
		if (party == null) {
			p.sendMessage(Messages.PREFIX + Messages.PARTY_NONE.toString());
		} else {
			int i = 1;
			String members = "";
			for (Player m : party.getMembers()) {
				members = members + "§b" + m.getName() + (party.getMembers().size() == i ? "§c." : "§c, ");
				i++;
			}
			for (String msg : Messages.PARTY_LIST.getList()) {
				p.sendMessage(msg.replace("%owner%", party.getOwner().getName()).replace("%members%", members));
			}
		}
	}

}

