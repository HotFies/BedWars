package ro.Aneras.ClashWars.Version.v1_16_R3;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import ro.Aneras.ClashWars.Version.PlayerHologram;

public class AirText implements PlayerHologram {

	private Player p;
	private boolean isvisible;
	private EntityArmorStand stand;

	public AirText(Player p, Location l) {
		this.p = p;
		stand = new EntityArmorStand(EntityTypes.ARMOR_STAND, ((CraftWorld) l.getWorld()).getHandle());
		stand.setLocation(l.getBlockX()+0.5, l.getBlockY(), l.getBlockZ()+0.5, l.getYaw(), l.getPitch());
		stand.setCustomNameVisible(true);
		stand.setNoGravity(false);
		stand.setBasePlate(false);
		stand.setInvisible(true);
	}
	
	@Override
	public void show(String name) {
		stand.setCustomName(new ChatComponentText(name));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
	}
	
	@Override
	public boolean isVisible() {
		return isvisible;
	}
	
	@Override
	public void isVisible(boolean value) {
		if (value) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(stand));
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
		} else {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(stand.getId());
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
		isvisible = value;
	}
	
	@Override
	public void remove() {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(stand.getId());
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

}
