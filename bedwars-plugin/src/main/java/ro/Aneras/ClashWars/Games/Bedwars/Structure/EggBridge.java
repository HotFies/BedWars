package ro.Aneras.ClashWars.Games.Bedwars.Structure;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Bedwars.BedWars;
import ro.Aneras.ClashWars.Handler.Tools.DataTable;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;

public class EggBridge extends Structure {

	private Egg egg;
	private int lived = 30;
	private BlockFace[] faces;
	
	public EggBridge(BedWars g, Player p, TeamColor color) {
		super(g, color);
		faces = new BlockFace[2];
		egg = p.launchProjectile(Egg.class);
		egg.teleport(egg.getLocation().subtract(0, 1, 0));
		faces[0] = DataTable.getCardinalDirection(p.getLocation().getYaw());
		faces[1] = DataTable.getNextDirection(faces[0]);
	}
	
	public void onStop() {
		if (!egg.isDead()) {
			egg.remove();
		}
	}
	
	public boolean hasFinished() {
		return lived <= 0 && blocks.isEmpty();
	}
	
	public void run() {
		if (egg.isDead()) {
			lived = 0;
		}
		if (lived > 0) {
			Block b = egg.getLocation().getBlock();
			if (!blocks.contains(b) && b.getType() == Material.AIR && g.getManager().canPlace(b)) {
				blocks.add(b);
				Block relative = b.getRelative(faces[1]);
				if (!blocks.contains(relative) && relative.getType() == Material.AIR && g.getManager().canPlace(relative)) {
					blocks.add(relative);
				}
			}
			Block relative = null;
			for (BlockFace face : faces) {
				if (relative == null) {
					relative = b.getRelative(face);
				} else {
					relative = relative.getRelative(face);
				}
				if (!blocks.contains(relative) && relative.getType() == Material.AIR && g.getManager().canPlace(b)) {
					blocks.add(relative);
				}
			}
			lived--;
		} else if (!egg.isDead()) {
			egg.remove();
		}
		if (lived <= 26) {
			super.run();
		}
	}
	
}
