package ro.Aneras.ClashWars.Version.v1_16_R3;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.*;
import net.minecraft.server.v1_16_R3.PacketPlayInClientCommand.EnumClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftFirework;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import ro.Aneras.ClashWars.Version.PlayerHologram;
import ro.Aneras.ClashWars.Version.VersionInterface;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class v1_16_R3 implements VersionInterface {
	
	private Collection<CraftScoreboard> shittyCollection;

	@SuppressWarnings("unchecked")
	public v1_16_R3() {
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
	public void respawn(Player p) {
		((CraftPlayer) p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
	}
	
	@Override
	public void isArmorVisible(boolean value, Player p, List<Player> players) {
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		List<Pair<EnumItemSlot, ItemStack>> equipment = new ArrayList<>();
		if (value) {
			equipment.add(new Pair<>(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(p.getInventory().getHelmet())));
			equipment.add(new Pair<>(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(p.getInventory().getChestplate())));
			equipment.add(new Pair<>(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(p.getInventory().getLeggings())));
			equipment.add(new Pair<>(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(p.getInventory().getBoots())));
		} else {
			equipment.add(new Pair<>(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(null)));
			equipment.add(new Pair<>(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(null)));
			equipment.add(new Pair<>(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(null)));
			equipment.add(new Pair<>(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(null)));
		}
		for (Player all : players) {
			((CraftPlayer) all).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityEquipment(ep.getId(), equipment));
		}
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