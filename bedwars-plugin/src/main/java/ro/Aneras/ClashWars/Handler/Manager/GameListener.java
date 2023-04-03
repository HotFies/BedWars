package ro.Aneras.ClashWars.Handler.Manager;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import ro.Aneras.ClashWars.Handler.Tools.DataTable;
import ro.Aneras.ClashWars.Handler.Tools.ItemBuilder;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameState;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Cache.JoinItem;
import ro.Aneras.ClashWars.Handler.Cache.PlayerData;
import ro.Aneras.ClashWars.Handler.Party.Party;

public class GameListener implements Listener {

	private Main main;
	public GameListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Block clicked = e.getClickedBlock();
		GameSetup setup = main.getSetups().get(p);
		if (setup != null) {
			ItemStack hand = p.getInventory().getItemInMainHand();
			if (hand != null && hand.getType() == Material.DIAMOND_SWORD) {
				e.setCancelled(true);
				if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					Location first = clicked.getLocation();
					setup.setSelection(first, null);
					p.sendMessage(Messages.PREFIX + " §7First position set. §8(§d"+first.getBlockX()+','+first.getBlockY()+','+first.getBlockZ()+"§8)");
				} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					Location second = clicked.getLocation();
					setup.setSelection(null, second);
					p.sendMessage(Messages.PREFIX + " §7Second position set. §8(§d"+second.getBlockX()+','+second.getBlockY()+','+second.getBlockZ()+"§8)");
				}
			}
		}
		Game g = main.getManager().getGame(p);
		ItemStack hand = p.getInventory().getItemInMainHand();
		if (hand == null) {
			hand = new ItemStack(Material.AIR);
		}
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (g == null) {
					if (e.getAction() == Action.RIGHT_CLICK_BLOCK && (clicked.getBlockData() instanceof WallSign)) {
						Location s = clicked.getLocation();
						for (Game game : main.getManager().getGames()) {
							for (Location sign : game.getSigns()) {
								if (s.getWorld() == sign.getWorld() && s.distance(sign) == 0) {
									e.setCancelled(true);
									main.getManager().addPlayer(game, p);
									return;
								}
							}
						}
					}
					for (JoinItem item : main.getJoinItems()) {
						if (p.getInventory().getHeldItemSlot() == item.getSlot()) {
							e.setCancelled(true);
							if (item.getCommand().length() > 0 && item.getCommand().charAt(0) == '/') {
								p.performCommand(item.getCommand().substring(1));
								break;
							}
						}
					}
				} else {
					if (!g.getSettings().canInteract()) {
						e.setCancelled(true);
					}
					Party party = main.getPartyManager().getParty(p);
					if (g.getState() == GameState.WAITING) {
						if (hand.getType() == Material.RED_BED) {
							e.setCancelled(true);
							if (party != null && party.getOwner() != p) {
								p.sendMessage(Messages.PREFIX + " " + Messages.PARTY_OWNER_ONLY);
								return;
							}
							main.getManager().removePlayer(p, g);
							p.sendMessage(Messages.PREFIX + " " + Messages.GAME_YOU_LEFT);
						} else if (hand.getType() == Material.LEATHER) {
							e.setCancelled(true);
							p.openInventory(g.getVoter().getInventory());
						}
					} else if (g.getSpectators().containsKey(p)) {
						e.setCancelled(true);
						if (hand.getType() == Material.RED_BED) {
							if (party != null && party.getOwner() != p) {
								p.sendMessage(Messages.PREFIX + " " + Messages.PARTY_OWNER_ONLY);
								return;
							}
							main.getManager().removePlayer(p, g);
						}
						if (hand.getType() == Material.PAPER) {
							if (party != null && party.getOwner() != p) {
								p.sendMessage(Messages.PREFIX + " " + Messages.PARTY_OWNER_ONLY);
								return;
							}
							main.getManager().removePlayer(p, g);
							Game found = main.getManager().findGame(g.getType(), p, -1);
							if (found != null) {
								main.getManager().addPlayer(found, p);
							}
						}
					}
				}
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		Game g = main.getManager().getGame(p);
		if (!main.getConfiguration().getBoolean("Game.PrivateChat")) {
			return;
		}
		if (g == null) {
			for (Game game : main.getManager().getGames()) {
				e.getRecipients().removeAll(game.getPlayers());
			}
		} else {
			if (g.getState() != GameState.IN_GAME) {
				e.getRecipients().clear();
				e.getRecipients().addAll(g.getPlayers());
				e.setFormat(Messages.CHAT_WAITING.toString());
			}
		}
	}
	
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		Game g = main.getManager().getGame(p);
		if (g != null && !e.isCancelled()) {
			String[] split = e.getMessage().split(" ");
			String cmd = split[0];
			if (cmd.equalsIgnoreCase("/start") && p.hasPermission("bw.admin")) {
				if (g.getState() == GameState.WAITING) {
					if (g.getPlayers().size() >= 2) {
						g.setTimer(0);
						g.isGameStarted(true);
					} else {
						p.sendMessage(Messages.PREFIX + " §cThe game can't start without a minimum of 2 players!");
					}
				} else {
					p.sendMessage(Messages.PREFIX + " §cThe game is already started!");
				}
				e.setCancelled(true);
			} else if (cmd.equalsIgnoreCase("/leave") || cmd.equalsIgnoreCase("/quit")) {
				e.setCancelled(true);
				Party party = main.getPartyManager().getParty(p);
				if (party != null && party.getOwner() != p) {
					p.sendMessage(Messages.PREFIX + " " + Messages.PARTY_OWNER_ONLY);
					return;
				}
				p.sendMessage(Messages.PREFIX + " " + Messages.GAME_YOU_LEFT);
				main.getManager().removePlayer(p, g);
			} else if (!p.hasPermission("cw.admin") && !g.getType().containCommand(cmd) && !DataTable.containsIgnoreCase(main.getConfiguration().getStringList("Game.Whitelist"), cmd)) {
				e.setCancelled(true);
				p.sendMessage(Messages.PREFIX + " " + Messages.GAME_RESTRICTED_COMMAND.toString());
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Game g = main.getManager().getGame(p);
		if (g != null) {
			if (g.getSpectators().get(p) != null || !g.getSettings().canClickInv()) {
				e.setCancelled(true);
			}
			if (e.getSlotType() != SlotType.OUTSIDE && e.getClickedInventory().equals(g.getVoter().getInventory())) {
				g.getVoter().click(p, e.getSlot());
			}
		} else if (main.getBungee() == null) {
			for (JoinItem item : main.getJoinItems()) {
				if (e.getSlot() == item.getSlot()) {
					e.setCancelled(true);
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		PlayerData data = main.getManager().getData().get(p.getUniqueId());
		if (data != null && data.getHowLong() < 1 && e.getNewGameMode() != GameMode.ADVENTURE) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		boolean cancelled = false;
		Game g = main.getManager().getGame(p);
		if (g != null) {
			if (g.getSpectators().get(p) != null || !g.getSettings().canDrop()) {
				cancelled = true;
			}
		} else if (main.getBungee() == null) {
			for (JoinItem item : main.getJoinItems()) {
				if (p.getInventory().getHeldItemSlot() == item.getSlot()) {
					cancelled = true;
					break;
				}
			}
		}
		if (cancelled) {
			ItemStack copy = e.getItemDrop().getItemStack().clone();
			copy.setAmount(p.getItemInHand().getAmount() + copy.getAmount());
			p.getInventory().setItem(p.getInventory().getHeldItemSlot(), copy);
			e.getItemDrop().remove();
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		for (Game g : main.getManager().getGames()) {
			for (Player p : g.getPlayers()) {
				p.hidePlayer(main, e.getPlayer());
			}
		}
		if (main.getBungee() == null) {
			for (JoinItem item : main.getJoinItems()) {
				ItemStack is = ItemBuilder.create(item.getType(), item.getName());
				e.getPlayer().getInventory().setItem(item.getSlot(), is);
			}
		}
		if (main.getPapiHook() != null) {
			main.getPapiHook().onJoin(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		main.getPartyManager().removePlayer(p);
		main.getPartyManager().getInvitations().remove(p);
		Game g = main.getManager().getGame(p);
		if (g != null) {
			main.getManager().removePlayer(p, g);
		}
		GameSetup setup = main.getSetups().get(p);
		if (setup != null) {
			main.getSetups().remove(p);
		}
		if (main.getPapiHook() != null) {
			main.getPapiHook().onLeave(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity().getType() == EntityType.PLAYER) {
			Game g = main.getManager().getGame((Player) e.getEntity());
			if (g != null && !g.getSettings().canDamage()) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		if (e.getEntity().getType() == EntityType.PLAYER) {
			Game g = main.getManager().getGame((Player) e.getEntity());
			if (g != null && !g.getSettings().canHunger()) {
				e.setFoodLevel(20);
			}
		}
	}

	@EventHandler
	public void onHealthRegain(EntityRegainHealthEvent e) {
		if (e.getEntity().getType() == EntityType.PLAYER) {
			Game g = main.getManager().getGame((Player) e.getEntity());
			if (g != null && !g.getSettings().canHeal()) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Game playing = main.getManager().getGame(p);
		if (playing != null) {
			if (playing.getState() == GameState.WAITING) {
				e.setCancelled(true);
			}
			return;
		}
		if (e.getBlock().getState() instanceof Sign) {
			Location s = e.getBlock().getLocation();
			for (Game game : main.getManager().getGames()) {
				for (Location sign : game.getSigns()) {
					if (s.getWorld() == sign.getWorld() && s.distance(sign) == 0) {
						if (p.hasPermission("bw.admin")) {
							p.sendMessage(Messages.PREFIX + " §cSign removed succefully!");
							main.getSigns().set("Signs."+game.getType().getName()+","+game.getID(), null);
							main.getSigns().save();
							game.getSigns().remove(sign);
						} else {
							e.setCancelled(true);
							p.sendMessage(Messages.PREFIX + " §cYou don't have the permission to remove this sign!");
						}
						break;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onSignPlace(SignChangeEvent e) {
		Player p = e.getPlayer();
		if (e.getLine(0).length() <= 2) {
			return;
		}
		GameType type = GameType.getEnum(e.getLine(0).substring(1, e.getLine(0).length()-1));
		if (type != null && type.isEnabled() && p.hasPermission(type.getPerm())) {
			try {
				int id = Integer.parseInt(e.getLine(1));
				Game g = main.getManager().getGame(id, type);
				if (g == null) {
					p.sendMessage("§cThis game doesn't exists!");
				} else {
					Location l = e.getBlock().getLocation();
					e.setLine(0, main.getPlaceholder().replace(g, Messages.SIGN_FIRST.toString()));
					e.setLine(1, main.getPlaceholder().replace(g, Messages.SIGN_SECOND.toString()));
					e.setLine(2, main.getPlaceholder().replace(g, Messages.SIGN_THIRD.toString()));
					e.setLine(3, main.getPlaceholder().replace(g, Messages.SIGN_FOURTH.toString()));
					main.getSigns().set("Signs."+g.getType().getName()+","+g.getID(), l.getWorld().getName()+","+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ());
					main.getSigns().save();
					g.getSigns().add(l);
				}
			} catch (NumberFormatException ex) {
				p.sendMessage("§cInvalid ID Number!");
			}
		}
	}
	
	
}
