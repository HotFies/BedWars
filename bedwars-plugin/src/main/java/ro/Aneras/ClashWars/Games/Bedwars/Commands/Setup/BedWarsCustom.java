package ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Bedwars.Setup.BedWarsSetup;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;

public class BedWarsCustom implements CommandInterface {

	private Main main;

	public BedWarsCustom(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "custom";
	}

	@Override
	public String[] getArguments() {
		return new String[] { "<team_size>", "<islands>", "<dmd_nr>", "<emr_nr>" };
	}

	@Override
	public boolean hasPermission(Player p) {
		GameSetup setup = main.getSetups().get(p);
		if (setup != null && setup.getType() == GameType.BEDWARS) {
			BedWarsSetup bsetup = (BedWarsSetup) setup;
			if (bsetup.getStep() == 0 && bsetup.getMin() > 0 && bsetup.isCustom()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void executeCommand(Player p, String[] args) {
		BedWarsSetup setup = (BedWarsSetup) main.getSetups().get(p);
		int size = Integer.parseInt(args[1]);
		int islands = Integer.parseInt(args[2]);
		if (islands > 8) {
			p.sendMessage("§cYou can't have more than 8 islands!");
			return;
		}
		int dmd_gens = Integer.parseInt(args[3]);
		int emr_gens = Integer.parseInt(args[4]);
		setup.isCustom(false);
		setup.setMax(size*islands);
		setup.setDmdNr(dmd_gens);
		setup.setEmrNr(emr_gens);
		setup.setIslands(islands);
		for (int i = 0; i < 20; i++) {
			p.sendMessage("");
		}
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
		p.sendMessage(Messages.PREFIX + " §7The custom data was set. Use §c/bw setlobby§7 to set where");
		p.sendMessage(Messages.PREFIX + " §7players wait for others to join.");
	}

}
