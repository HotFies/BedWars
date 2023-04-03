package ro.Aneras.ClashWars.Handler.Party.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.Party.Party;

public class PartyAccept implements CommandInterface {

	private Main main;

	public PartyAccept(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "accept";
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
		Party party = main.getPartyManager().getInvitations().get(p);
		if (party == null) {
			p.sendMessage(Messages.PREFIX + Messages.PARTY_NO_INVITATION.toString());
		} else {
			party.getMembers().add(p);
			main.getPartyManager().getInvitations().remove(p);
			p.sendMessage(Messages.PREFIX + Messages.PARTY_ACCEPT.toString());
			party.getOwner().sendMessage(Messages.PREFIX + Messages.PARTY_ACCEPTED.toString().replace("%player%", p.getName()));
		}
	}

}

