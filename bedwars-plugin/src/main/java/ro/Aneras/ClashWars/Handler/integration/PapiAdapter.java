package ro.Aneras.ClashWars.Handler.integration;

import org.bukkit.entity.Player;

public interface PapiAdapter {
	
	public void onJoin(Player p);
	
	public void onLeave(Player p);	
	
	public void onTick(long ticks);

	public void enable();

	public void disable();
	
}
