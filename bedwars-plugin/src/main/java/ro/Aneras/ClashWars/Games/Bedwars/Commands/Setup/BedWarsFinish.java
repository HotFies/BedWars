package ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Bedwars.BedWars;
import ro.Aneras.ClashWars.Games.Bedwars.Setup.BedWarsSetup;
import ro.Aneras.ClashWars.Handler.Configuration.Configuration;
import ro.Aneras.ClashWars.Handler.Tools.Serializer;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;

public class BedWarsFinish implements CommandInterface {

	private Main main;

	public BedWarsFinish(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "finish";
	}

	@Override
	public String[] getArguments() {
		return new String[0];
	}

	@Override
	public boolean hasPermission(Player p) {
		GameSetup setup = main.getSetups().get(p);
		return setup != null && setup.getType() == GameType.BEDWARS && (setup.getStep() == 4 && setup.getMin() > 0 || setup.getStep() == 5 && setup.getMin() == 0);
	}

	@Override
	public void executeCommand(Player p, String[] args) {
		GameSetup gsetup = main.getSetups().get(p);
		Configuration database = main.getDatabase(GameType.BEDWARS);
		if (database == null) {
			p.sendMessage(Messages.PREFIX + " §cThe Database for some reason couldn't load");
			main.getSetups().remove(p);
			return;
		}
		if (gsetup.getStep() == 4) {
			BedWarsSetup setup = (BedWarsSetup) gsetup;
			database.set("Game." + setup.getID() + ".Min", setup.getMin());
			if (setup.getMode() == 5) {
				database.set("Game." + setup.getID() + ".Max", setup.getMax());
				database.set("Game." + setup.getID() + ".Islands", setup.getIslands());
			}
			database.set("Game." + setup.getID() + ".Mode", setup.getMode());
			database.set("Game." + setup.getID() + ".Name", setup.getName());
			database.set("Game." + setup.getID() + ".Lobby", Serializer.getSerializedLocation(setup.getLobby(), false));
			database.set("Game." + setup.getID() + ".Spectator", Serializer.getSerializedLocation(setup.getSpectator(), false));
			database.set("Game." + setup.getID() + ".Diamond", Serializer.getSerializedLocations(setup.getDiamond(), true));
			database.set("Game." + setup.getID() + ".Emerald", Serializer.getSerializedLocations(setup.getEmerald(), true));
			database.save();
			BedWars bw = new BedWars(main, setup.getName(), setup.getID(), setup.getMin(), setup.getMode(), setup.getLobby(), setup.getDiamond(), setup.getEmerald(), setup.getSpectator());
			if (setup.getMode() == 5) {
				bw.getSettings().setMax(setup.getMax());
				bw.setIsland(setup.getIslands());
			}
			main.getManager().getGames().add(bw);
			for (int i = 0; i < 20; i++) {
				p.sendMessage("");
			}
			main.getSetups().remove(p);
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
			p.sendMessage(Messages.PREFIX + " §7The main game setup has finished.");
			p.sendMessage(Messages.PREFIX + " §7Please use §c/bw addteam <id> <game_color>");
			p.sendMessage(Messages.PREFIX + " §7to add teams to the game.");
		}
	}

}
