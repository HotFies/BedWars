package ro.Aneras.ClashWars.Handler.Cache;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

import ro.Aneras.ClashWars.Messages;

public enum TeamColor {

	RED(Messages.GAME_TEAM_RED.toString(), Color.RED, Material.RED_WOOL, Material.RED_STAINED_GLASS, Material.RED_TERRACOTTA, ChatColor.RED, Material.RED_BED, 1),
	BLUE(Messages.GAME_TEAM_BLUE.toString(), Color.BLUE, Material.BLUE_WOOL, Material.BLUE_STAINED_GLASS, Material.BLUE_TERRACOTTA, ChatColor.BLUE, Material.BLUE_BED, 2),
	GREEN(Messages.GAME_TEAM_GREEN.toString(), Color.GREEN, Material.GREEN_WOOL, Material.GREEN_STAINED_GLASS, Material.GREEN_TERRACOTTA, ChatColor.GREEN, Material.GREEN_BED, 3),
	YELLOW(Messages.GAME_TEAM_YELLOW.toString(), Color.YELLOW, Material.YELLOW_WOOL, Material.YELLOW_STAINED_GLASS, Material.YELLOW_TERRACOTTA, ChatColor.YELLOW, Material.YELLOW_BED, 4),
	AQUA(Messages.GAME_TEAM_AQUA.toString(), Color.AQUA, Material.LIGHT_BLUE_WOOL, Material.LIGHT_BLUE_STAINED_GLASS, Material.LIGHT_BLUE_TERRACOTTA, ChatColor.AQUA, Material.LIGHT_BLUE_BED, 5),
	WHITE(Messages.GAME_TEAM_WHITE.toString(), Color.WHITE, Material.WHITE_WOOL, Material.WHITE_STAINED_GLASS, Material.WHITE_TERRACOTTA, ChatColor.WHITE, Material.WHITE_BED, 6),
	PINK(Messages.GAME_TEAM_PINK.toString(), Color.FUCHSIA, Material.PINK_WOOL, Material.PINK_STAINED_GLASS, Material.PINK_TERRACOTTA, ChatColor.LIGHT_PURPLE, Material.PINK_BED, 7),
	GRAY(Messages.GAME_TEAM_GRAY.toString(), Color.GRAY, Material.GRAY_WOOL, Material.GRAY_STAINED_GLASS, Material.GRAY_TERRACOTTA, ChatColor.DARK_GRAY, Material.GRAY_BED, 8);
	
	private int id;
	private Material real;
	private Color color;
	private String name;
	private ChatColor chat;
	public Material stained;
	public Material clay;
	private Material bed;

	private TeamColor(String name, Color color, Material real, Material stained, Material clay, ChatColor chat, Material bed, int id) {
		this.id = id;
		this.bed = bed;
		this.real = real;
		this.name = name;
		this.color = color;
		this.chat = chat;
		this.clay = clay;
		this.stained = stained;
	}
	
	public Material getRealMaterial() {
		return real;
	}
	
	public String getIDName() {
		return id+name;
	}
	
	public Material getBed() {
		return bed;
	}
	
	public Color getDyeColor() {
		return color;
	}
	
	public ChatColor getChatColor() {
		return chat;
	}
	
	public String getTeamColor() {
		return chat.toString();
	}
	
	public String getColoredName() {
		return chat+name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFirstLetter() {
		return chat+"§l"+name.charAt(0)+chat;
	}

	public static TeamColor getEnum(String name) {
		for (TeamColor type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
	
}
