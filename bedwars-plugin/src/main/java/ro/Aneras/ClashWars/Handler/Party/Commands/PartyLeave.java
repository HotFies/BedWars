package ro.Aneras.ClashWars.Handler.Party.Commands;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;
import ro.Aneras.ClashWars.Handler.Party.Party;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;

public class PartyLeave implements CommandInterface {

	private Main main;

	public PartyLeave(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "leave";
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
			if (party.getOwner() == p) {
				main.getPartyManager().getParty().remove(party);
				p.sendMessage(Messages.PREFIX + Messages.PARTY_DELETED.toString());
				Iterator<Entry<Player, Party>> it = main.getPartyManager().getInvitations().entrySet().iterator();
				while (it.hasNext()) {
					Entry<Player, Party> next = it.next();
					if (next.getValue() == party) {
						it.remove();
					}
				}
			} else {
				party.getMembers().remove(p);
				p.sendMessage(Messages.PREFIX + Messages.PARTY_LEFT.toString());
			}
		}
	}

}

