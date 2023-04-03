package ro.Aneras.ClashWars.Handler.Manager;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import ro.Aneras.ClashWars.Handler.Tools.ItemBuilder;
import ro.Aneras.ClashWars.Handler.Tools.RandomFetcher;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Api.GameJoinEvent;
import ro.Aneras.ClashWars.Api.GameLeaveEvent;
import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Games.GameTeam;
import ro.Aneras.ClashWars.Handler.GameState;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Cache.PlayerData;
import ro.Aneras.ClashWars.Handler.Party.Party;
import ro.Aneras.ClashWars.Handler.Statusbar.Statusbar;

public class GameManager {

	private Main main;
	public Location spawn;
	private List<Game> games = new ArrayList<Game>();
	private Queue<Game> rollback = new ArrayDeque<>();
	private HashMap<UUID, PlayerData> data = new HashMap<UUID, PlayerData>();

	public GameManager(Main main) {
		this.main = main;
	}
	
	public void addPlayer(Game g, Player p) {
		if (g.getSettings().canJoin()) {
			Party party = main.getPartyManager().getParty(p);
			if (party != null && party.getOwner() != p && !g.getPlayers().contains(party.getOwner())) {
				p.sendMessage(Messages.PREFIX + Messages.PARTY_OWNER_ONLY.toString());
			} else if (getGame(p) != null) {
				p.sendMessage(Messages.PREFIX + " " + Messages.GAME_JOIN_ANOTHER_GAME);
			} else if (g.getState() != GameState.WAITING) {
				p.sendMessage(Messages.PREFIX + " " + Messages.GAME_NO_GAME);
			} else if (party == null ? g.getPlayers().size() >= g.getSettings().getMax() : !main.getPartyManager().canJoin(g, p, party)) {
				p.sendMessage(Messages.PREFIX + " " + Messages.GAME_FULL);
			} else {
				GameJoinEvent e = new GameJoinEvent(p);
				main.getServer().getPluginManager().callEvent(e);
				g.getPlayers().add(p);
				data.put(p.getUniqueId(), new PlayerData(main, p));
				p.getInventory().setHeldItemSlot(4);
				g.getLobby().getChunk().load();
				p.teleport(g.getLobby());
				p.setGameMode(GameMode.SURVIVAL);
				g.getBoards().add(new Statusbar(main, p));
				if (!g.isGameStarted()) {
					for (Statusbar bar : g.getBoards()) {
						g.updateSidebar(bar.getSidebar());
					}
					if (g.getPlayers().size() >= g.getSettings().getMin()) {
						g.isGameStarted(true);
					}
				}
				p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
				p.playEffect(g.getLobby(), Effect.ENDER_SIGNAL, 1);
				if (p.hasPermission("bedwars.vote")) {
					p.getInventory().setItem(0, ItemBuilder.create(Material.LEATHER, Messages.SELECTOR_ITEM_NAME.toString()));
				}
				p.getInventory().setItem(8, ItemBuilder.create(Material.RED_BED, Messages.LEAVE_ITEM_NAME.toString()));
				g.broadcast(Messages.PREFIX + " " + main.getPlaceholder().replace(p, g, Messages.GAME_JOIN.toString()));
				updateSigns(g);
				for (Player online : Bukkit.getOnlinePlayers()) {
					if (g.getPlayers().contains(online)) {
						online.showPlayer(main, p);
					} else {
						p.hidePlayer(main, online);
					}
				}
				g.getVoter().setRefresh(true);
				if (party == null) {
					
				} else if (party.getOwner() == p) {
					List<Player> cleanup = new ArrayList<Player>();
					for (Player pp : main.getPartyManager().getInvitations().keySet()) {
						if (main.getPartyManager().getInvitations().get(pp) == party) {
							cleanup.add(pp);
						}
					}
					cleanup.forEach(c -> main.getPartyManager().getInvitations().remove(c));
					cleanup.clear();
					GameTeam team = g.getVoter().getFittingTeam(party.getSize());
					if (team != null) {
						g.getVoter().add(p, team);
					}
					for (Player mem : party.getMembers()) {
						Game game = getGame(mem);
						if (team != null) {
							g.getVoter().add(mem, team);
						}
						if (game == g) {
							continue;
						}
						if (game != null) {
							removePlayer(mem, game);
						}
						addPlayer(g, mem);
					}
				}
			}
		} else {
			p.sendMessage(Messages.PREFIX + " §cThe arena setup wasn't completed, please make sure you followed all the steps!");
		}
	}
	
