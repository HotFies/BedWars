package ro.Aneras.ClashWars.Handler.Party.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.Party.Party;
import ro.Aneras.ClashWars.Handler.Party.PartyManager;

public class PartyDeny implements CommandInterface {

	private Main main;

	public PartyDeny(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "deny";
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
		PartyManager manager = main.getPartyManager();
		if (manager.getInvitations().containsKey(p)) {
			Party party = manager.getInvitations().remove(p);
			p.sendMessage(Messages.PREFIX + Messages.PARTY_DENY.toString());
			party.getOwner().sendMessage(Messages.PREFIX + Messages.PARTY_DENIED.toString().replace("%player%", p.getName()));
		} else {
			p.sendMessage(Messages.PREFIX + Messages.PARTY_NO_INVITATION.toString());
		}
	}
	
}

