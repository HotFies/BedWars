package ro.Aneras.ClashWars.Handler.Hologram;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class HologramLine {

	private ArmorStand stand;
	
	public HologramLine(Location l, String name) {
		stand = l.getWorld().spawn(l, ArmorStand.class);
		stand.setGravity(false);
		stand.setVisible(false);
		stand.setCustomName(name);
		stand.setCustomNameVisible(true);
	}
	
	public void unregisterLine() {
		stand.remove();
	}
	
	public void update(String name) {
		if (!name.equals(stand.getCustomName())) {
			stand.setCustomName(name);
		}
	}
	
}