	public void removePlayer(Player p, Game g) {
		g.removePlayer(p);
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (g.getPlayers().contains(online)) {
				online.hidePlayer(main, p);
			} else {
				p.showPlayer(main, online);
			}
		}
		if (g.getState() == GameState.WAITING) {
			g.getVoter().remove(p);
		}
		Party party = main.getPartyManager().getParty(p);
		if (party != null && party.getOwner() == p) {
			for (Player mem : party.getMembers()) {
				Game game = getGame(mem);
				if (game != null) {
					removePlayer(mem, game);
				}
			}
		}
		data.remove(p.getUniqueId()).restore(true);
		GameLeaveEvent e = new GameLeaveEvent(p);
		main.getServer().getPluginManager().callEvent(e);
	}
	
	public void stopGame(Game g) {
		g.endGame();
		for (Statusbar board : g.getBoards()) {
			board.reset();
		}
		g.getBoards().clear();
		if (g.getRollback().size() > 0) {
			int size = g.getRollback().size();
			if (size/60 >= 30) {
				g.setRollbackSize((size/30)/4);
			} else {
				g.setRollbackSize(15);
			}
			g.setState(GameState.RESETING);
			rollback.add(g);
		} else {
			g.setState(GameState.WAITING);
		}
		for (Player p : g.getPlayers()) {
			data.remove(p.getUniqueId()).restore(true);
			for (Player online : Bukkit.getOnlinePlayers()) {
				p.showPlayer(main, online);
			}
		}
		g.isGameStarted(false);
		List<Player> temp = new ArrayList<>(g.getPlayers());
		g.getPlayers().clear();
		if (main.getBungee() == null) {
			updateSigns(g);
		} else {
			main.getBungee().onStop(temp);
		}
	}
	
	public void updateSigns(Game g) {
		for (Location l : g.getSigns()) {
			Block b = l.getBlock();
			b.getChunk().load();
			if (b.getState() instanceof Sign) {
				Sign s = (Sign) b.getState();
				if (main.getConfiguration().getBoolean("Game.SignGlass")) {
					Block attached = b.getRelative(((org.bukkit.material.Sign) b.getState().getData()).getAttachedFace());
					if (g.getState() == GameState.WAITING) {
						attached.setType(Material.GREEN_STAINED_GLASS);
					} else {
						attached.setType(Material.RED_STAINED_GLASS);
					}
				}
				s.setLine(0, main.getPlaceholder().replace(g, Messages.SIGN_FIRST.toString()));
				s.setLine(1, main.getPlaceholder().replace(g, Messages.SIGN_SECOND.toString()));
				s.setLine(2, main.getPlaceholder().replace(g, Messages.SIGN_THIRD.toString()));
				s.setLine(3, main.getPlaceholder().replace(g, Messages.SIGN_FOURTH.toString()));
				s.update(true);
			}
		}
	}
	
	public Game findGame(GameType type, Player p, int mode) {
		int players = 0;
		Game found = null;
		Party party;
		if (p != null) {
			party = main.getPartyManager().getParty(p);
		} else {
			party = null;
		}
		if (party == null) {
			for (Game g : games) {
				if (g.getType() == type && (mode == -1 || mode == g.getMode())) {
					int size = g.getPlayers().size();
					if (g.getState() == GameState.WAITING && size < g.getSettings().getMax() && g.getTimer() > 2) {
						if (found == null) {
							found = g;
							players = size;
						} else if (size > players) {
							if (RandomFetcher.getRandom().nextBoolean()) {
								found = g;
								players = size;
							}
						}
					}
				}
			}
		} else {
			for (Game g : games) {
				if (g.getType() == type && (mode == -1 || mode == g.getMode())) {
					int size = g.getPlayers().size() + party.getSize();
					if (g.getState() == GameState.WAITING && size <= g.getSettings().getMax() && g.getTimer() > 2 && g.getVoter().canPartyJoin(party.getSize())) {
						if (found == null) {
							found = g;
							players = size;
						} else if (size > players) {
							if (RandomFetcher.getRandom().nextBoolean()) {
								found = g;
								players = size;
							}
						}
					}
				}
			}
		}
		return found;
	}
	
	public Game findGame(GameType type, Player p, int teams, int teamMax) {
		int players = 0;
		Game found = null;
		Party party = main.getPartyManager().getParty(p);
		if (party == null) {
			for (Game g : games) {
				if (g.getType() == type && g.getIslands().size() == teams && g.getSettings().getMax() / teams == teamMax) {
					int size = g.getPlayers().size();
					if (g.getState() == GameState.WAITING && size < g.getSettings().getMax() && g.getTimer() > 2) {
						if (found == null) {
							found = g;
							players = size;
						} else if (size > players) {
							found = g;
							players = size;
						}
					}
				}
			}
		} else {
			for (Game g : games) {
				if (g.getType() == type && g.getIslands().size() == teams && g.getSettings().getMax() / teams == teamMax) {
					int size = g.getPlayers().size() + party.getSize();
					if (g.getState() == GameState.WAITING && size <= g.getSettings().getMax() && g.getTimer() > 2 && g.getVoter().canPartyJoin(party.getSize())) {
						if (found == null) {
							found = g;
							players = size;
						} else if (size > players) {
							found = g;
							players = size;
						}
					}
				}
			}
		}
		return found;
	}
	
	public void reset(Player p) {
		p.setExp(0);
		p.setLevel(0);
		p.setFireTicks(0);
		p.setFoodLevel(20);
		p.setFlying(false);
		p.setFlySpeed(0.2F);
		p.setWalkSpeed(0.2F);
		p.setFallDistance(0);
		p.getEnderChest().clear();
		p.setAllowFlight(false);
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
		p.setHealth(20);
		p.setGameMode(GameMode.SURVIVAL);
		p.getInventory().setArmorContents(null);
		if (p.isInsideVehicle()) {
			p.leaveVehicle();
		}
		p.getInventory().clear();
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
		p.closeInventory();
		p.updateInventory();
	}
	
	public List<Game> getGames() {
		return games;
	}

	public HashMap<UUID, PlayerData> getData() {
		return data;
	}
	
	public Game getGame(int id, GameType type) {
		for (Game g : games) {
			if (g.getID() == id && g.getType() == type) {
				return g;
			}
		}
		return null;
	}
	
	public Game getGame(Player p) {
		for (Game g : games) {
			if (g.getPlayers().contains(p)) {
				return g;
			}
		}
		return null;
	}

	public int getPlayers(GameType type) {
		int players = 0;
		for (Game g : games) {
			if (g.getType() == type) {
				players += g.getPlayers().size();
			}
		}
		return players;
	}
	
	public int getStats(GameType type) {
		int gameCount = 0;
		int available = 0;
		for (Game g : games) {
			if (g.getType() == type) {
				gameCount++;
				if (g.getState() == GameState.WAITING && g.getPlayers().size() < g.getSettings().getMax()) {
					available++;
				}
			}
		}
		if (gameCount > 0) {
			if (available > 0) {
				return 1;
			} else {
				return 2;
			}
		}
		return 0;
	}
	
	public void removeGame(Game g) {
		g.getPlayers().clear();
		games.remove(g);
		for (Location l : g.getSigns()) {
			Block b = l.getBlock();
			if (b.getBlockData() instanceof WallSign) {
				Sign s = (Sign) b.getState();
				s.setLine(0, "");
				s.setLine(1, "");
				s.setLine(2, "");
				s.setLine(3, "");
				s.update();
			}
		}
	}

	public void onTick(long ticks) {
		if (ticks % 5 == 0 && rollback.size() > 0) {
			Game game = rollback.peek();
			for (int x = 0; x < game.getRollbackSize(); x++) {
				if (game.getRollback().size() > 0) {
					game.getRollback().removeLast().update(true, false);
				} else {
					game.setState(GameState.WAITING);
					main.getManager().updateSigns(game);
					rollback.poll();
					break;
				}
			}
		}
	}
}
