package ro.Aneras.ClashWars.Version.v1_17_R1;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import ro.Aneras.ClashWars.Version.PlayerHologram;

public class AirText implements PlayerHologram {

	private Player p;
	private boolean isvisible;
	private EntityArmorStand stand;

	public AirText(Player p, Location l) {
		this.p = p;
		stand = new EntityArmorStand(EntityTypes.c, ((CraftWorld) l.getWorld()).getHandle());
		stand.setLocation(l.getBlockX()+0.5, l.getBlockY(), l.getBlockZ()+0.5, l.getYaw(), l.getPitch());
		stand.setCustomNameVisible(true);
		stand.setNoGravity(false);
		stand.setBasePlate(false);
		stand.setInvisible(true);
	}
	
	@Override
	public void show(String name) {
		stand.setCustomName(new ChatComponentText(name));
		((CraftPlayer) p).getHandle().b.sendPacket(new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
	}
	
	@Override
	public boolean isVisible() {
		return isvisible;
	}
	
	@Override
	public void isVisible(boolean value) {
		if (value) {
			((CraftPlayer) p).getHandle().b.sendPacket(new PacketPlayOutSpawnEntityLiving(stand));
			((CraftPlayer) p).getHandle().b.sendPacket(new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
		} else {
			remove();
		}
		isvisible = value;
	}
	
	@Override
	public void remove() {
		int[] ids = {stand.getId()};
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ids);
		((CraftPlayer) p).getHandle().b.sendPacket(packet);
	}

}
