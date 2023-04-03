package ro.Aneras.ClashWars.Handler.Statusbar;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

public class StatusBarLine {

	private Team team;
	private Score score;
	private String name;
	private Statusbar board;

	public StatusBarLine(Statusbar manager, String name, int line) {
		this.board = manager;
		String color = ChatColor.values()[line - 1] + "§r";
		team = manager.getScoreboard().registerNewTeam(color);
		score = manager.getSidebar().getObjective().getScore(color);
		score.setScore(line);
		team.addEntry(color);
		update(name);
	}

	public void unregister() {
		if (team != null) {
			team.unregister();
			team = null;
		}
	}

	public void unregisterLine() {
		board.getScoreboard().resetScores(score.getEntry());
		score = null;
	}
	
	public Score getScore() {
		return score;
	}

	public void update(String name) {
		if (!name.equals(this.name)) {
			this.name = name;
			String prefix = name.length() >= 16 ? name.substring(0, 16) : name;
			boolean colorMark = false;
			if (prefix.length() > 0 && prefix.charAt(prefix.length()-1) == '§') {
				prefix = prefix.substring(0, prefix.length() - 1);
				colorMark = true;
			}
			team.setPrefix(prefix);
			if (name.length() > 16) {
				String suffix = (colorMark ? "" : ChatColor.getLastColors(prefix));
				suffix = suffix.replace("§f", "");
				suffix = suffix + name.substring(prefix.length(), name.length());
				if (suffix.length() <= 16) {
					team.setSuffix(suffix);
				} else {
					team.setSuffix(suffix.substring(0, 16));
				}
			} else {
				team.setSuffix("");
			}
		}
	}
	
}
