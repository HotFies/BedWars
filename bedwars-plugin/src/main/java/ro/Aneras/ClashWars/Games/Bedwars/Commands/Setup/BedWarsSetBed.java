package ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup;

import org.bukkit.Sound;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Bedwars.BedWars;
import ro.Aneras.ClashWars.Games.Bedwars.BedWarsIsland;
import ro.Aneras.ClashWars.Games.Bedwars.Setup.BedWarsTeamSetup;
import ro.Aneras.ClashWars.Handler.Configuration.Configuration;
import ro.Aneras.ClashWars.Handler.Tools.Serializer;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;

public class BedWarsSetBed implements CommandInterface {

	private Main main;

	public BedWarsSetBed(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "setbed";
	}

	@Override
	public String[] getArguments() {
		return new String[0];
	}

	@Override
	public boolean hasPermission(Player p) {
		GameSetup setup = main.getSetups().get(p);
		return setup != null && setup.getType() == GameType.BEDWARS && setup.getStep() == 4 && setup.getMin() == 0 && setup.getMax() == 0;
	}

	@Override
	public void executeCommand(Player p, String[] args) {
		BedWarsTeamSetup setup = (BedWarsTeamSetup) main.getSetups().get(p);
		if (!(p.getLocation().getBlock().getBlockData() instanceof Bed)) {
			p.sendMessage(Messages.PREFIX + " §cYou must stand on top of the bed!");
			return;
		}
		setup.setBed(p.getLocation().clone());
		Configuration database = main.getDatabase(GameType.BEDWARS);
		if (database == null) {
			p.sendMessage(Messages.PREFIX + " §cDatabase for some reason couldn't load");
			main.getSetups().remove(p);
			return;
		}
		BedWars bw = (BedWars)main.getManager().getGame(setup.getID(), GameType.BEDWARS);
		if (bw == null) {
			p.sendMessage(Messages.PREFIX + " §7The game doesn't exist anymore.");
			return;
		}
		database.set("Game." + setup.getID() + ".Teams."+setup.getTeam().name()+".Bed", Serializer.getSerializedLocation(setup.getBed(), false));
		database.set("Game." + setup.getID() + ".Teams."+setup.getTeam().name()+".Iron", Serializer.getSerializedLocation(setup.getIron(), false));
		database.set("Game." + setup.getID() + ".Teams."+setup.getTeam().name()+".Shop", Serializer.getSerializedLocation(setup.getShop(), false));
		database.set("Game." + setup.getID() + ".Teams."+setup.getTeam().name()+".Upgrade", Serializer.getSerializedLocation(setup.getUpgrade(), false));
		database.set("Game." + setup.getID() + ".Teams."+setup.getTeam().name()+".Spawn", Serializer.getSerializedLocation(setup.getSpawn(), true));
		database.save();
		bw.getManager().addTeam(new BedWarsIsland(setup.getTeam(), setup.getSpawn(), setup.getShop(), setup.getUpgrade(), setup.getIron(), setup.getBed()));
		for (int i = 0; i < 20; i++) {
			p.sendMessage("");
		}
		main.getSetups().remove(p);
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
		int max = bw.getIsland();
		p.sendMessage(Messages.PREFIX + " §aThe team has been successfully set. (§c"+bw.getIslands().size()+"/"+max+"§a)");
		if (bw.getSettings().canJoin()) {
			p.sendMessage(Messages.PREFIX + " §7The game is now fully loaded!");
			p.sendMessage(Messages.PREFIX + " §7You can use §c/bw join " + setup.getID());
			p.sendMessage(Messages.PREFIX + " §7Or make a sign with the text:");
			p.sendMessage(Messages.PREFIX + " §7[" + setup.getType().getName()+']');
			p.sendMessage(Messages.PREFIX + " §7  " + setup.getID());
		}
	}

}
