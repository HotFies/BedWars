package ro.Aneras.ClashWars.Games.Bedwars.Cache;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Bedwars.BedWars;
import ro.Aneras.ClashWars.Handler.Tools.DataTable;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;

public class EggBridge {

	private Egg egg;
	private BedWars g;
	private int lived = 30;
	private TeamColor color;
	private BlockFace[] faces;
	private List<Block> bridge;
	
	public EggBridge(BedWars g, Player p, TeamColor color) {
		this.g = g;
		this.color = color;
		faces = new BlockFace[2];
		this.bridge = new ArrayList<Block>();
		egg = p.launchProjectile(Egg.class);
		egg.teleport(egg.getLocation().subtract(0, 1, 0));
		faces[0] = DataTable.getCardinalDirection(p.getLocation().getYaw());
		faces[1] = DataTable.getNextDirection(faces[0]);
	}
	
	public Egg getEgg() {
		return egg;
	}
	
	public void onStop() {
		if (egg != null) {
			egg.remove();
			egg = null;
		}
		for (Block b : bridge) {
			b.setType(Material.AIR);
		}
	}
	
	public void onHit() {
		if (egg != null) {
			egg.remove();
			egg = null;
			lived = 0;
		}
	}
	
	public boolean hasFinished() {
		return lived <= 0 && bridge.isEmpty();
	}
	
	public void run() {
		if (lived > 0) {
			Block b = egg.getLocation().getBlock();
			if (!bridge.contains(b) && b.getType() == Material.AIR && g.getManager().canPlace(b)) {
				bridge.add(b);
				Block relative = b.getRelative(faces[1]);
				if (!bridge.contains(relative) && relative.getType() == Material.AIR
						&& g.getManager().canPlace(relative)) {
					bridge.add(relative);
				}
			}
			Block relative = null;
			for (BlockFace face : faces) {
				if (relative == null) {
					relative = b.getRelative(face);
				} else {
					relative = relative.getRelative(face);
				}
				if (!bridge.contains(relative) && relative.getType() == Material.AIR && g.getManager().canPlace(b)) {
					bridge.add(relative);
				}
			}
			lived--;
		} else if (egg != null) {
			egg.remove();
			egg = null;
		}
		if (lived <= 26 && bridge.size() > 0) {
			Block b = bridge.remove(0);
			g.getRollback().add(b.getState());
			b.getWorld().playSound(b.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, 1f, 1f);
			b.setType(color.getRealMaterial());
			g.getPlaced().add(b);
		}
	}
	
}
