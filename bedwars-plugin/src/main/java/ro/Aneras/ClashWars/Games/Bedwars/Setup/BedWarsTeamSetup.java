package ro.Aneras.ClashWars.Games.Bedwars.Setup;

import org.bukkit.Location;

import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;

public class BedWarsTeamSetup extends GameSetup {
	
	private Location bed;
	private TeamColor team;
	private Location iron;
	private Location shop;
	private Location upgrade;
	private Location spawn;
	
	public BedWarsTeamSetup(int id, GameType type, TeamColor team) {
		super(id, 0, 0, type);
		this.team = team;
	}
	
	public Location getBed() {
		return bed;
	}
	
	public TeamColor getTeam() {
		return team;
	}
	
	public Location getIron() {
		return iron;
	}
	
	public Location getShop() {
		return shop;
	}
	
	public Location getUpgrade() {
		return upgrade;
	}
	
	public Location getSpawn() {
		return spawn;
	}
	
	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}
	
	public void setBed(Location bed) {
		this.bed = bed;
	}
	
	public void setIron(Location iron) {
		this.iron = iron;
	}
	
	public void setShop(Location shop) {
		this.shop = shop;
	}
	
	public void setUpgrade(Location upgrade) {
		this.upgrade = upgrade;
	}
	
}
