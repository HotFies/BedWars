package ro.Aneras.ClashWars.Handler.Statusbar;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import ro.Aneras.ClashWars.Games.Game;

public class StatusbarTeam {
	
	private Team spectator;
	private Scoreboard board;
	
	public StatusbarTeam(Scoreboard board) {
		spectator = board.registerNewTeam("999Spectator");
		spectator.setAllowFriendlyFire(false);
		spectator.setCanSeeFriendlyInvisibles(true);
		spectator.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
		spectator.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		spectator.setColor(ChatColor.GRAY);
		this.board = board;
	}
	
	public void add(Game g, String team, String prefix, ChatColor color) {
		Team t = board.registerNewTeam(team);
		t.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		t.setCanSeeFriendlyInvisibles(true);
		t.setPrefix(prefix);
		t.setColor(color);
	}
	
	public void addSpectator(String player, String team) {
		spectator.addEntry(player);
		Team t = board.getTeam(team);
		if (t != null) {
			t.removeEntry(player);
		}
	}
	
	public void removeSpectator(String player, String team) {
		spectator.removeEntry(player);
		Team t = board.getTeam(team);
		if (t != null) {
			t.addEntry(player);
		}
	}
	
	public void tagHider(boolean hide, String team, Player p) {
		Team t = board.getTeam(team);
		String name = p.getName();
		if (name.length() > 7) {
			name = name.substring(0, 7);
		}
		Team hteam = board.getTeam(team+name);
		if (hteam == null) {
			hteam = board.registerNewTeam(team+name);
		}
		if (t != null) {
			if (hide) {
				hteam.setPrefix(t.getPrefix());
				hteam.setColor(t.getColor());
				hteam.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
				hteam.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
				hteam.addEntry(p.getName());
				t.removeEntry(p.getName());
			} else {
				hteam.unregister();
				t.addEntry(p.getName());
			}
		}
	}
	
	public void addPlayer(String team, Player p) {
		Team t = board.getTeam(team);
		if (t != null) {
			t.addEntry(p.getName());
		}
	}
	
	public void removePlayer(String team, Player p) {
		Team t = board.getTeam(team);
		if (t != null) {
			t.removeEntry(p.getName());
		}
	}
}
