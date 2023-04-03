package ro.Aneras.ClashWars.Handler.Statusbar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class StatusbarHealth {

	private Objective[] health = new Objective[2];
	private List<Score> scores = new ArrayList<Score>();
	
	public StatusbarHealth(Statusbar bar) {
		health[0] = bar.getScoreboard().registerNewObjective("health", "dummy");
		health[0].setDisplaySlot(DisplaySlot.BELOW_NAME);
		health[0].setDisplayName("§c❤");
		health[1] = bar.getScoreboard().registerNewObjective("list_health", "dummy");
		health[1].setDisplaySlot(DisplaySlot.PLAYER_LIST);
	}
	
	public List<Score> getScores() {
		return scores;
	}
	
	public Objective[] getHealth() {
		return health;
	}
	
	public void update(Player p, double dmg) {
		for (Objective obj : health) {
			obj.getScore(p.getName()).setScore((int) dmg);
		}
	}
	
}
