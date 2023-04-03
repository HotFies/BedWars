package ro.Aneras.ClashWars.Handler.Party.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;
import ro.Aneras.ClashWars.Handler.Party.Party;
import ro.Aneras.ClashWars.Handler.Party.PartyManager;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;

public class PartyInvite implements CommandInterface {

	private Main main;

	public PartyInvite(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "invite";
	}

	@Override
	public String[] getArguments() {
		return new String[] { "<player>" };
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return true;
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		PartyManager manager = main.getPartyManager();
		Party party = manager.getParty(p);
		if (party == null) {
			p.sendMessage(Messages.PREFIX + Messages.PARTY_NONE.toString());
		} else {
			Player inv = main.getServer().getPlayer(args[1]);
			if (inv == null) {
				p.sendMessage(Messages.PREFIX + Messages.PARTY_ONLINE.toString());
			} else if (party.getMax() <= party.getMembers().size()) {
				p.sendMessage(Messages.PREFIX + Messages.PARTY_MAX.toString());
			} else if (party.getOwner() != p) {
				p.sendMessage(Messages.PREFIX + Messages.PARTY_OWNER.toString());
			} else if (manager.getParty(inv) == null) {
				if (manager.getInvitations().get(p) == null) {
					manager.getInvitations().put(inv, party);
					inv.sendMessage(Messages.PREFIX + Messages.PARTY_INVITED.toString().replace("%player%", p.getName()));
					p.sendMessage(Messages.PREFIX + Messages.PARTY_SENT.toString().replace("%player%", inv.getName()));
				} else {
					p.sendMessage(Messages.PREFIX + Messages.PARTY_ALREADY_INVITED.toString().replace("%player%", inv.getName()));
				}
			} else {
				p.sendMessage(Messages.PREFIX + Messages.PARTY_ALREADY_INVITE.toString().replace("%player%", inv.getName()));
			}
		}
	}

}

