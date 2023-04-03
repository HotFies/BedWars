package ro.Aneras.ClashWars.Handler.Tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Serializer {
	
	public static String timeConverter(int seconds) {
		return ((seconds % 3600 / 60 < 10) ? "0" : "") + seconds % 3600 / 60 + ":" + ((seconds % 3600 % 60 < 10) ? "0" : "") + seconds % 3600 % 60;
	}
	
	public static List<Location> getDeserializedLocations(List<String> list) {
		List<Location> loclist = new ArrayList<Location>();
		for (String l : list) {
			loclist.add(getDeserializedLocation(l));
		}
		return loclist;
	}
	
	public static int multiplyEach(int base, int times) {
		for (int i = 0; i < times; i++) {
			base = base*2;
		}
		return base;
	}
	
	public static List<String> getSerializedLocations(List<Location> list, boolean blocky) {
		List<String> loclist = new ArrayList<String>();
		for (Location l : list) {
			loclist.add(getSerializedLocation(l, blocky));
		}
		return loclist;
	}
	
	public static String getSerializedLocation(Location l, boolean blocky) {
		if (blocky) {
			return l.getWorld().getName() + "," + (l.getBlockX()+0.5) + "," + (l.getBlockY()+0.5) + "," + (l.getBlockZ()+0.5) + "," + l.getYaw() + "," + l.getPitch();
		} else {
			return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ() + "," + l.getYaw() + "," + l.getPitch();
		}
	}

	public static Location getDeserializedLocation(String s) {
		String[] st = s.split(",");
		World world = Bukkit.getWorld(st[0]);
		if (world == null) {
			throw new SerializerException("The world doesn't exists!");
		}
		return new Location(world, Double.parseDouble(st[1]), Double.parseDouble(st[2]), Double.parseDouble(st[3]), Float.parseFloat(st[4]), Float.parseFloat(st[5]));
	}
	
	public static class SerializerException extends RuntimeException {
		
		private static final long serialVersionUID = 3869927859934056992L;

		public SerializerException(String message) {
			super(message);
		}
		
	}
	
}
