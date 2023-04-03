package ro.Aneras.ClashWars.Handler.Party.Commands;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;
import ro.Aneras.ClashWars.Handler.Party.Party;
import ro.Aneras.ClashWars.Handler.Party.PartyManager;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;

public class PartyCreate implements CommandInterface {

	private Main main;

	public PartyCreate(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "create";
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
		if (manager.getParty(p) == null) {
			manager.getParty().add(new Party(p, 3));
			manager.getInvitations().remove(p);
			p.sendMessage(Messages.PREFIX + Messages.PARTY_CREATED.toString());
		} else {
			p.sendMessage(Messages.PREFIX + Messages.PARTY_ALREADY.toString());
		}
	}

}

