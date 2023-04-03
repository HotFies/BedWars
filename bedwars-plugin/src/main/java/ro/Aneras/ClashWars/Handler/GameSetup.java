package ro.Aneras.ClashWars.Handler;

import org.bukkit.Location;

import ro.Aneras.ClashWars.Handler.Tools.Selection;

public class GameSetup {

	private int id;
	private int min;
	private int max;
	private int step = 0;
	private GameType type;
	protected Selection selection;
	
	public GameSetup(int id, int min, int max, GameType type) {
		this.id = id;
		this.min = min;
		this.max = max;
		this.type = type;
	}
	
	public int getID() {
		return id;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public void nextStep() {
		step++;
	}
	
	public int getStep() {
		return step;
	}
	
	public GameType getType() {
		return type;
	}
	
	public Selection getSelection() {
		return selection;
	}
	
	public void setSelection(Location left, Location right) {
		
	}
	
}
