package ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Bedwars.Setup.BedWarsSetup;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;

public class BedWarsAddDiamond implements CommandInterface {

	private Main main;

	public BedWarsAddDiamond(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "adddiamond";
	}

	@Override
	public String[] getArguments() {
		return new String[0];
	}

	@Override
	public boolean hasPermission(Player p) {
		GameSetup setup = main.getSetups().get(p);
		return setup != null && setup.getType() == GameType.BEDWARS && setup.getStep() == 1 && setup.getMin() > 0;
	}

	@Override
	public void executeCommand(Player p, String[] args) {
		BedWarsSetup setup = (BedWarsSetup) main.getSetups().get(p);
		setup.getDiamond().add(p.getEyeLocation().clone());
		p.sendMessage(Messages.PREFIX+" §7Diamond Generator location added. §8(§d"+setup.getDiamond().size()+"/"+setup.getDmdNr()+"§8)");
		if (setup.getDiamond().size() == setup.getDmdNr()) {
			setup.nextStep();
			for (int i = 0; i < 20; i++) {
				p.sendMessage("");
			}
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
			if (setup.getEmrNr() > 0) {
				p.sendMessage(Messages.PREFIX + " §7The diamond generators were set. Now stand on a §a§lEmerald Block");
				p.sendMessage(Messages.PREFIX + " §7and type §c/bw addemerald §7to add generator locations.");
			} else {
				setup.nextStep();
				setup.nextStep();
				p.sendMessage(Messages.PREFIX + " §7The diamond generators were set. Now set the spectator");
				p.sendMessage(Messages.PREFIX + " §7spawn by using §c/bw setspectator");
			}
		}
	}

}
