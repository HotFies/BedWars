package ro.Aneras.ClashWars.Handler.Party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;
import ro.Aneras.ClashWars.Handler.Party.Commands.PartyAccept;
import ro.Aneras.ClashWars.Handler.Party.Commands.PartyDeny;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Handler.Commands.CommandExe;
import ro.Aneras.ClashWars.Handler.Party.Commands.PartyCreate;
import ro.Aneras.ClashWars.Handler.Party.Commands.PartyInvite;
import ro.Aneras.ClashWars.Handler.Party.Commands.PartyLeave;
import ro.Aneras.ClashWars.Handler.Party.Commands.PartyList;

public class PartyManager {
	
	private List<Party> partys = new ArrayList<Party>();
	private HashMap<Player, Party> invitations = new HashMap<Player, Party>();
	
	public PartyManager(Main main) {
		List<CommandInterface> party = new ArrayList<CommandInterface>();
		party.add(new PartyCreate(main));
		party.add(new PartyList(main));
		party.add(new PartyLeave(main));
		party.add(new PartyInvite(main));
		party.add(new PartyAccept(main));
		party.add(new PartyDeny(main));
		PluginCommand pluginCommand = main.getCommand("bwparty");
		if (pluginCommand != null) {
			pluginCommand.setExecutor(new CommandExe("Party", "bwparty", party));
		}
	}
	
	public List<Party> getParty() {
		return partys;
	}
	
	public HashMap<Player, Party> getInvitations() {
		return invitations;
	}
	
	public Party getParty(Player p) {
		for (Party party : partys) {
			if (party.getOwner() == p || party.getMembers().contains(p)) {
				return party;
			}
		}
		return null;
	}
	
	public boolean canJoin(Game g, Player p, Party party) {
		Player owner = party.getOwner();
		if (owner != p && g.getPlayers().contains(owner)) {
			return true;
		}
		return owner == p && (party.getSize() + g.getPlayers().size()) < g.getSettings().getMax();
	}
	
	public void removePlayer(Player p) {
		for (Party party : partys) {
			if (party.getOwner() == p) {
				partys.remove(party);
				break;
			}
			party.getMembers().remove(p);
		}
	}
	
}
