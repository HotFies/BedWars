package ro.Aneras.ClashWars.Handler.Statusbar;

import java.util.HashMap;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import ro.Aneras.ClashWars.Messages;

public class StatusbarSideBar {

	private Objective obj;
	private Statusbar board;
	private HashMap<Integer, StatusBarLine> entries = new HashMap<Integer, StatusBarLine>();
	
	public StatusbarSideBar(Statusbar board) {
		this.board = board;
		obj = board.getScoreboard().registerNewObjective("status", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(Messages.SCOREBOARD_TITLE.toString());
	}
	
	public Statusbar getBoard() {
		return board;
	}
	
	public Objective getObjective() {
		return obj;
	}
	
	public void reset() {
		for (StatusBarLine key : entries.values()) {
			key.unregister();
			board.getScoreboard().resetScores(key.getScore().getEntry());
		}
		entries.clear();
	}
	
	public void removeLine(int line) {
		StatusBarLine l = entries.remove(line);
		if (l != null) {
			l.unregister();
			l.unregisterLine();
		}
	}
	
	public void updateLine(int line, String name) {
		if (entries.get(line) != null) {
			entries.get(line).update(name);
		} else {
			entries.put(line, new StatusBarLine(board, name, line));
		}
	}
}
