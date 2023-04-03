package ro.Aneras.ClashWars.Games.Bedwars.Structure;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;

import ro.Aneras.ClashWars.Games.Bedwars.BedWars;
import ro.Aneras.ClashWars.Handler.Tools.DataTable;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;

public class PopupTower extends Structure {
	
	private Block chest;
	private BlockFace face;
	
	public PopupTower(BedWars g, Block chest, TeamColor color) {
		super(g, color);
		
		face = ((Directional) (this.chest = chest).getBlockData()).getFacing();
		
		//Walls
		for (int i = 0; i <= 6; i++) {
			placeBlock(chest.getRelative(1, i, 2));
			if (face != BlockFace.SOUTH || i >= 2) {
				placeBlock(chest.getRelative(0, i, 2));
			}
			placeBlock(chest.getRelative(-1, i, 2));

			placeBlock(chest.getRelative(2, i, 1));
			if (face != BlockFace.EAST || i >= 2) {
				placeBlock(chest.getRelative(2, i, 0));
			}
			placeBlock(chest.getRelative(2, i, -1));

			placeBlock(chest.getRelative(1, i, -2));
			if (face != BlockFace.NORTH || i >= 2) {
				placeBlock(chest.getRelative(0, i, -2));
			}
			placeBlock(chest.getRelative(-1, i, -2));

			placeBlock(chest.getRelative(-2, i, 1));
			if (face != BlockFace.WEST || i >= 2) {
				placeBlock(chest.getRelative(-2, i, 0));
			}
			placeBlock(chest.getRelative(-2, i, -1));
		}
		//External holes
		placeBlock(chest.getRelative(2, 6, 2));
		placeBlock(chest.getRelative(-2, 6, 2));
		placeBlock(chest.getRelative(2, 6, -2));
		placeBlock(chest.getRelative(-2, 6, -2));
		
		//Internal holes
		placeBlock(chest.getRelative(0, 6, 0));
		placeBlock(chest.getRelative(1, 6, -1));
		placeBlock(chest.getRelative(1, 6, 0));
		placeBlock(chest.getRelative(1, 6, 1));
		placeBlock(chest.getRelative(-1, 6, -1));
		placeBlock(chest.getRelative(-1, 6, 0));
		placeBlock(chest.getRelative(-1, 6, 1));
		placeBlock(chest.getRelative(-1, 6, 1));
		placeBlock(chest.getRelative(0, 6, 1));
		placeBlock(chest.getRelative(1, 6, 1));
		placeBlock(chest.getRelative(-1, 6, -1));
		placeBlock(chest.getRelative(0, 6, -1));
		placeBlock(chest.getRelative(1, 6, -1));
		
		//ZigZag
		
		placeBlock(chest.getRelative(3, 6, 0));
		placeBlock(chest.getRelative(3, 7, 0));
		placeBlock(chest.getRelative(3, 8, 0));
		placeBlock(chest.getRelative(3, 7, 1));
		placeBlock(chest.getRelative(3, 7, -1));
		placeBlock(chest.getRelative(3, 6, 2));
		placeBlock(chest.getRelative(3, 7, 2));
		placeBlock(chest.getRelative(3, 8, 2));
		placeBlock(chest.getRelative(3, 6, -2));
		placeBlock(chest.getRelative(3, 7, -2));
		placeBlock(chest.getRelative(3, 8, -2));
		
		placeBlock(chest.getRelative(-3, 6, 0));
		placeBlock(chest.getRelative(-3, 7, 0));
		placeBlock(chest.getRelative(-3, 8, 0));
		placeBlock(chest.getRelative(-3, 7, 1));
		placeBlock(chest.getRelative(-3, 7, -1));
		placeBlock(chest.getRelative(-3, 6, 2));
		placeBlock(chest.getRelative(-3, 7, 2));
		placeBlock(chest.getRelative(-3, 8, 2));
		placeBlock(chest.getRelative(-3, 6, -2));
		placeBlock(chest.getRelative(-3, 7, -2));
		placeBlock(chest.getRelative(-3, 8, -2));
		
		placeBlock(chest.getRelative(0, 6, 3));
		placeBlock(chest.getRelative(0, 7, 3));
		placeBlock(chest.getRelative(0, 8, 3));
		placeBlock(chest.getRelative(1, 7, 3));
		placeBlock(chest.getRelative(-1, 7, 3));
		placeBlock(chest.getRelative(2, 6, 3));
		placeBlock(chest.getRelative(2, 7, 3));
		placeBlock(chest.getRelative(2, 8, 3));
		placeBlock(chest.getRelative(-2, 6, 3));
		placeBlock(chest.getRelative(-2, 7, 3));
		placeBlock(chest.getRelative(-2, 8, 3));
		
		placeBlock(chest.getRelative(0, 6, -3));
		placeBlock(chest.getRelative(0, 7, -3));
		placeBlock(chest.getRelative(0, 8, -3));
		placeBlock(chest.getRelative(1, 7, -3));
		placeBlock(chest.getRelative(-1, 7, -3));
		placeBlock(chest.getRelative(2, 6, -3));
		placeBlock(chest.getRelative(2, 7, -3));
		placeBlock(chest.getRelative(2, 8, -3));
		placeBlock(chest.getRelative(-2, 6, -3));
		placeBlock(chest.getRelative(-2, 7, -3));
		placeBlock(chest.getRelative(-2, 8, -3));
		
	}
	
	//Bed color per team
	
	void placeBlock(Block block) {
		if (block.getY() < 256 && block.isEmpty() && g.getManager().canPlace(block)) {
			blocks.add(block);
		}
	}
	
	@Override
	public void onStop() {
		Block block = chest.getRelative(face.getOppositeFace());
		for (int i = 0; i <= 6; i++) {
			if (DataTable.isWool(block.getRelative(face.getOppositeFace()).getType())) {
				Block relative = block.getRelative(0, i, 0);
				if (relative.getY() < 256 && (relative.isEmpty() || DataTable.isWool(relative.getType())) && g.getManager().canPlace(relative)) {
					g.getRollback().add(relative.getState());
					relative.setType(Material.LADDER, false);
					Directional dir = (Directional) relative.getBlockData();
					dir.setFacing(face);
					relative.setBlockData(dir);
					g.getPlaced().add(relative);
				}
			}
		}
	}

}
