package ro.Aneras.ClashWars.Handler.SQLStats;

import java.util.UUID;

public class SQLData {

	private UUID uuid;
	private String name;
	private int win;
	private int lose;
	private int score;
	private int playtime;
	private String type;
	private int kills;
	private int final_kills;
	private int deaths;
	private int final_deaths;
	private int beds_destroyed;
	private int xp;
	
	public SQLData(String type, UUID uuid, String name, int win, int lose, int kills, int final_kills, int deaths, int final_deaths, int beds_destroyed, int playtime, int xp) {
		this.type = type;
		this.uuid = uuid;
		this.name = name;
		this.win = win;
		this.lose = lose;
		this.kills = kills;
		this.final_kills = final_kills;
		this.deaths = deaths;
		this.final_deaths = final_deaths;
		this.beds_destroyed = beds_destroyed;
		this.playtime = playtime;
		this.setXp(xp);
	}
	
	public String getTypeName() {
		return type;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public int getWin() {
		return win;
	}
	
	public int getLose() {
		return lose;
	}
	
	public int getPlayTime() {
		return playtime;
	}
	
	public int getScore() {
		return score;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getFinalKills() {
		return final_kills;
	}

	public void setFinalKills(int final_kills) {
		this.final_kills = final_kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getFinalDeaths() {
		return final_deaths;
	}

	public void setFinalDeaths(int final_deaths) {
		this.final_deaths = final_deaths;
	}

	public int getBedsDestroyed() {
		return beds_destroyed;
	}

	public void setBedsDestroyed(int beds_destroyed) {
		this.beds_destroyed = beds_destroyed;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}
	
}
