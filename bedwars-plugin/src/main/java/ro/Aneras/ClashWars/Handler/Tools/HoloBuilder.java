package ro.Aneras.ClashWars.Handler.Tools;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class HoloBuilder {

	public static ArmorStand create(Location l, String message) {
		ArmorStand stand = l.getWorld().spawn(l, ArmorStand.class);
		stand.setCustomName(message);
		stand.setGravity(false);
		stand.setVisible(false);
		stand.setCustomNameVisible(true);
		return stand;
	}
	
}
