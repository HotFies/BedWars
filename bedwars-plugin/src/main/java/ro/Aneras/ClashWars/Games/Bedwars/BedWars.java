package ro.Aneras.ClashWars.Games.Bedwars;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Games.GameTeam;
import ro.Aneras.ClashWars.Handler.Spectator.Spectator;
import ro.Aneras.ClashWars.Handler.Statusbar.Statusbar;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Games.Bedwars.Cache.PlayerData;
import ro.Aneras.ClashWars.Games.Bedwars.GUI.ItemShop;
import ro.Aneras.ClashWars.Games.Bedwars.GUI.Upgrade;
import ro.Aneras.ClashWars.Games.Bedwars.Structure.EggBridge;
import ro.Aneras.ClashWars.Games.Bedwars.Structure.PopupTower;
import ro.Aneras.ClashWars.Games.Bedwars.Structure.Structure;
import ro.Aneras.ClashWars.Handler.GameState;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;
import ro.Aneras.ClashWars.Handler.Hologram.Hologram;
import ro.Aneras.ClashWars.Handler.SQLStats.SQLData;
import ro.Aneras.ClashWars.Handler.Statusbar.StatusbarSideBar;
import ro.Aneras.ClashWars.Handler.Tools.DataTable;
import ro.Aneras.ClashWars.Handler.Tools.HoloBuilder;
import ro.Aneras.ClashWars.Handler.Tools.ItemBuilder;
import ro.Aneras.ClashWars.Handler.Tools.RandomFetcher;
import ro.Aneras.ClashWars.Handler.Tools.Selection;
import ro.Aneras.ClashWars.Handler.Tools.Serializer;

public class BedWars extends Game {

	private int island;
	private Selection sel;
	private int sel_count;
	private BedWarsIsland winner;
	private List<Location> diamond;
	private List<Location> emerald;
	private BedWarsManager manager;
	private int[] values = new int[7];
	private int secondsPassed = 0;
	private List<Block> containers = new ArrayList<Block>();
	private List<Entity> shop = new ArrayList<Entity>();
	private List<Block> placed = new ArrayList<Block>();
	private ItemStack item = new ItemStack(Material.AIR);
	private List<Entity> upgrade = new ArrayList<Entity>();
	private List<Hologram> diamonds = new ArrayList<Hologram>();
	private List<Hologram> emeralds = new ArrayList<Hologram>();
	private List<Structure> bridges = new ArrayList<Structure>();
	private List<ArmorStand> generators = new ArrayList<ArmorStand>();
	private Map<Item, Integer> drops = new HashMap<Item, Integer>();
	private List<BedWarsIsland> islands = new ArrayList<BedWarsIsland>();
	private Map<Entity, Player> statics = new HashMap<Entity, Player>();
	private Map<Player, PlayerData> caches = new LinkedHashMap<Player, PlayerData>();

	private final Vector vec = new Vector(0, 0.20, 0);

	public BedWars(Main main, String name, int id, int min, int mode, Location lobby, List<Location> diamond, List<Location> emerald, Location spectator) {
		super(main, GameType.BEDWARS, id, name, min, 0, lobby);
		if (!main.getConfiguration().getBoolean("Game.KeepLobby")) {
			sel = new Selection(lobby.clone().add(20, 20, 20), lobby.clone().subtract(20, 6, 20), false);
		}
		manager = new BedWarsManager(main, this);
		settings.canJoin(false);
		this.diamond = diamond;
		this.emerald = emerald;
		this.spectator = spectator;
		this.mode = mode;
		if (mode == 1) {
			island = 8;
			settings.setMax(8);
		} else if (mode == 2) {
			island = 8;
			settings.setMax(16);
		} else if (mode == 3) {
			island = 4;
			settings.setMax(12);
		} else if (mode == 4) {
			settings.setMax(16);
			island = 4;
		}
		if (settings.getMin() < 2) {
			settings.setMin(2);
		}
	}
	
	public int getIsland() {
		return island;
	}
	
	public void setIsland(int count) {
		island = count;
	}
	
	public Map<Item, Integer> getDrops() {
		return drops;
	}
	
	public List<Block> getPlaced() {
		return placed;
	}
	
	public List<Location> getDiamond() {
		return diamond;
	}
	
	public List<Location> getEmerald() {
		return emerald;
	}
	
	public BedWarsManager getManager() {
		return manager;
	}
	
	public List<BedWarsIsland> getIslands() {
		return islands;
	}
	
	public void setWinner(BedWarsIsland winner) {
		this.winner = winner;
	}
	
	public Map<Player, PlayerData> getDatas() {
		return caches;
	}
	
