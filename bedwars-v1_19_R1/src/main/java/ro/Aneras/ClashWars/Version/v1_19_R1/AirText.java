package ro.Aneras.ClashWars.Version.v1_19_R1;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import ro.Aneras.ClashWars.Version.PlayerHologram;

public class AirText implements PlayerHologram {

	private Player p;
	private boolean isvisible;
	private EntityArmorStand stand;

	public AirText(Player p, Location l) {
		this.p = p;
		stand = new EntityArmorStand(EntityTypes.d, ((CraftWorld) l.getWorld()).getHandle());
		stand.a(l.getBlockX() + 0.5, l.getBlockY(), l.getBlockZ() + 0.5, l.getYaw(), l.getPitch());
		stand.n(true);
		stand.e(false);
		stand.s(false);
		stand.j(true);
	}
	
	@Override
	public void show(String name) {
		stand.b(CraftChatMessage.fromStringOrNull(name));
		((CraftPlayer) p).getHandle().b.a(new PacketPlayOutEntityMetadata(stand.ae(), stand.ai(), true));
	}
	
	@Override
	public boolean isVisible() {
		return isvisible;
	}
	
	@Override
	public void isVisible(boolean value) {
		if (value) {
			((CraftPlayer) p).getHandle().b.a(new PacketPlayOutSpawnEntity(stand));
			((CraftPlayer) p).getHandle().b.a(new PacketPlayOutEntityMetadata(stand.ae(), stand.ai(), true));
		} else {
			remove();
		}
		isvisible = value;
	}
	
	@Override
	public void remove() {
		int[] ids = { stand.ae() };
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ids);
		((CraftPlayer) p).getHandle().b.a(packet);
	}

}
