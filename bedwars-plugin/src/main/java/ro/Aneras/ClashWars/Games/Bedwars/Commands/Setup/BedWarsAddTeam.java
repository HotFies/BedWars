package ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Bedwars.Setup.BedWarsTeamSetup;
import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;

public class BedWarsAddTeam implements CommandInterface {

	private Main main;

	public BedWarsAddTeam(Main main) {
		this.main = main;
	}
	
	@Override
	public String getCommand() {
		return "addteam";
	}

	@Override
	public String[] getArguments() {
		return new String[] { "<id>", "<team_color>" };
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return p.hasPermission("bw.admin");
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		try {
			int id = Integer.parseInt(args[1]);
			Game g = main.getManager().getGame(id, GameType.BEDWARS);
			TeamColor team = TeamColor.getEnum(args[2]);
			if (g == null) {
				p.sendMessage(Messages.PREFIX + " §cGame doesn't exists!");
			} else if (team == null) {
				p.sendMessage(Messages.PREFIX + " §cTeam dosen't exists.");
				p.sendMessage(Messages.PREFIX + " §cBelow is the list of available §cteams:");
				int i = 1;
				String list = Messages.PREFIX + " ";
				for (TeamColor ex : TeamColor.values()) {
					list = list + ex.getTeamColor() + "§l" + ex.name() + (i==TeamColor.values().length ? "§c." : "§c, ");
					i++;
				}
				p.sendMessage(list);
			} else if (g.getSettings().canJoin()) {
				p.sendMessage(Messages.PREFIX + " §cThis game has been finished already!");
			} else if (g.hasTeam(team)) {
				p.sendMessage(Messages.PREFIX + " §cThis team color is already taken!");
			} else {
				if (main.getSetups().get(p) == null) {
					main.getSetups().put(p, new BedWarsTeamSetup(g.getID(), g.getType(), team));
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
					p.sendMessage(Messages.PREFIX + " §7Welcome to BedWars wizard setup. Please follow the commands!");
					p.sendMessage(Messages.PREFIX + " §7Use §c/bw setspawn§7 to set spawn of where the team will spawn.");
				} else {
					p.sendMessage(Messages.PREFIX + " §aYou must finish your previous setup before starting a new one!");
				}
			}
		} catch (NumberFormatException e) {
			p.sendMessage(Messages.PREFIX + " §cMust be a number!");
		}
	}

}