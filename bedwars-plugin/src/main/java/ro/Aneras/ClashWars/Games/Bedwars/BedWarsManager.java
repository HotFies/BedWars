package ro.Aneras.ClashWars.Games.Bedwars;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bed.Part;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Consumer;

import ro.Aneras.ClashWars.Api.PlayerKillEvent;
import ro.Aneras.ClashWars.Handler.Statusbar.Statusbar;
import ro.Aneras.ClashWars.Handler.Tools.DataTable;
import ro.Aneras.ClashWars.Handler.Tools.ItemBuilder;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Games.Bedwars.Cache.PlayerData;
import ro.Aneras.ClashWars.Handler.GameState;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;
import ro.Aneras.ClashWars.Handler.Party.Party;

public class BedWarsManager {

	private Main main;
	private BedWars g;

	public BedWarsManager(Main main, BedWars g) {
		this.g = g;
		this.main = main;
	}
	
	public void addTeam(BedWarsIsland island) {
		g.getIslands().add(island);
		int mode = g.getMode();
		if (mode == 5) {
			if (g.getIslands().size() == g.getIsland()) {
				g.getSettings().canJoin(true);
			}
		} else if (mode > 2) {
			if (g.getIslands().size() == 4) {
				g.getSettings().canJoin(true);
			}
		} else if (g.getIslands().size() == 8) {
			g.getSettings().canJoin(true);
		}
	}
	
