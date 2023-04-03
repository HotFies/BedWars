package ro.Aneras.ClashWars.Handler.Cache;

import org.bukkit.Material;

public class JoinItem {

	private int slot;
	private int data;
	private String name;
	private Material type;
	private String command;
	
	public JoinItem(int slot, int data, Material type, String command, String name) {
		this.slot = slot;
		this.data = data;
		this.type = type;
		this.name = name;
		this.command = command;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public int getData() {
		return data;
	}
	
	public String getName() {
		return name;
	}
	
	public Material getType() {
		return type;
	}
	
	public String getCommand() {
		return command;
	}
}
