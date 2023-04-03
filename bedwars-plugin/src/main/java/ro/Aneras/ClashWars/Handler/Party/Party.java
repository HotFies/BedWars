package ro.Aneras.ClashWars.Handler.Party;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Party {

	private int max;
	private Player owner;
	private boolean privacy = true;
	private List<Player> members = new ArrayList<Player>();
	
	public Party(Player owner, int max) {
		this.max = max;
		this.owner = owner;
	}
	
	public int getMax() {
		return max;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public boolean getPrivacy() {
		return privacy;
	}
	
	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}
	
	public int getSize() {
		return members.size()+1;
	}
	
	public List<Player> getMembers() {
		return members;
	}
	
}
