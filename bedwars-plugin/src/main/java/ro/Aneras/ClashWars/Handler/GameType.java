package ro.Aneras.ClashWars.Handler;

public enum GameType {

	BEDWARS("BedWars", "bw.admin", "§c", -1, "bedwars", "bw");
	
	private int time;
	private String sc;
	private String pr;
	private int amount;
	private String name;
	private String perm;
	private String color;
	private boolean enabled;
	
	private GameType(String name, String perm, String color, int time, String pr, String sc) {
		this.enabled = true;
		this.name = name;
		this.perm = perm;
		this.color = color;
		this.time = time;
		this.sc = sc;
		this.pr = pr;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void isEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTimer(int time) {
		this.time = time;
	}
	
	public String getPerm() {
		return perm;
	}
	
	public String getAlias() {
		return sc;
	}
	
	public String getCommand() {
		return pr;
	}
	
	public String getColor() {
		return color;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public boolean containCommand(String cmd) {
		return pr.contains(cmd) || sc.contains(cmd);
	}
	
	public static GameType getEnum(String name) {
		for (GameType type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
	
	public GameType getEnumByName(String name) {
		for (GameType type : values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
	
}