	public boolean hasTeam(TeamColor color) {
		for (BedWarsIsland bw : g.getIslands()) {
			if (bw.getColor().equals(color)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasNearbyShop(Entity e, List<Entity> toFind) {
		for (Entity en : toFind) {
			if (en.getLocation().distance(e.getLocation()) <= 3) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isUnique(Material type) {
		if (type == Material.SHEARS) {
			return true;
		}
		if (type.name().contains("AXE")) {
			return true;
		}
		if (type.name().contains("SWORD")) {
			return true;
		}
		return false;
	}
	
	public void refreshTop() {
		Player[] top = g.getTop();
		top[0] = null;
		top[1] = null;
		top[2] = null;
		for (int x = 0; x < 3; x++) {
			int best = -1;
			Player p = null;
			for (Entry<Player, PlayerData> entry : g.getDatas().entrySet()) {
				if (entry.getValue().getStats()[0] > best && !DataTable.contains(top, entry.getKey())) {
					best = entry.getValue().getStats()[0];
					p = entry.getKey();
				}
			}
			top[x] = p;
		}
	}
	
	public void checkEnding() {
		if (g.getState() == GameState.IN_GAME) {
			int i = 0;
			BedWarsIsland winner = null;
			for (BedWarsIsland island : g.getIslands()) {
				if (island.isActive() && island.getAlive() > 0) {
					winner = island;
					i++;
				}
			}
			if (i == 1) {
				g.setTimer(12);
				g.setWinner(winner);

				List<String> cwin = main.getConfiguration().getStringList("Reward.Winners");
				List<String> closer = main.getConfiguration().getStringList("Reward.Losers");
				for (Player p : g.getPlayers()) {
					if (winner.getTeam().contains(p)) {
						for (String command : cwin) {
							main.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
						}
					} else {
						for (String command : closer) {
							main.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
						}
					}
				}

				for (Statusbar bar : g.getBoards()) {
					g.updateSidebar(bar.getSidebar());
				}
				g.setState(GameState.END);
				for (Item drop : g.getDrops().keySet()) {
					drop.remove();
				}
				g.getDrops().clear();
				for (BedWarsIsland is : g.getIslands()) {
					for (Player p : is.getTeam()) {
						p.closeInventory();
						PlayerData data = g.getDatas().get(p);
						if (is == winner) {
							if (data != null) {
								if (g.getSettings().getMax() / g.getIslands().size() > 2) {
									data.getStats()[5] += 50;
								} else {
									data.getStats()[5] += 100;
								}
							}
							main.getVersion().sendTitle(p, 0, 160, 0, Messages.BEDWARS_VICTORY.toString(), "");
						} else {
							main.getVersion().sendTitle(p, 0, 160, 0, Messages.BEDWARS_GAMEOVER.toString(), "");
						}
						if (data != null) {
							Party party = main.getPartyManager().getParty(p);
							if (party != null && party.getSize() > 1) {
								data.getStats()[5] += 25;
							}
						}
						p.sendMessage(Messages.BEDWARS_TEAM_WIN.toString().replace("%team%", winner.getColor().getColoredName()));
					}
				}
				g.getSettings().canClickInv(false);
				refreshTop();
				g.sendTop();
				g.sendReward(winner.getTeam(), g.getTop(), null);
			}
		}
	}
	
	public void removeBed(Block b) {
		if (b.getBlockData() instanceof Bed) {
			Bed bed = (Bed) b.getBlockData();
			BlockFace face = bed.getFacing();
			if (bed.getPart() == Part.HEAD) {
				face = face.getOppositeFace();
			}
			Block relative = b.getRelative(face);
			//b.getWorld().playEffect(relative.getLocation(), Effect.STEP_SOUND, b.getType());
			//relative.getWorld().playEffect(relative.getLocation(), Effect.STEP_SOUND, relative.getType());
			if (bed.getPart() == Part.HEAD) {
				relative.setType(Material.AIR);
				b.setType(Material.AIR);
			} else {
				b.setType(Material.AIR);
				relative.setType(Material.AIR);
			}
		}
	}
	
	public void rollbackBed(BedWarsIsland island) {
		Block b = island.getBed().getBlock();
		if (b.getBlockData() instanceof Bed) {
			Bed bed = (Bed) b.getBlockData();
			BlockFace face = bed.getFacing();
			if (bed.getPart() == Part.HEAD) {
				face = face.getOppositeFace();
			}
			Block relative = b.getRelative(face);
			g.getRollback().add(b.getLocation().getBlock().getState());
			g.getRollback().add(relative.getLocation().getBlock().getState());
		} else {
			b.setType(Material.RED_BED);
			Bed bed = (Bed) b.getBlockData();
			bed.setPart(Part.HEAD);
			b.setBlockData(bed);
			Block relative = b.getRelative(bed.getFacing().getOppositeFace());
			relative.setType(Material.RED_BED);
			bed = (Bed) relative.getBlockData();
			bed.setPart(Part.FOOT);
			relative.setBlockData(bed);
			System.out.println("[BedWars] ERROR: The bed was missing! It has been recreeated at World: " + b.getWorld().getName() + " X: " + b.getX() + " Y: " + b.getY() + " Z: " + b.getZ());
		}
	}

	public boolean canPlace(Block b) {
		Location loc = b.getLocation();
		for (BedWarsIsland is : g.getIslands()) {
			if (is.isActive() && DataTable.distance3d(is.getBed(), loc) <= 16) {
				return true;
			}
		}
		for (Location l : g.getEmerald()) {
			if (DataTable.distance3d(l, loc) <= 16) {
				return false;
			}
		}
		for (Location l : g.getDiamond()) {
			if (DataTable.distance3d(l, loc) <= 16) {
				return false;
			}
		}
		for (BedWarsIsland island : g.getIslands()) {
			if (island.isActive() && (
					DataTable.distance3d(island.getIron(), loc) <= 16 ||
							DataTable.distance3d(island.getShop(), loc) <= 4 ||
							DataTable.distance3d(island.getUpgrade(), loc) <=4 )) {
				return false;
			}
		}
		return true;
	}
	
	public void forEachProtectedChunk(Consumer<Chunk> consumer) {
		for (Location l : g.getEmerald()) {
			consumer.accept(l.getChunk());
		}
		for (Location l : g.getDiamond()) {
			consumer.accept(l.getChunk());
		}
		for (BedWarsIsland island : g.getIslands()) {
			consumer.accept(island.getIron().getChunk());
			consumer.accept(island.getSpawn().getChunk());
		}
	}
	
	public void addItems(Player p, PlayerData data) {
		ItemStack sword = ItemBuilder.create(Material.WOODEN_SWORD, false, null);
		if (data.getIsland().isSharpened()) {
			sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		}
		if (data.getIsland().getHaste() > 0) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, data.getIsland().getHaste()-1), true);
		}
		p.getInventory().addItem(sword);
		if (data.getShearTier() == 1) {
			p.getInventory().addItem(new ItemStack(Material.SHEARS));
		}
		if (data.getPickAxeTier() == 4) {
			ItemStack is = ItemBuilder.create(Material.DIAMOND_PICKAXE, true, null);
			is.addEnchantment(Enchantment.DIG_SPEED, 3);
			p.getInventory().addItem(is);
		}
		if (data.getPickAxeTier() == 3) {
			ItemStack is = ItemBuilder.create(Material.IRON_PICKAXE, true, null);
			is.addEnchantment(Enchantment.DIG_SPEED, 2);
			p.getInventory().addItem(is);
		}
		if (data.getPickAxeTier() == 2) {
			ItemStack is = ItemBuilder.create(Material.STONE_PICKAXE, true, null);
			is.addEnchantment(Enchantment.DIG_SPEED, 1);
			p.getInventory().addItem(is);
		}
		if (data.getPickAxeTier() == 1) {
			ItemStack is = ItemBuilder.create(Material.WOODEN_PICKAXE, true, null);
			is.addEnchantment(Enchantment.DIG_SPEED, 1);
			p.getInventory().addItem(is);
		}
		if (data.getAxeTier() == 4) {
			ItemStack is = ItemBuilder.create(Material.DIAMOND_AXE, true, null);
			is.addEnchantment(Enchantment.DIG_SPEED, 3);
			p.getInventory().addItem(is);
		}
		if (data.getAxeTier() == 3) {
			ItemStack is = ItemBuilder.create(Material.IRON_AXE, true, null);
			is.addEnchantment(Enchantment.DIG_SPEED, 2);
			p.getInventory().addItem(is);
		}
		if (data.getAxeTier() == 2) {
			ItemStack is = ItemBuilder.create(Material.STONE_AXE, true, null);
			is.addEnchantment(Enchantment.DIG_SPEED, 1);
			p.getInventory().addItem(is);
		}
		if (data.getAxeTier() == 1) {
			ItemStack is = ItemBuilder.create(Material.WOODEN_AXE, true, null);
			is.addEnchantment(Enchantment.DIG_SPEED, 1);
			p.getInventory().addItem(is);
		}
	}
	
	public void sendResources(Player p, Player killed) {
		int[] res = new int[4];
		for (ItemStack is : killed.getInventory().getContents()) {
			if (is == null) continue;
			if (is.getType() == Material.DIAMOND) {
				res[0] += is.getAmount();
			}
			if (is.getType() == Material.EMERALD) {
				res[1] += is.getAmount();
			}
			if (is.getType() == Material.GOLD_INGOT) {
				res[2] += is.getAmount();
			}
			if (is.getType() == Material.IRON_INGOT) {
				res[3] += is.getAmount();
			}
		}
		if (res[0] > 0) {
			p.sendMessage(Messages.BEDWARS_REWARD_DIAMOND.toString().replace("%amount%", res[0]+""));
			p.getInventory().addItem(new ItemStack(Material.DIAMOND, res[0]));
		}
		if (res[1] > 0) {
			p.sendMessage(Messages.BEDWARS_REWARD_EMERALD.toString().replace("%amount%", res[1]+""));
			p.getInventory().addItem(new ItemStack(Material.EMERALD, res[1]));
		}
		if (res[2] > 0) {
			p.sendMessage(Messages.BEDWARS_REWARD_GOLD.toString().replace("%amount%", res[2]+""));
			p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, res[2]));
		}
		if (res[3] > 0) {
			p.sendMessage(Messages.BEDWARS_REWARD_IRON.toString().replace("%amount%", res[3]+""));
			p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, res[3]));
		}
	}
	
	public void onKill(Player p, int cause) {
		if (g.getState() != GameState.IN_GAME) {
			return;
		}
		p.closeInventory();
		String message = "";
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		PlayerData pdata = g.getDatas().get(p);
		if (pdata.isInvisible()) {
			main.getVersion().isArmorVisible(true, p, g.getPlayers());
		}
		main.getVersion().removeStuckArrows(p);
		Player killer = pdata.getLastDamager();
		if (!g.getPlayers().contains(killer) || killer == p) {
			killer = null;
			pdata.setLastDamager(null);
		}
		String pcolor =  pdata.getIsland().getColor().getTeamColor();
		for (Statusbar bar : g.getBoards()) {
			if (pdata.isInvisible()) {
				bar.getTeam().tagHider(false, pdata.getIsland().getColor().getIDName(), p);
			}
			bar.getTeam().addSpectator(p.getName(), pdata.getIsland().getColor().getIDName());
			bar.getHealth().update(p, p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		}
		if (cause == 1) {
			if (killer == null) {
				message = Messages.BEDWARS_DIED.toString().replace("%player%", pcolor+p.getName());
			} else {
				String kcolor =  g.getDatas().get(killer).getIsland().getColor().getTeamColor();
				message = Messages.BEDWARS_KILLED.toString().replace("%player%", pcolor+p.getName()).replace("%killer%", kcolor+killer.getName());
			}
		}
		if (cause == 2) {
			if (killer == null) {
				message = Messages.BEDWARS_KILLED_BY_VOID.toString().replace("%player%", pcolor+p.getName());
			} else {
				String kcolor =  g.getDatas().get(killer).getIsland().getColor().getTeamColor();
				message = Messages.BEDWARS_KILLED_BY_PUSHED.toString().replace("%player%", pcolor+p.getName()).replace("%killer%", kcolor+killer.getName());
			}
		}
		if (cause == 3) {
			if (killer == null || killer == p) {
				message = Messages.BEDWARS_KILLED_BY_EXPLODE.toString().replace("%player%", pcolor+p.getName());
			} else {
				String kcolor =  g.getDatas().get(killer).getIsland().getColor().getTeamColor();
				message = Messages.BEDWARS_KILLED_BY_EXPLODE_BY_PLAYER.toString().replace("%player%", pcolor+p.getName()).replace("%killer%", kcolor+killer.getName());
			}
		}
		PlayerData kdata = null;
		if (killer != null) {
			kdata = g.getDatas().get(killer);
			if (g.getSpectators().get(killer) == null) {
				sendResources(killer, p);
			}
			killer.playSound(killer.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
		}
		if (pdata.getIsland().hasBed()) {
			g.broadcast(message);
			pdata.respawn(p);
			p.teleport(g.getSpectator());
			if (kdata != null) {
				kdata.getStats()[1]++;
			}
			pdata.getStats()[2]++;
			g.sendReward("Reward.Kill", killer);
		} else {
			BedWarsIsland island = pdata.getIsland();
			island.setAlive(island.getAlive() - 1);
			message = message+Messages.BEDWARS_FINAL;
			g.broadcast(message);
			p.sendMessage(Messages.BEDWARS_ELIMINATED.toString());
			p.getWorld().spigot().strikeLightningEffect(p.getLocation(), true);
			p.teleport(g.getSpectator());
			if (kdata != null) {
				kdata.getStats()[0]++;
				kdata.getStats()[1]++;
			}
			pdata.getStats()[2]++;
			pdata.getStats()[3]++;
			g.sendReward("Reward.FinalKill", killer);
			for (Player all : g.getPlayers()) {
				all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);
				if (island.getAlive() <= 0) {
					all.sendMessage("");
					all.sendMessage(Messages.BEDWARS_TEAM_ELIMINATED.toString().replace("%team%", island.getColor().getColoredName()));
					all.sendMessage("");
				}
			}
		}
		PlayerKillEvent e = new PlayerKillEvent(p, pdata.getIsland().hasBed());
		main.getServer().getPluginManager().callEvent(e);
		pdata.isInvisible(false);
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
		p.playEffect(EntityEffect.HURT);
		g.addSpectator(p, pdata.getIsland().hasBed());
		p.getInventory().setArmorContents(null);
		g.sendReward("Reward.Death", p);
	}
	
}
