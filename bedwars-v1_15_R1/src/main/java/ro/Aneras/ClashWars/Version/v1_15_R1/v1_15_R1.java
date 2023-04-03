package ro.Aneras.ClashWars.Version.v1_15_R1;

import net.minecraft.server.v1_15_R1.*;
import net.minecraft.server.v1_15_R1.PacketPlayInClientCommand.EnumClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftFirework;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_15_R1.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_15_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import ro.Aneras.ClashWars.Version.PlayerHologram;
import ro.Aneras.ClashWars.Version.VersionInterface;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class v1_15_R1 implements VersionInterface {
	
	private Collection<CraftScoreboard> shittyCollection;

	@SuppressWarnings("unchecked")
	public v1_15_R1() {
		CraftScoreboardManager manager = (CraftScoreboardManager) Bukkit.getScoreboardManager();
		try {
			Field field = manager.getClass().getDeclaredField("scoreboards");
			field.setAccessible(true);
			shittyCollection = (Collection<CraftScoreboard>) field.get(manager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void removeFromShittyCollection(Scoreboard scoreboard) {
		shittyCollection.remove((CraftScoreboard) scoreboard);
	}
	
	public Fireball launchFireball(Player p) {
		EntityPlayer cp = ((CraftPlayer) p).getHandle();
	    Location location = p.getEyeLocation();
	    Vector direction = location.getDirection().multiply(10);
		CustomFireball fireball = new CustomFireball(cp.getWorld(), cp, direction.getX(), direction.getY(), direction.getZ());
		fireball.projectileSource = (ProjectileSource) p;
		fireball.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		cp.getWorld().addEntity(fireball);
		return (Fireball) fireball.getBukkitEntity();
	}
	
	@Override
	public void isArmorVisible(boolean value, Player p, List<Player> players) {
		EntityPlayer ep = ((CraftPlayer)p).getHandle();
		ItemStack air = new ItemStack(Material.AIR);
		for (Player g : players) {
			EntityPlayer eg = ((CraftPlayer)g).getHandle();
			if (value) {
				eg.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(ep.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(p.getInventory().getHelmet())));
				eg.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(ep.getId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(p.getInventory().getChestplate())));
				eg.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(ep.getId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(p.getInventory().getLeggings())));
				eg.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(ep.getId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(p.getInventory().getBoots())));
			} else {
				eg.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(ep.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(air)));
				eg.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(ep.getId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(air)));
				eg.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(ep.getId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(air)));
				eg.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(ep.getId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(air)));
			}
		}
	}
	
	@Override
	public void respawn(Player p) {
		((CraftPlayer) p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
	}
	
	@Override
	public void sendTitle(Player p, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
		PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
		PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
		connection.sendPacket(packetPlayOutTimes);
		if (subtitle != null) {
			IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
			PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
			connection.sendPacket(packetPlayOutSubTitle);
		}
		if (title != null) {
			IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
			PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
			connection.sendPacket(packetPlayOutTitle);
		}
	}

	@Override
	public void removeStuckArrows(Player p) {
		((CraftPlayer)p).getHandle().getDataWatcher().set(new DataWatcherObject<>(10, DataWatcherRegistry.b),0);
	}

	@Override
	public PlayerHologram createHologram(Player p, Location l) {
		return new AirText(p, l);
	}

	@Override
	public ArmorStand createGenerator(Location l) {
		Generator generator = new Generator(((CraftWorld)l.getWorld()).getHandle());
		generator.setPositionRotation(new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ()), 0, 0);
		((CraftWorld) l.getWorld()).addEntity(generator, SpawnReason.CUSTOM);
		return (ArmorStand) generator.getBukkitEntity();
	}

}