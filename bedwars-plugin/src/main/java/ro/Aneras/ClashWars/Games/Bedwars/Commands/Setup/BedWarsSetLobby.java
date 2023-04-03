package ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Bedwars.Setup.BedWarsSetup;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;

public class BedWarsSetLobby implements CommandInterface {

	private Main main;

	public BedWarsSetLobby(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "setlobby";
	}

	@Override
	public String[] getArguments() {
		return new String[0];
	}

	@Override
	public boolean hasPermission(Player p) {
		GameSetup setup = main.getSetups().get(p);
		if (setup != null && setup.getType() == GameType.BEDWARS) {
			BedWarsSetup bsetup = (BedWarsSetup) setup;
			if (bsetup.getStep() == 0 && bsetup.getMin() > 0 && !bsetup.isCustom()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void executeCommand(Player p, String[] args) {
		BedWarsSetup setup = (BedWarsSetup) main.getSetups().get(p);
		setup.nextStep();
		setup.setLobby(p.getLocation().clone());
		for (int i = 0; i < 20; i++) {
			p.sendMessage("");
		}
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
		if (setup.getDmdNr() > 0) {
			p.sendMessage(Messages.PREFIX + " §7The lobby was set. Now stand on a §b§lDiamond Block");
			p.sendMessage(Messages.PREFIX + " §7and type §c/bw adddiamond §7to add generator locations.");
		} else if (setup.getEmrNr() > 0) {
			setup.nextStep();
			p.sendMessage(Messages.PREFIX + " §7The lobby was set. Now stand on a §a§lEmerald Block");
			p.sendMessage(Messages.PREFIX + " §7and type §c/bw addemerald §7to add generator locations.");
		} else {
			setup.nextStep();
			setup.nextStep();
			p.sendMessage(Messages.PREFIX + " §7The lobby was set. Now set the spectator");
			p.sendMessage(Messages.PREFIX + " §7spawn by using §c/bw setspectator");
		}
	}

}