	@Override
	public void startGame() {	
		values[0] = 1;
		values[1] = 30;
		values[2] = 1;
		values[3] = 65;
		values[4] = 1;
		values[5] = 360;
		values[6] = 1;
		secondsPassed = 0;
		super.startGame();
		settings.canDrop(true);
		settings.canHeal(true);
		settings.canDamage(true);
		settings.canClickInv(true);
		settings.canInteract(true);
		
		int islandMax = settings.getMax() / islands.size();
		
		int reqIslands = Math.max(2, (players.size() - 1) / islandMax + 1);
		int avgIslandMax = Math.max(1, players.size() / reqIslands);
		int extraIsland = players.size() - avgIslandMax * reqIslands;
		
		int curTeam = 0;
		List<Player> curIsland = islands.get(curTeam).getTeam();
		
		Collections.shuffle(players);
		
		for (Player p : players) {
			curIsland.add(p);
			if (curIsland.size() >= (curTeam < extraIsland ? avgIslandMax + 1 : avgIslandMax)) {
				curIsland = islands.get((++curTeam) % islands.size()).getTeam();
			}
		}
		
		//Swap players so they get into their wanted team
		for (Player p : players) {
			GameTeam voted = voter.get(p);
			if (voted != null && !voted.getTeam().contains(p)) {
				for (BedWarsIsland island : islands) {
					if (voted != island && island.getTeam().remove(p)) {
						Player swap = null;
						Iterator<Player> it = voted.getTeam().iterator();
						while (it.hasNext()) {
							swap = it.next();
							GameTeam team = voter.get(swap);
							//Make sure we only take players that didn't voted for this island
							if (team == null || team != voted) {
								break;
							}
							swap = null;
						}
						if (swap == null) {
							island.getTeam().add(p);
						} else {
							voted.getTeam().remove(swap);
							island.getTeam().add(swap);
							voted.getTeam().add(p);
						}
						break;
					}
				}
			}
		}
		
		voter.clear();

		if (sel != null) {
			sel_count = sel.getBlocks().size();
		}

		Vector vec = new Vector(0, 0, 0.005);
		manager.forEachProtectedChunk(chunk -> {
			chunk.addPluginChunkTicket(main);
			DataTable.removeEntities(chunk);
		});
		for (BedWarsIsland island : islands) {
			manager.rollbackBed(island);
			if (island.size() > 0) {
				island.isActive(true);
				island.hasBed(true);
				Villager shop = island.getShop().getWorld().spawn(island.getShop(), Villager.class);
				statics.put(HoloBuilder.create(island.getShop(), Messages.BEDWARS_SHOP.toString()), null);
				shop.setProfession(Profession.WEAPONSMITH);
				shop.setAI(false);
				shop.setSilent(true);
				shop.setVelocity(vec);
				this.shop.add(shop);
				Messages msg_up = mode == 1 ? Messages.BEDWARS_SOLO_UPGRADE : Messages.BEDWARS_TEAM_UPGRADE;
				Villager upgrade = island.getUpgrade().getWorld().spawn(island.getUpgrade(), Villager.class);
				statics.put(HoloBuilder.create(island.getUpgrade(), msg_up.toString()), null);
				upgrade.setProfession(Profession.LIBRARIAN);
				upgrade.setSilent(true);
				upgrade.setAI(false);
				this.upgrade.add(upgrade);
				upgrade.setVelocity(vec);
			} else {
				Block b = island.getBed().getBlock();
				if (b.getBlockData() instanceof Bed) {
					manager.removeBed(b);
				}
			}
			for (Player p : island.getTeam()) {
				p.getInventory().clear();
				p.teleport(island.getSpawn());
				p.setGameMode(GameMode.SURVIVAL);
				TeamColor color = island.getColor();
				main.getVersion().sendTitle(p, 0, 35, 0, Messages.BEDWARS_START_TITLE.toString(), "");
				PlayerData data = new PlayerData(main, island.getBed(), island);
				data.setHologram(main.getVersion().createHologram(p, island.getBed()));
				data.getHologram().show(Messages.BEDWARS_DEFEND_BED_HOLO.toString());
				p.getInventory().addItem(ItemBuilder.create(Material.WOODEN_SWORD, null));
				p.getInventory().setArmorContents(ItemBuilder.create(color.getColoredName(), color.getDyeColor()));
				p.sendMessage("§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
				for (String m : Messages.BEDWARS_START.getList()) {
					p.sendMessage(main.getPlaceholder().centerMessage(m.replace('&', '§')));
				}
				p.sendMessage("§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
				p.playSound(island.getSpawn(), Sound.BLOCK_NOTE_BLOCK_PLING, 2F, 2F);
				caches.put(p, data);
			}
			island.setAlive(island.size());
		}
		for (Statusbar bar : super.getBoards()) {
			bar.showTeam();
			bar.showHealth();
			for (TeamColor color : TeamColor.values()) {
				bar.getTeam().add(this, color.getIDName(), color.getFirstLetter() + " ", color.getChatColor());
			}
			for (Player p : players) {
				bar.getHealth().update(p, p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				bar.getTeam().addPlayer(caches.get(p).getIsland().getColor().getIDName(), p);
			}
		}
		for (Location l : diamond) {
			Location clone = l.clone().add(0, 0.28, 0);
			Hologram holo = new Hologram(clone);
			List<String> msg = Messages.BEDWARS_DIAMOND.getList();
			for (int x = msg.size()-1; x >= 0; x--) {
				holo.updateLine(x, msg.get(x));
			}
			diamonds.add(holo);
			ArmorStand stand = main.getVersion().createGenerator(clone);
			stand.setVisible(false);
			stand.setHelmet(new ItemStack(Material.DIAMOND_BLOCK));
			generators.add(stand);
		}
		for (Location l : emerald) {
			Location clone = l.clone().add(0, 0.28, 0);
			Hologram holo = new Hologram(clone);
			List<String> msg = Messages.BEDWARS_EMERALD.getList();
			for (int x = msg.size()-1; x >= 0; x--) {
				holo.updateLine(x, msg.get(x));
			}
			emeralds.add(holo);
			ArmorStand stand = main.getVersion().createGenerator(clone);
			stand.setVisible(false);
			stand.setHelmet(new ItemStack(Material.EMERALD_BLOCK));
			generators.add(stand);
		}
	}

	@Override
	public void endGame() {
		for (BedWarsIsland island : islands) {
			island.isActive(false);
			island.hasBed(false);
			island.getTeam().clear();
			island.setAlive(0);
			island.setRegenerating(false);
			island.setBlindTrap(false);
			island.setSlowTrap(false);
			island.setSharpened(false);
			island.setHaste(0);
			island.setForge(0);
			island.setArmor(0);
		}
		settings.canDrop(false);
		settings.canHeal(false);
		settings.canDamage(false);
		settings.canClickInv(false);
		settings.canInteract(false);
		bridges.clear();
		for (Hologram holo : diamonds) {
			holo.reset();
		}
		for (Hologram holo : emeralds) {
			holo.reset();
		}
		for (Block b : containers) {
			BlockState state = b.getState();
			if (state instanceof Container) {
				Container container = (Container) state;
				if (container != null && container.getInventory() != null) {
					try {
						container.getInventory().clear();
					} catch (Exception ex) {
						
					}
				}
			}
		}
		for (Player p : spectators.keySet()) {
			p.closeInventory();
		}
		for (PlayerData data : caches.values()) {
			data.getHologram().remove();
		}
		for (Entity e : generators) {
			e.remove();
		}
		for (Entity e : statics.keySet()) {
			e.remove();
		}
		for (Entity e : shop) {
			e.remove();
		}
		for (Entity e : upgrade) {
			e.remove();
		}
		for (Item i : drops.keySet()) {
			i.remove();
		}
		for (int x = 0; x < values.length; x++) {
			values[x] = 0;
		}
		bridges.clear();
		caches.clear();
		containers.clear();
		diamonds.clear();
		emeralds.clear();
		placed.clear();
		generators.clear();
		statics.clear();
		upgrade.clear();
		drops.clear();
		shop.clear();
		manager.forEachProtectedChunk(chunk -> chunk.removePluginChunkTicket(main));
		winner = null;
		super.endGame();
	}
	
	@Override
	public Statusbar updateSidebar(StatusbarSideBar status) {
		if (state == GameState.IN_GAME) {
			LocalDateTime date = LocalDateTime.now();
			status.updateLine(15, Messages.BEDWARS_SB_DATE_COLOR.toString() +  date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear());
			status.updateLine(14, "");
			if (values[4] == 6) {
				status.updateLine(13, Messages.BEDWARS_EVENT_SUDDEN_DEATH.toString());
			} else if (values[4] == 5) {
				status.updateLine(13, Messages.BEDWARS_EVENT_BED_DESTRUCTION.toString());
			} else if (values[4] % 2 == 0) {
				if (values[2] == 2) {
					status.updateLine(13, Messages.BEDWARS_EVENT_EMERALD_MAXED.toString());
				} else {
					status.updateLine(13, Messages.BEDWARS_EVENT_EMERALD_UPGRADE.toString());
				}
			} else {
				if (values[0] == 2) {
					status.updateLine(13, Messages.BEDWARS_EVENT_DIAMOND_MAXED.toString());
				} else {
					status.updateLine(13, Messages.BEDWARS_EVENT_DIAMOND_UPGRADE.toString());
				}
			}
			status.updateLine(12, "§a" + Serializer.timeConverter(values[5]));
			status.updateLine(11, "");
			int x = 10;
			for (BedWarsIsland island : islands) {
				if (x <= 2) break;
				TeamColor color = island.getColor();
				String alive = island.getAlive() > 0 ? String.valueOf(island.getAlive()) : "§c✗";
				String state = " §a" + (island.hasBed() ? '✔' : alive);
				boolean yours = island.getTeam().contains(status.getBoard().getPlayer());
				String end = yours ? Messages.BEDWARS_SB_YOU.toString() : "";
				status.updateLine(x, color.getFirstLetter() + " §f" + color.getName() + ':' + state + end);
				x--;
			}
			if (islands.size() < 5) {
				status.updateLine(5, "");
				PlayerData data = caches.get(status.getBoard().getPlayer());
				status.updateLine(4, Messages.BEDWARS_SB_KILLS.toString()+data.getStats()[1]);
				status.updateLine(3, Messages.BEDWARS_SB_FINALKILLS.toString()+data.getStats()[0]);
			}
			status.updateLine(2, "");
			status.updateLine(1, Messages.SCOREBOARD_LOBBY_SERVER.toString());
		}
		return super.updateSidebar(status);
	}
	
	@Override
	public void removePlayer(Player p) {
		super.removePlayer(p);
		if (start >= 0) {
			PlayerData pdata = caches.remove(p);
			
			if (pdata != null && main.getSQLDatabase() != null) {
					main.getSQLDatabase().addData(new SQLData(type.getName(), p.getUniqueId(), p.getName(), 0,
									(pdata.getIsland().getAlive() > 0 ? 0 : 1), pdata.getStats()[1],
									pdata.getStats()[0], pdata.getStats()[2], pdata.getStats()[3], pdata.getStats()[4],
									(int) (System.currentTimeMillis() - start) / 1000, pdata.getStats()[5]));
			}
			
			BedWarsIsland island = pdata.getIsland();
			for (Statusbar bar : super.getBoards()) {
				bar.getTeam().removePlayer(island.getColor().getIDName(), p);
			}
			pdata.getHologram().remove();
			island.getTeam().remove(p);
			Iterator<Entry<Entity, Player>> it = statics.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Entity, Player> next = it.next();
				if (next.getValue() == p) {
					next.getKey().remove();
					it.remove();
				}
			}
			Spectator spectator = spectators.remove(p);
			if (spectator == null || spectator.isRespawning()) {
				island.setAlive(island.getAlive() - 1);
				if (island.getAlive() <= 0) {
					island.hasBed(false);
					Block b = island.getBed().getBlock();
					if (b.getBlockData() instanceof Bed) {
						manager.removeBed(b);
					}
					manager.checkEnding();
				}

			}
			for (PlayerData data : caches.values()) {
				if (data.getLastDamager() == p) {
					data.setLastDamager(null);
					break;
				}
			}
		} 
	}
	
	@Override
	public void tick(long ticks) {
		if (state == GameState.IN_GAME) {
			if (sel != null && ticks % 5 == 0 && sel_count > 0) {
				int amount = Math.min(sel_count, 40);
				for (int x = 0; x < amount; x++) {
					BlockState state = sel.getBlocks().get(sel_count - 1);
					state.getBlock().setType(Material.AIR);
					rollback.add(state);
					sel_count--;
				}
			}
			if (ticks % 30 == 0) {
				for (BedWarsIsland island : islands) {
					if (island.isActive()) {
						if (ticks % 90 == 0 && DataTable.getItemAmount(island.getIron(), Material.GOLD_INGOT) < 16) {
							for (int x = 0; x < 2; x++) {
								item.setType(Material.GOLD_INGOT);
								Item i = island.getIron().getWorld().dropItem(island.getIron(), item);
								i.setVelocity(vec);
								drops.put(i, 1);
								if (island.getForge() == 0 || island.getForge() == 1 && timer % 4 == 0) {
									break;
								}
							}
						} else if (DataTable.getItemAmount(island.getIron(), Material.IRON_INGOT) < 48) {
							item.setType(Material.IRON_INGOT);
							for (int x = 0; x < 2; x++) {
								Item i = island.getIron().getWorld().dropItem(island.getIron(), item);
								i.setVelocity(vec);
								drops.put(i, 1);
								if (island.getForge() == 0 || timer % 4 == 0) {
									break;
								}
							}
						}
					}
				}
			}
			if (bridges.size() > 0) {
				Iterator<Structure> it = bridges.iterator();
				while (it.hasNext()) {
					Structure egg = it.next();
					if (egg.hasFinished()) {
						egg.onStop();
						it.remove();
					} else {
						egg.run();
					}
				}
			}
		}
		super.tick(ticks);
	}
	
	@Override
	public void run() {
		if (state == GameState.END) {
			if (timer > 0) {
				timer--;
				if (timer > 0) {
					double x = spectator.getX(), y = spectator.getY(), z = spectator.getZ();
					for (int i = 0; i < 10; i++) {
						spectator.setX(x + RandomFetcher.randomRange(-50, 50));
						spectator.setY(y + RandomFetcher.getRandom(10));
						spectator.setZ(z + RandomFetcher.randomRange(-50, 50));
						main.explodeColor(spectator, winner.getColor().getDyeColor());
						spectator.setX(x);
						spectator.setY(y);
						spectator.setZ(z);
					}
				}
			} else {
				main.getManager().stopGame(this);
			}
		} else if (state == GameState.IN_GAME) {
			secondsPassed++;
			for (Entry<Player, PlayerData> values : caches.entrySet()) {
				Player p = values.getKey();
				PlayerData data = values.getValue();
				Spectator spectator = spectators.get(p);
				if (spectator != null && !spectator.isRespawning()) {
					main.getVersion().sendActionBar(p, Messages.BEDWARS_SPECTATOR.toString());
				} else if (secondsPassed % 60 == 0) {
					data.getStats()[5] += 25;
				}
				if (data.getTrapImmunity() > 0) {
					data.setTrapImmunity(data.getTrapImmunity()-1);
				}
				if (data.getLastDamager() != null) {
					if (data.getLastDamagerTimer() > 0) {
						data.setLastDamagerTimer(data.getLastDamagerTimer() - 1);
					} else {
						data.setLastDamager(null);
					}
				}
				if (data.getRespawnItems() != null) {
					if (data.getTimer() > 0) {
						String message = Messages.BEDWARS_RESPAWN_IN.toString().replace("%timer%", String.valueOf(data.getTimer()));
						main.getVersion().sendTitle(p, 0, 40, 0, Messages.BEDWARS_TITLE_DIED.toString(), message);
						p.sendMessage(message);
					} else {
						main.getVersion().sendTitle(p, 0, 20, 5, Messages.BEDWARS_TITLE_RESPAWNED.toString(), "");
						p.sendMessage(Messages.BEDWARS_MSG_RESPAWNED.toString());
						p.teleport(data.getIsland().getSpawn());
						p.getInventory().setArmorContents(data.getRespawnItems());
						data.setRespawnItems(null);
						super.removeSpectator(p);
						if (data.getAxeTier() > 1) {
							data.setAxeTier(data.getAxeTier()-1);
						}
						if (data.getPickAxeTier() > 1) {
							data.setPickAxeTier(data.getPickAxeTier()-1);
						}
						manager.addItems(p, data);
						for (Statusbar bar : super.getBoards()) {
							bar.getHealth().update(p, p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
							bar.getTeam().removeSpectator(p.getName(), data.getIsland().getColor().getIDName());
						}
					}
					data.setTimer(data.getTimer()-1);
				}
				if (data.isInvisible() && !p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
					main.getVersion().isArmorVisible(true, p, players);
					for (Statusbar bar : super.getBoards()) {
						bar.getTeam().tagHider(false, data.getIsland().getColor().getIDName(), p);
					}
					data.isInvisible(false);
				}
				if (spectator == null && data.getIsland().hasBed()) {
					if (DataTable.distance(data.getIsland().getBed(), p.getLocation()) <= 6) {
						if (data.getHologram().isVisible()) {
							data.getHologram().isVisible(false);
						}
					} else if (!data.getHologram().isVisible()) {
						data.getHologram().isVisible(true);
					}
				}
			}
			for (BedWarsIsland island : islands) {
				if (island.isActive()) {
					if (timer % 4 == 0 && island.isRegenerating()) {
						for (Player p : island.getTeam()) {
							if (spectators.get(p) == null && DataTable.distance(p.getLocation(), island.getBed()) <= 24) {
								p.spawnParticle(Particle.VILLAGER_HAPPY, island.getBed(), 80, 5, 5, 5);
								p.spawnParticle(Particle.VILLAGER_HAPPY, island.getSpawn(), 80, 5, 5, 5);
								p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0), true);
							}
						}
					}
					if (timer % 2 == 0 && (island.hasSlowTrap() || island.hasBlindTrap())) {
						for (Player p : players) {
							if (spectators.get(p) == null && caches.get(p).getTrapImmunity() <= 0 && !island.getTeam().contains(p) && DataTable.distance(p.getLocation(), island.getBed()) <= 24) {
								if (island.hasSlowTrap()) {
									p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 200, 0), true);
									island.setSlowTrap(false);
									for (Player t : island.getTeam()) {
										t.playSound(t.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
										main.getVersion().sendTitle(t, 0, 65, 0, Messages.BEDWARS_TITLE_TRAP_TRIGGERED.toString(), Messages.BEDWARS_SUBTITLE_TRAP_TRIGGER.toString());
									}
									break;
								} else if (island.hasBlindTrap()) {
									p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 110, 0), true);
									p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 110, 0), true);
									island.setBlindTrap(false);
									for (Player t : island.getTeam()) {
										t.playSound(t.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
										main.getVersion().sendTitle(t, 0, 65, 0, Messages.BEDWARS_TITLE_TRAP_TRIGGERED.toString(), Messages.BEDWARS_SUBTITLE_MINER_TRIGGER.toString());
									}
									break;
								}
							}
						}
					}
					if (island.getForge() == 3 && values[3] <= 0) {
						item.setType(Material.EMERALD);
						Item i = island.getIron().getWorld().dropItem(island.getIron(), item);
						i.setVelocity(vec);
						drops.put(i, 0);
					}
				}
			}
			if (values[1] <= 0) {
				if (values[0] == 1) {
					values[1] = 30;
				}
				if (values[0] == 2) {
					values[1] = 23;
				}
				if (values[0] == 3) {
					values[1] = 12;
				}
				item.setType(Material.DIAMOND);
				for (Location l : diamond) {
					if (DataTable.getItemAmount(l, Material.DIAMOND) < 4) {
						Item i = l.getWorld().dropItem(l, item);
						i.setVelocity(vec);
						drops.put(i, 0);
					}
				}
			} else {
				values[1]--;
			}
			if (values[3] <= 0) {
				if (values[2] == 1) {
					values[3] = 65;
				}
				if (values[2] == 2) {
					values[3] = 50;
				}
				if (values[2] == 3) {
					values[3] = 35;
				}
				item.setType(Material.EMERALD);
				for (Location l : emerald) {
					if (DataTable.getItemAmount(l, Material.EMERALD) <= 2) {
						Item i = l.getWorld().dropItem(l, item);
						i.setVelocity(vec);
						drops.put(i, 0);
					}
				}
			} else {
				values[3]--;
			}
			for (Hologram holo : diamonds) {
				List<String> msg = Messages.BEDWARS_DIAMOND.getList();
				for (int x = msg.size()-1; x >= 0; x--) {
					String tier = values[0] == 1 ? "I" : values[0] == 2 ? "II" : "III";
					holo.updateLine(x, msg.get(x).replace("%tier%", tier).replace("%time%", values[1]+""));
				}
			}
			for (Hologram holo : emeralds) {
				List<String> msg = Messages.BEDWARS_EMERALD.getList();
				for (int x = msg.size()-1; x >= 0; x--) {
					String tier = values[2] == 1 ? "I" : values[2] == 2 ? "II" : "III";
					holo.updateLine(x, msg.get(x).replace("%tier%", tier).replace("%time%", values[3]+""));
				}
			}
			if (values[4] == 6 && values[5] == 0) {
				main.getManager().stopGame(this);
			} else if (values[5] <= 0) {
				if (values[4] == 5) {
					for (BedWarsIsland island : islands) {
						if (island.hasBed()) {
							manager.removeBed(island.getBed().getBlock());
							island.hasBed(false);
						}
					}
					for (Entry<Player, PlayerData> entry : caches.entrySet()) {
						Player p = entry.getKey();
						PlayerData data = entry.getValue();
						p.playSound(p.getEyeLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
						main.getVersion().sendTitle(p, 0, 65, 0, Messages.BEDWARS_ALL_BED_DESTROYED_TITLE.toString(), Messages.BEDWARS_ALL_BED_DESTROYED_SUBTITLE.toString());
						data.getHologram().remove();
					}
				}
				if (values[4] < 5) {
					if (values[4] % 2 == 0) {
						values[2]++;
						String tier = values[2] == 2 ? "II" : "III";
						super.broadcast(Messages.BEDWARS_EMERALD_UPGRADE.toString().replace("%tier%", tier));
					} else {
						values[0]++;
						String tier = values[0] == 2 ? "II" : "III";
						super.broadcast(Messages.BEDWARS_DIAMOND_UPGRADE.toString().replace("%tier%", tier));
					}
				}
				values[4]++;
				if (values[4] == 6) {
					values[5] = 600;
				} else {
					values[5] = 360;
				}
			} else {
				values[5]--;
			}
			timer++;
		} else {
			super.run();
		}
	}
	
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		if (players.contains(p)) {
			ItemStack is = e.getItem();
			if (is.getType() == Material.POTION) {
				PotionMeta im = (PotionMeta) is.getItemMeta();
				for (PotionEffect potion : im.getCustomEffects()) {
					p.addPotionEffect(potion, true);
				}
				if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
					PlayerData data = caches.get(p);
					data.isInvisible(true);
					for (Statusbar bar : super.getBoards()) {
						bar.getTeam().tagHider(true, data.getIsland().getColor().getIDName(), p);
					}
					main.getVersion().isArmorVisible(false, p, players);
				}
				ItemStack item = e.getItem();
				if (item.getAmount() > 1) {
					item.setAmount(item.getAmount() - 1);
				} else {
					e.setItem(null);
				}
			} else if (is.getType() == Material.MILK_BUCKET) {
				caches.get(p).setTrapImmunity(30);
				ItemStack item = e.getItem();
				if (item.getAmount() > 1) {
					item.setAmount(item.getAmount() - 1);
				} else {
					e.setItem(null);
				}
			}
		}
	}
	
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (state == GameState.IN_GAME && players.contains(p)) {
			e.getRecipients().clear();
			PlayerData data = caches.get(p);
			Spectator spectator = spectators.get(p);
			if (spectator != null && !spectator.isRespawning()) {
				e.getRecipients().addAll(spectators.keySet());
				e.setFormat(Messages.CHAT_SPECTATOR.toString().replace("%player%", p.getName()));
			} else {
				if (settings.getMax() / islands.size() > 1) {
					e.getRecipients().addAll(data.getIsland().getTeam());
				} else {
					e.getRecipients().addAll(players);
				}
				String color = data.getIsland().getColor().getColoredName();
				e.setFormat("§8[%team%§8] §7%1$s§f: %2$s".replace("%team%", color));
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (settings.getMax() / islands.size() > 1 && state == GameState.IN_GAME && players.contains(p)) {
			String cmd = e.getMessage().split(" ")[0];
			PlayerData data = caches.get(p);
			if (spectators.get(p) == null && cmd.equalsIgnoreCase("/shout")) {
				if (!p.hasPermission("bedwars.shout")) {
					p.sendMessage(Messages.GAME_COMMAND_NO_PERM.toString());
				} else if (e.getMessage().length() > 8) {
					e.setCancelled(true);
					String color = data.getIsland().getColor().getColoredName();
					super.broadcast(Messages.CHAT_INGAME_SHOUT.toString().replace("%player%", p.getName()).replace("%team%", color).replace("%2$s", e.getMessage().substring(7, e.getMessage().length())));
				}
			}
		}
	}
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		Item i = e.getItem();
		LivingEntity p = e.getEntity();
		if (players.contains(p)) {
			if (spectators.containsKey(p)) {
				e.setCancelled(true);
			} else if (drops.containsKey(i)) {
				Integer gen = drops.get(i);
				PlayerData data = caches.get(p);
				BedWarsIsland validIsland = null;

				for (BedWarsIsland island : getIslands()) {
					if (island.isActive() && island.getIron().distance(p.getLocation()) < 2.1) {
						validIsland = island;
						break;
					}
				}

				if (gen == 1 && validIsland != null) {
					Material type = e.getItem().getItemStack().getType();
					if (type == Material.IRON_INGOT || type == Material.GOLD_INGOT) {
						for (Player team : data.getIsland().getTeam()) {
							if (team != p && !spectators.containsKey(team) && team.getLocation().distance(validIsland.getIron()) < 2.1) {
								team.getInventory().addItem(i.getItemStack());
							}
						}
					}
				}
				drops.remove(i);
			}
		}
	}
	
	@EventHandler
	public void onItemDisapper(ItemDespawnEvent e) {
		drops.remove(e.getEntity());
	}
	
	@EventHandler
	public void onMerge(ItemMergeEvent e) {
		if (drops.containsKey(e.getTarget())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (players.contains(p)) {
			Item item = e.getItemDrop();
			if (manager.isUnique(item.getItemStack().getType())) {
				e.setCancelled(true);
				p.updateInventory();
			} else {
				drops.put(item, 0);
			}
		}
	}
	
	@EventHandler
	public void onSpawn(ItemSpawnEvent e) {
		Item item = e.getEntity();
		if (item.getItemStack().getType().name().contains("BED")) {
			for (BedWarsIsland island : islands) {
				if (DataTable.distance(island.getBed(), item.getLocation()) <= 5) {
					e.setCancelled(true);
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (players.contains(p)) {
			Block b = e.getClickedBlock();
			ItemStack hand = p.getItemInHand();
			if (spectators.containsKey(p)) {
				e.setCancelled(true);
			} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (b.getType() == Material.CHEST) {
					for (BedWarsIsland island : islands) {
						if (DataTable.distance(island.getBed(), b.getLocation()) <= 25) {
							if (island.getAlive() > 0 && !island.getTeam().contains(p)) {
								e.setUseInteractedBlock(Result.DENY);
								p.sendMessage(Messages.BEDWARS_TEAM_CHEST.toString().replace("%team%", island.getColor().getColoredName()));
								break;
							}
						}
					}
				}
				BlockState state = b.getState();
				if (state instanceof Container && !containers.contains(b)) {
					((Container) state).getInventory().clear();
					containers.add(b);
				}
				if (state instanceof org.bukkit.block.Bed) {
					if (!p.isSneaking() || hand == null || !hand.getType().isSolid()) {
						e.setUseInteractedBlock(Result.DENY);
					}
				}
				if (hand != null && hand.getType() == Material.TNT) {
					e.setCancelled(true);
					Block tnt_loc = e.getClickedBlock().getRelative(e.getBlockFace());
					b.getWorld().playSound(tnt_loc.getLocation(), Sound.ENTITY_TNT_PRIMED, 1f, 1f);
					TNTPrimed tnt = tnt_loc.getWorld().spawn(tnt_loc.getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
					tnt.setYield(0);
					tnt.setFuseTicks(40);
					statics.put(tnt, p);
					if (hand.getAmount() > 1) {
						hand.setAmount(hand.getAmount()-1);
					} else {
						p.getInventory().setItemInHand(null);
					}
				}
			}
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (hand != null && hand.getType() == Material.FIRE_CHARGE) {
					e.setCancelled(true);
					Fireball fb = main.getVersion().launchFireball(p);
					fb.setIsIncendiary(false);
					statics.put(fb, p);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 2));
					if (hand.getAmount() > 1) {
						hand.setAmount(hand.getAmount()-1);
					} else {
						p.getInventory().setItemInHand(null);
					}
				}
				if (hand != null && hand.getType() == Material.EGG) {
					e.setCancelled(true);
					PlayerData data = caches.get(p);
					bridges.add(new EggBridge(this, p, data.getIsland().getColor()));
					if (hand.getAmount() > 1) {
						hand.setAmount(hand.getAmount()-1);
					} else {
						p.getInventory().setItemInHand(null);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onExplode(ExplosionPrimeEvent e) {
		Entity en = e.getEntity();
		if (statics.containsKey(en)) {
			e.setCancelled(true);
			List<Block> blocks = DataTable.getBlocks(en.getLocation().getBlock(), 4);
			Block b = en.getLocation().getBlock();
			b.getWorld().createExplosion(b.getLocation(), 0, false);
			for (int x = 0; x < blocks.size(); x++) {
				double best = 0;
				Block closest = null;
				for (Block block : blocks) {
					double distance = block.getLocation().distance(en.getLocation());
					if (closest == null || distance < best) {
						best = distance;
						closest = block;
					}
				}
				if (closest == null) {
					break;
				}
				if (closest.getType() == Material.AIR || !placed.contains(closest)) {
					blocks.remove(closest);
					continue;
				}
				if (en.getType() == EntityType.FIREBALL && closest.getType() == Material.END_STONE) {
					blocks.remove(closest);
					continue;
				}
				if (closest.getType().name().contains("STAINED_GLASS") || closest.isLiquid()) {
					break;
				}
				if (closest.getType() != Material.OBSIDIAN && closest.getLocation().distance(en.getLocation()) < 4) {
					rollback.add(closest.getState());
					closest.setType(Material.AIR);
				}
				blocks.remove(closest);
			}
			for (Player p : players) {
				if (spectators.get(p) == null && p.getLocation().distance(b.getLocation()) <= 4) {
					PlayerData data = caches.get(p);
					Player killer = statics.get(en);
					if (data != null && killer != null) {
						PlayerData kData = caches.get(killer);
						if (kData != null && data.getIsland() != kData.getIsland()) {
							data.setLastDamager(killer);
						}
					}
					Vector vector = p.getEyeLocation().toVector().subtract(en.getLocation().toVector());
					if (vector.lengthSquared() <= 0.1) {
						vector.setY(1);
					}
					p.setVelocity(vector.normalize().multiply(1.3));
					if (p.getHealth() <= 3) {
						p.playEffect(EntityEffect.HURT);
						manager.onKill(p, 3);
						manager.checkEnding();
					} else {
						p.damage(3);
						for (Statusbar bar : super.getBoards()) {
							bar.getHealth().update(p, p.getHealth());
						}
					}
				}
			}
			statics.remove(en);
		}
	}
	
	@EventHandler
	public void onEmpty(PlayerBucketEmptyEvent e) {
		if (players.contains(e.getPlayer())) {
			Block b = e.getBlockClicked().getRelative(e.getBlockFace());
			if (manager.canPlace(b)) {
				for (BlockFace face : DataTable.getFaces()) {
					for (int x = 0; x < 3; x++) {
						if (b.getRelative(face, x).isLiquid()) {
							e.getPlayer().sendMessage(Messages.BEDWARS_PLACE_DENY.toString());
							e.setCancelled(true);
							return;
						}
					}
				}
				BlockState state = b.getState();
				state.setType(Material.AIR);
				rollback.add(state);
			} else {
				e.setCancelled(true);
				e.getPlayer().sendMessage(Messages.BEDWARS_PLACE_DENY.toString());
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		Entity en = e.getRightClicked();
		if (players.contains(p)) {
			PlayerData data = caches.get(p);
			e.setCancelled(true);
			if (shop.contains(en)) {
				data.setGUI(new ItemShop(this, data, p));
			} else if (upgrade.contains(en)) {
				data.setGUI(new Upgrade(this, data, p));
			}
		}
	}
	
	@EventHandler
	public void onArmorStandInteract(PlayerInteractAtEntityEvent e) {
		Player p =  e.getPlayer();
		Entity en = e.getRightClicked();
		if (en.getType() == EntityType.ARMOR_STAND && players.contains(p)) {
			PlayerData data = caches.get(p);
			e.setCancelled(true);
			if (manager.hasNearbyShop(en, shop)) {
				data.setGUI(new ItemShop(this, data, p));
			} else if (manager.hasNearbyShop(en, upgrade)) {
				data.setGUI(new Upgrade(this, data, p));
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (players.contains(p) && spectators.get(p) == null) {
			PlayerData data = caches.get(p);
			if (e.getSlotType() == SlotType.CONTAINER || e.getSlotType() == SlotType.QUICKBAR) {
				Inventory inv = e.getClickedInventory();
				if (data.getGUI() != null && inv.getItem(e.getSlot()) != null) {
					e.setCancelled(true);
					if (inv.getType() == InventoryType.CHEST) {
						data.getGUI().onClick(e.getSlot());
					}
					return;
				}
				if (e.getClickedInventory() != p.getInventory()) {
					if (e.getCursor() != null && manager.isUnique(e.getCursor().getType())) {
						e.setCancelled(true);
					}
				}
			} else if (e.getSlotType() != SlotType.OUTSIDE) {
				e.setCancelled(true);
			}
			if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				if (e.getCurrentItem() != null && manager.isUnique(e.getCurrentItem().getType())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInvClose(InventoryCloseEvent e) {
		HumanEntity p = e.getPlayer();
		if (players.contains(p) && spectators.get(p) == null) {
			PlayerData data = caches.get(p);
			if (data != null && data.getGUI() != null && !data.getGUI().isSwitching()) {
				data.setGUI(null);
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (players.contains(e.getEntity())) {
			Player p = (Player) e.getEntity();
			if (spectators.containsKey(p) || caches.get(p).isImmune() || e.getCause() == DamageCause.DROWNING) {
				e.setCancelled(true);
			} else if (e.getCause() == DamageCause.FALL) {
				if (e.getDamage() >= p.getHealth()) {
					manager.onKill(p, 1);
					manager.checkEnding();
					e.setCancelled(true);
				} else {
					for (Statusbar bar : super.getBoards()) {
						bar.getHealth().update(p, p.getHealth()-e.getFinalDamage());
					}
				}
			} else if (e.getCause() != DamageCause.ENTITY_ATTACK && e.getCause() != DamageCause.PROJECTILE && e.getCause() != DamageCause.CUSTOM) {
				e.setCancelled(true);
			}
		} else if (drops.containsKey(e.getEntity()) || shop.contains(e.getEntity()) || upgrade.contains(e.getEntity())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		Player killer = null;
		if (e.getDamager().getType() == EntityType.ENDER_PEARL) {
			e.setCancelled(true);
			return;
		}
		if (e.getCause() == DamageCause.PROJECTILE) {
			Projectile proj = (Projectile) e.getDamager();
			if (proj.getShooter() instanceof Player) {
				killer = (Player) proj.getShooter();
				proj.remove();
			}
		}
		if (e.getDamager().getType() == EntityType.PLAYER) {
			killer = (Player) e.getDamager();
		}
		if (killer != null && players.contains(killer)) {
			if (spectators.containsKey(killer)) {
				e.setCancelled(true);
				return;
			}
		}
		if (e.getEntityType() == EntityType.PLAYER) {
			Player p = (Player) e.getEntity();
			if (players.contains(p) && players.contains(killer)) {
				PlayerData pdata = caches.get(p);
				PlayerData kdata = caches.get(killer);
				if (kdata.getIsland() == pdata.getIsland()) {
					e.setCancelled(true);
				} else {
					if (pdata.isInvisible()) {
						p.removePotionEffect(PotionEffectType.INVISIBILITY);
						main.getVersion().isArmorVisible(true, p, players);
						for (Statusbar bar : super.getBoards()) {
							bar.getTeam().tagHider(false, pdata.getIsland().getColor().getIDName(), p);
						}
						pdata.isInvisible(false);
					}
					pdata.setLastDamager(killer);
					for (Statusbar bar : super.getBoards()) {
						bar.getHealth().update(p, p.getHealth() - e.getFinalDamage());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		if (players.contains(player)) {
			e.setKeepInventory(true);
			e.setDeathMessage(null);
			e.setKeepLevel(true);
			e.getDrops().clear();
			Bukkit.getScheduler().runTask(main, () -> {
				if (player.isDead()) {
					main.getVersion().respawn(player);
				}
				if (!getSpectators().containsKey(player)) {
					manager.onKill(player, 1);
					manager.checkEnding();
				}
			});
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onRespawn(PlayerRespawnEvent e) {
		if (players.contains(e.getPlayer())) {
			e.setRespawnLocation(spectator);
		}
	}
	
	@EventHandler
	public void onHealthRegain(EntityRegainHealthEvent e) {
		if (e.getEntity().getType() == EntityType.PLAYER) {
			Player p = (Player) e.getEntity();
			if (players.contains(p)) {
				for (Statusbar bar : super.getBoards()) {
					bar.getHealth().update(p, p.getHealth()+e.getAmount());
				}
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (players.contains(p)) {
			Block b = e.getBlock();
			if (b.getBlockData() instanceof Bed) {
				BedWarsIsland island = null;
				Location l = b.getLocation();
				for (BedWarsIsland is : islands) {
					if (is.isActive() && is.getBed().getWorld() == l.getWorld() && is.getBed().distance(l) <= 3) {
						island = is;
					}
				}
				PlayerData data = caches.get(p);
				if (island == null) {
					p.sendMessage(Messages.BEDWARS_BREAK.toString());
				} else if (data.getIsland() == island) {
					p.sendMessage(Messages.BEDWARS_YOUR_BED_BREAK.toString());
				} else {
					manager.removeBed(b);
					island.hasBed(false);
					data.getStats()[4]++;
					sendReward("Reward.BedDestroyed", p);
					String color = island.getColor().getColoredName();
					b.getWorld().strikeLightningEffect(b.getLocation());
					for (Player all : players) {
						all.sendMessage("");
						if (island.getTeam().contains(all)) {
							all.playSound(all.getEyeLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
							main.getVersion().sendTitle(all, 0, 65, 0, Messages.BEDWARS_BED_DESTROYED_TITLE.toString(), Messages.BEDWARS_BED_DESTROYED_SUBTITLE.toString());
							caches.get(all).getHologram().show(Messages.BEDWARS_DESTROYED_BED_HOLO.toString().replace("%player%", data.getIsland().getColor().getTeamColor()+p.getName()));
							all.sendMessage(Messages.BEDWARS_DESTROYED_BED_YOURS.toString().replace("%player%", data.getIsland().getColor().getTeamColor()+p.getName()));
						} else {
							all.playSound(all.getEyeLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
							all.sendMessage(Messages.BEDWARS_BED_DESTROYED.toString().replace("%player%", data.getIsland().getColor().getTeamColor()+p.getName()).replace("%team%", color));
						}
						all.sendMessage("");
					}
				}
				e.setCancelled(true);
			} else if (placed.contains(b)) {
				placed.remove(b);
				rollback.add(b.getState());
				drops.put(b.getWorld().dropItem(b.getLocation(), new ItemStack(b.getType(), 1)), 0);
				b.setType(Material.AIR);
				e.setCancelled(true);
			} else {
				e.setCancelled(true);
				p.sendMessage(Messages.BEDWARS_BREAK.toString());
			}
		}
	}
	
	@EventHandler
	public void onFall(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (players.contains(p)) {
			if (e.getTo().getBlockY() <= 0) {
				if (spectators.containsKey(p)) {
					p.teleport(spectator);
				} else {
					manager.onKill(p, 2);
					manager.checkEnding();
				}
			}
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (players.contains(p)) {
			if (manager.canPlace(e.getBlock())) {
				if (e.getBlockPlaced().getType() == Material.CHEST) {
					e.setCancelled(true);
					bridges.add(new PopupTower(this, e.getBlockPlaced(), caches.get(p).getIsland().getColor()));
					if (e.getHand() == EquipmentSlot.HAND) {
						ItemStack hand = p.getInventory().getItemInMainHand();
						if (hand.getAmount() > 1) {
							hand.setAmount(hand.getAmount()-1);
						} else {
							p.getInventory().setItemInMainHand(null);
						}
					} else if (e.getHand() == EquipmentSlot.OFF_HAND) {
						ItemStack hand = p.getInventory().getItemInOffHand();
						if (hand.getAmount() > 1) {
							hand.setAmount(hand.getAmount() - 1);
						} else {
							p.getInventory().setItemInOffHand(null);
						}
					}
				} else {
					placed.add(e.getBlock());
					Block down = e.getBlock().getRelative(BlockFace.DOWN);
					if (down.getType() == Material.GRASS) {
						rollback.add(down.getState());
					}
					rollback.add(e.getBlockReplacedState());
				}
			} else {
				p.sendMessage(Messages.BEDWARS_PLACE_DENY.toString());
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onToggle(PlayerToggleFlightEvent e) {
		Player p = e.getPlayer();
		if (players.contains(p) && start >= 0) {
			Spectator spec = spectators.get(p);
			if (spec != null && spec.getHowLong() < 1 && !e.isFlying()) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractAtEntityEvent e) {
		if (players.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
}
