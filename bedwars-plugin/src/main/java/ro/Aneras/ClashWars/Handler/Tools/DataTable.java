package ro.Aneras.ClashWars.Handler.Tools;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;

public class DataTable {
	
	private static BlockFace[] faces = new BlockFace[8];
	private static DecimalFormat format = new DecimalFormat("#.##");
	
	static {
		faces[0] = BlockFace.NORTH;
		faces[1] = BlockFace.EAST;
		faces[2] = BlockFace.SOUTH;
		faces[3] = BlockFace.WEST;
		faces[4] = BlockFace.NORTH_EAST;
		faces[5] = BlockFace.NORTH_WEST;
		faces[6] = BlockFace.SOUTH_EAST;
		faces[7] = BlockFace.SOUTH_WEST;
	}
	
	public int divideAndCeilPositive(int a, int b) {
		return (a-1) /b + 1;
	}
	
	public static BlockFace getCardinalDirection(double yaw) {
		double rotation = (yaw - 90) % 360;
		if (rotation < 0) {
			rotation += 360.0;
		}
		if (0 <= rotation && rotation < 22.5) {
			return BlockFace.WEST;
		} else if (22.5 <= rotation && rotation < 67.5) {
			return BlockFace.NORTH_WEST;
		} else if (67.5 <= rotation && rotation < 112.5) {
			return BlockFace.NORTH;
		} else if (112.5 <= rotation && rotation < 157.5) {
			return BlockFace.NORTH_EAST;
		} else if (157.5 <= rotation && rotation < 202.5) {
			return BlockFace.EAST;
		} else if (202.5 <= rotation && rotation < 247.5) {
			return BlockFace.SOUTH_EAST;
		} else if (247.5 <= rotation && rotation < 292.5) {
			return BlockFace.SOUTH;
		} else if (292.5 <= rotation && rotation < 337.5) {
			return BlockFace.SOUTH_WEST;
		} else if (337.5 <= rotation && rotation < 360.0) {
			return BlockFace.NORTH;
		} else {
			return null;
		}
	}
	
	public static BlockFace getBlockCardinal(double yaw) {
		double rotation = (yaw - 90) % 360;
		if (rotation < 0) {
			rotation += 360.0;
		}
		if (0 <= rotation && rotation < 22.5) {
			return BlockFace.WEST;
		} else if (22.5 <= rotation && rotation < 67.5) {
			return BlockFace.WEST;
		} else if (67.5 <= rotation && rotation < 112.5) {
			return BlockFace.NORTH;
		} else if (112.5 <= rotation && rotation < 157.5) {
			return BlockFace.NORTH;
		} else if (157.5 <= rotation && rotation < 202.5) {
			return BlockFace.EAST;
		} else if (202.5 <= rotation && rotation < 247.5) {
			return BlockFace.EAST;
		} else if (247.5 <= rotation && rotation < 292.5) {
			return BlockFace.SOUTH;
		} else if (292.5 <= rotation && rotation < 337.5) {
			return BlockFace.SOUTH;
		} else if (337.5 <= rotation && rotation < 360.0) {
			return BlockFace.NORTH;
		} else {
			return null;
		}
	}
	
	public static BlockFace getNextDirection(BlockFace face) {
		if (face == BlockFace.NORTH) {
			return BlockFace.EAST;
		}
		if (face == BlockFace.EAST) {
			return BlockFace.SOUTH;
		}
		if (face == BlockFace.SOUTH) {
			return BlockFace.WEST;
		}
		if (face == BlockFace.WEST) {
			return BlockFace.NORTH;
		}
		if (face == BlockFace.NORTH_EAST) {
			return BlockFace.EAST;
		}
		if (face == BlockFace.SOUTH_EAST) {
			return BlockFace.SOUTH;
		}
		if (face == BlockFace.SOUTH_WEST) {
			return BlockFace.WEST;
		}
		if (face == BlockFace.NORTH_WEST) {
			return BlockFace.NORTH;
		}
		return BlockFace.NORTH;
	}
	
	public static BlockFace getDirection(Location from, Location to) {
		double dist = 0;
		BlockFace face = null;
		for (int x = 0; x < 4; x++) {
			Block b = from.getBlock().getRelative(faces[x]);
			double curDir = b.getLocation().distance(to);
			if (face == null || dist < curDir) {
				face = faces[x];
				curDir = dist;
			}
		}
		return face;
	}
	
	public static String getFormat(double val) {
		return format.format(val);
	}
	
	public static boolean isWool(Material material) {
		switch (material) {
			case BLACK_WOOL:
			case BLUE_WOOL:
			case BROWN_WOOL:
			case CYAN_WOOL:
			case GRAY_WOOL:
			case GREEN_WOOL:
			case LIGHT_BLUE_WOOL:
			case LIGHT_GRAY_WOOL:
			case LIME_WOOL:
			case MAGENTA_WOOL:
			case ORANGE_WOOL:
			case PINK_WOOL:
			case PURPLE_WOOL:
			case RED_WOOL:
			case WHITE_WOOL:
			case YELLOW_WOOL:
				return true;
			default: {
				return false;
			}
		}
	}
	
	public static boolean contains(Object[] obj, Object o) {
		for (Object ob : obj) {
			if (ob == o) {
				return true;
			}
		}
		return false;
	}
	
	public static void removeEntities(Chunk chunk) {
		if (chunk != null && chunk.getEntities() != null) {
			for (Entity en : chunk.getEntities()) {
				if (en.getType() != EntityType.PLAYER) {
					en.remove();
				}
			}
		}
	}
	
	public static BlockFace[] getFaces() {
		return faces;
	}
	
	public static ArrayList<Block> getBlocks(Block block, int radius) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		int iR = radius + 1;
		for (int x = -iR; x <= iR; x++) {
			for (int z = -iR; z <= iR; z++) {
				for (int y = -iR; y <= iR; y++) {
					Block curBlock = block.getRelative(x, y, z);
					if (block.getLocation().toVector().subtract(curBlock.getLocation().toVector()).length() <= radius) {
						blocks.add(curBlock);
					}
				}
			}
		}
		return blocks;
	}
	
	public static int getItemAmount(Location loc, Material type) {
		int x = 0;
		for (Entity e : loc.getChunk().getEntities()) {
			if (e.getType() == EntityType.DROPPED_ITEM) {
				Item i = (Item) e;
				if (i.getItemStack().getType() == type) {
					x++;
				}
			}
		}
		return x;
	}
	
	public static double distance(Location f, Location s) {
		return Point2D.distance(f.getX(), f.getZ(), s.getX(), s.getZ());
	}

	public static double distance3d(Location f, Location s) {
		return f.getWorld() == s.getWorld() ? f.distanceSquared(s) : Double.MAX_VALUE;
	}

	public static double distance(Location first, int x, int z) {
		return Point2D.distance(first.getX(), first.getZ(), x, z);
	}
	
	public static String getServerVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}
	
	public static boolean containsIgnoreCase(List<String> list, String cmd) {
		for (String value : list) {
			if (cmd.equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}
	
}
