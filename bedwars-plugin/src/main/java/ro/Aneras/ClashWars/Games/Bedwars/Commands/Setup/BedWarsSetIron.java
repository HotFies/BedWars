package ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Bedwars.Setup.BedWarsTeamSetup;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;

public class BedWarsSetIron implements CommandInterface {

	private Main main;

	public BedWarsSetIron(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "setiron";
	}

	@Override
	public String[] getArguments() {
		return new String[0];
	}

	@Override
	public boolean hasPermission(Player p) {
		GameSetup setup = main.getSetups().get(p);
		return setup != null && setup.getType() == GameType.BEDWARS && setup.getStep() == 1 && setup.getMin() == 0 && setup.getMax() == 0;
	}

	@Override
	public void executeCommand(Player p, String[] args) {
		BedWarsTeamSetup setup = (BedWarsTeamSetup) main.getSetups().get(p);
		setup.nextStep();
		setup.setIron(p.getLocation().clone());
		for (int i = 0; i < 20; i++) {
			p.sendMessage("");
		}
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
		p.sendMessage(Messages.PREFIX + " §7The iron generator was set. Now go to the shop location");
		p.sendMessage(Messages.PREFIX + " §7and type §c/bw setshop §7to add the team shop.");
	}

}
