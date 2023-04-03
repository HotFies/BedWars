package ro.Aneras.ClashWars.Games;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Handler.Cache.TeamColor;

public class GameTeam {
	
	private TeamColor color;
	private List<Player> team = null;
	
	public GameTeam(TeamColor color) {
		this.team = new ArrayList<Player>();
		this.color = color;
	}
	
	public List<Player> getTeam() {
		return team;
	}
	
	public TeamColor getColor() {
		return color;
	}
	
	public int size() {
		return team.size();
	}
	
}
