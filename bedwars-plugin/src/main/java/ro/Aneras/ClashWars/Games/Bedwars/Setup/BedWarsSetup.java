package ro.Aneras.ClashWars.Games.Bedwars.Setup;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import ro.Aneras.ClashWars.Handler.Tools.Selection;
import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameType;

public class BedWarsSetup extends GameSetup {
	
	private String name;
	private int mode = 0;
	private int dmd_nr = 0;
	private int emr_nr = 0;
	private int islands = 0;
	private boolean custom;
	private Location lobby;
	private Location first;
	private Location second;
	private Location spectator;
	private List<Location> emerald;
	private List<Location> diamond;
	
	public BedWarsSetup(String name, int id, int min, GameType type, int mode) {
		super(id, min, 0, type);
		this.mode = mode;
		this.name = name;
		if (mode == 5) {
			custom = true;
		} else {
			dmd_nr = 4;
			if (mode > 2) {
				emr_nr = 2;
			} else {
				emr_nr = 4;
			}
		}
		emerald = new ArrayList<Location>();
		diamond = new ArrayList<Location>();
	}
	
	public boolean isCustom() {
		return custom;
	}
	
	public void isCustom(boolean custom) {
		this.custom = custom;
	}
	
	public Location getLobby() {
		return lobby;
	}
	
	public Location getSpectator() {
		return spectator;
	}
	
	public int getMode() {
		return mode;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDmdNr() {
		return dmd_nr;
	}
	
	public int getEmrNr() {
		return emr_nr;
	}
	
	public int getIslands() {
		return islands;
	}
	
	public void setIslands(int islands) {
		this.islands = islands;
	}
	
	public void setEmrNr(int emr) {
		this.emr_nr = emr;
	}
	
	public void setDmdNr(int dmd) {
		this.dmd_nr = dmd;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}
	
	public void setSpectator(Location spectator) {
		this.spectator = spectator;
	}
	
	public List<Location> getDiamond() {
		return diamond;
	}
	
	public List<Location> getEmerald() {
		return emerald;
	}
	
	@Override
	public void setSelection(Location left, Location right) {
		if (left != null) {
			first = left;
		} else if (right != null) {
			second = right;
		}
		if (first == null || second == null) {
			return;
		}
		if (first.getWorld() == second.getWorld()) {
			selection = new Selection(first, second, true);
		}
	}
	
}
