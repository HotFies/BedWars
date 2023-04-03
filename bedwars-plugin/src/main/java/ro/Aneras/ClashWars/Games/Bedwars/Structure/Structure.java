package ro.Aneras.ClashWars.Games.Bedwars.Structure;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import ro.Aneras.ClashWars.Games.Bedwars.BedWars;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;

public abstract class Structure {
	
	protected BedWars g;
	protected TeamColor color;
	protected List<Block> blocks;
	
	public Structure(BedWars g, TeamColor color) {
		this.g = g;
		this.color = color;
		this.blocks = new ArrayList<>();
	}
	
	public abstract void onStop();
	
	public boolean hasFinished() {
		return blocks.isEmpty();
	}
	
	public void run() {
		if (blocks.size() > 0) {
			Block b = blocks.remove(0);
			g.getRollback().add(b.getState());
			b.getWorld().playSound(b.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, 1f, 1f);
			b.setType(color.getRealMaterial());
			g.getPlaced().add(b);
		}
	}
	
}
