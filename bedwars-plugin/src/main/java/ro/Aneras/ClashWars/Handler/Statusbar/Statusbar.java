package ro.Aneras.ClashWars.Handler.Statusbar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import ro.Aneras.ClashWars.Main;

public class Statusbar {

	private Player p;
	private Scoreboard board;
	private StatusbarTeam team;
	private StatusbarHealth health;
	private StatusbarSideBar sidebar;
	private Main main;
	
	public Statusbar(Main main, Player p) {
		this.p = p;
		this.main = main;
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		sidebar = new StatusbarSideBar(this);
		sidebar.getBoard().
		p.setScoreboard(board);
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public StatusbarTeam getTeam() {
		return team;
	}
	
	public Scoreboard getScoreboard() {
		return board;
	}
	
	public StatusbarHealth getHealth() {
		return health;
	}
	
	public StatusbarSideBar getSidebar() {
		return sidebar;
	}
	
	public void showHealth() {
		health = new StatusbarHealth(this);
	}
	
	public void showTeam() {
		team = new StatusbarTeam(board);
	}
	
	public void reset() {
		if (health != null) {
			for (Score score : health.getScores()) {
				board.resetScores(score.getEntry());
			}
			for (Objective obj : health.getHealth()) {
				obj.unregister();
			}
			health = null;
		}
		sidebar.reset();
		for (Team team : board.getTeams()) {
			team.unregister();
		}
		team = null;
		main.getVersion().removeFromShittyCollection(board);
	}
	
}
