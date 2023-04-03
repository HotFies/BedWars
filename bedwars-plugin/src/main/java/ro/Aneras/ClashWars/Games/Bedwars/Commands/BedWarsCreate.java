package ro.Aneras.ClashWars.Games.Bedwars.Commands;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Games.Bedwars.Setup.BedWarsSetup;
import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;

public class BedWarsCreate implements CommandInterface {

	private Main main;

	public BedWarsCreate(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "create";
	}

	@Override
	public String[] getArguments() {
		return new String[] { "<id>", "<name>", "<min_players>", "<game_mode>" };
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return p.hasPermission("bw.admin");
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		try {
			int id = Integer.parseInt(args[1]);
			String name = args[2];
			int min = Integer.parseInt(args[3]);
			String mode = args[4];
			for (GameSetup setup : main.getSetups().values()) {
				if (setup.getID() == id && setup.getType() == GameType.BEDWARS) {
					p.sendMessage(Messages.PREFIX + " §cA setup with the same ID is already being made.");
					return;
				}
			}
			int x = 0;
			if (mode.equalsIgnoreCase("solo")) {
				x = 1;
			} else if (mode.equalsIgnoreCase("doubles")) {
				x = 2;
			} else if (mode.equalsIgnoreCase("3v3v3v3")) {
				x = 3;
			} else if (mode.equalsIgnoreCase("4v4v4v4")) {
				x = 4;
			} else if (mode.equalsIgnoreCase("custom")) {
				x = 5;
			}
			if (x == 0) {
				p.sendMessage(Messages.PREFIX + " §cInvalid gamemode, valid gamemodes are:");
				p.sendMessage(Messages.PREFIX + " §6solo§e,§6 doubles§e,§6 3v3v3v3§e,§6 4v4v4v4§e,§6 custom");
				return;
			}
			if (main.getManager().getGame(id, GameType.BEDWARS) == null) {
				if (main.getSetups().get(p) == null) {
					main.getSetups().put(p, new BedWarsSetup(name, id, min, GameType.BEDWARS, x));
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
					for (int i = 0; i < 20; i++) {
						p.sendMessage("");
					}
					p.sendMessage(Messages.PREFIX + " §7Welcome to BedWars wizard setup. Please follow the commands!");
					if (x == 5) {
						p.sendMessage(Messages.PREFIX + " §7Use §c/bw custom <team_size> <islands> <dmd_nr> <emr_nr>§7.");
					} else {
						p.sendMessage(Messages.PREFIX + " §7Use §c/bw setlobby§7 to set where players wait for others to join.");
					}
				} else {
					p.sendMessage(Messages.PREFIX + " §cYou must finish your previous setup first.");
				}
			} else {
				p.sendMessage(Messages.PREFIX + " §cA game with the same ID already exists.");
			}
		} catch (NumberFormatException e) {
			p.sendMessage(Messages.PREFIX + " §cMust be a number!");
		}
	}

}