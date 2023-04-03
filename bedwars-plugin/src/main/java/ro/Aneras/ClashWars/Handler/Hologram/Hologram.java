package ro.Aneras.ClashWars.Handler.Hologram;

import java.util.HashMap;

import org.bukkit.Location;

public class Hologram {
	
	private Location base;
	private HashMap<Integer, HologramLine> entries;

	public Hologram(Location base) {
		entries = new HashMap<Integer, HologramLine>();
		this.base = base;
	}
	
	public void reset() {
		for (HologramLine line : entries.values()) {
			line.unregisterLine();
		}
		entries.clear();
	}
	
	public void removeLine(int line) {
		HologramLine l = entries.remove(line);
		if (l != null) {
			l.unregisterLine();
		}
	}
	
	public void updateLine(int line, String name) {
		if (entries.get(line) != null) {
			entries.get(line).update(name);
		} else {
			entries.put(line, new HologramLine(base.add(0, 0.28, 0), name));
		}
	}
	
}
