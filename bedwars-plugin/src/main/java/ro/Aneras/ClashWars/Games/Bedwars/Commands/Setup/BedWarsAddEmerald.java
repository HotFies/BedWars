package ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Bedwars.Setup.BedWarsSetup;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;

public class BedWarsAddEmerald implements CommandInterface {

	private Main main;

	public BedWarsAddEmerald(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "addemerald";
	}

	@Override
	public String[] getArguments() {
		return new String[0];
	}

	@Override
	public boolean hasPermission(Player p) {
		GameSetup setup = main.getSetups().get(p);
		return setup != null && setup.getType() == GameType.BEDWARS && setup.getStep() == 2 && setup.getMin() > 0;
	}

	@Override
	public void executeCommand(Player p, String[] args) {
		BedWarsSetup setup = (BedWarsSetup) main.getSetups().get(p);
		setup.getEmerald().add(p.getEyeLocation().clone());
		p.sendMessage(Messages.PREFIX+" §7Emerald Generator location added. §8(§d"+setup.getEmerald().size()+"/"+setup.getEmrNr()+"§8)");
		if (setup.getEmerald().size() == setup.getEmrNr()) {
			setup.nextStep();
			for (int i = 0; i < 20; i++) {
				p.sendMessage("");
			}
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
			p.sendMessage(Messages.PREFIX + " §7The emerald generators were set. Now set the");
			p.sendMessage(Messages.PREFIX + " §7spectator spawn by using §c/bw setspectator");
		}
	}

}
