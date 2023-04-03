package ro.Aneras.ClashWars.Handler.Tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;


public class Selection {

	private Location min;
	private Location max;
	private Location middle;
	private List<BlockState> blocks;

	public Selection(Location first, Location second, boolean full) {
		blocks = new ArrayList<BlockState>();
		int minX = Math.min(first.getBlockX(), second.getBlockX());
		int minY = Math.min(first.getBlockY(), second.getBlockY());
		int minZ = Math.min(first.getBlockZ(), second.getBlockZ());
		int maxX = Math.max(first.getBlockX(), second.getBlockX());
		int maxY = Math.max(first.getBlockY(), second.getBlockY());
		int maxZ = Math.max(first.getBlockZ(), second.getBlockZ());
		min = new Location(first.getWorld(), minX, minY, minZ);
		max = new Location(first.getWorld(), maxX, maxY, maxZ);
		for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					Block b = min.getWorld().getBlockAt(x, y, z);
					if (b != null && (full || b.getType() != Material.AIR)) {
						blocks.add(b.getState());
					}
				}
			}
		}
		middle = new Location(min.getWorld(), (minX + maxX)/2, (minY + maxY)/2, (minZ + maxZ)/2);
	}

	public Location getMin() {
		return min;
	}

	public Location getMax() {
		return max;
	}
	
	public Location getMiddle() {
		return middle;
	}
	
	public List<BlockState> getBlocks() {
		return blocks;
	}
	
	public boolean contains(Location l) {
		if (l.getWorld() == min.getWorld()) {
			return l.getBlockX() >= min.getBlockX() && l.getBlockX() < max.getBlockX() + 1
				&& l.getBlockY() >= min.getBlockY() && l.getBlockY() < max.getBlockY() + 1
				&& l.getBlockZ() >= min.getBlockZ() && l.getBlockZ() < max.getBlockZ() + 1;
		} else {
			return false;
		}
	}

}
