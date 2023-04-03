
package ro.Aneras.ClashWars.Games;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ro.Aneras.ClashWars.Api.GameEndEvent;
import ro.Aneras.ClashWars.Api.GameStartEvent;
import ro.Aneras.ClashWars.Handler.Spectator.Spectator;
import ro.Aneras.ClashWars.Handler.Statusbar.Statusbar;
import ro.Aneras.ClashWars.Handler.Tools.DataTable;
import ro.Aneras.ClashWars.Handler.Tools.ItemBuilder;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Games.Bedwars.BedWars;
import ro.Aneras.ClashWars.Games.Bedwars.Cache.PlayerData;
import ro.Aneras.ClashWars.Handler.GameState;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;
import ro.Aneras.ClashWars.Handler.SQLStats.SQLData;
import ro.Aneras.ClashWars.Handler.SQLStats.SQLDatabase;
import ro.Aneras.ClashWars.Handler.Statusbar.StatusbarSideBar;

public abstract class Game implements Listener {

	private int id;
	protected int mode;
	protected int timer;
	protected Main main;
	private String name;
	protected GameType type;
	protected Location lobby;
	protected long start = -1;
	private int rollback_size;
	protected GameVoter voter;
	protected Location spectator;
	protected GameSettings settings;
	private boolean isGameStarted = false;
	protected Player[] top = new Player[3];
	protected GameState state = GameState.WAITING;
	protected List<Player> players = new ArrayList<Player>();
	protected List<Location> signs = new ArrayList<Location>();
	private List<Statusbar> boards = new ArrayList<Statusbar>();
	protected Deque<BlockState> rollback = new ArrayDeque<BlockState>();
	protected Map<Player, Spectator> spectators = new HashMap<Player, Spectator>();

	public Game(Main main, GameType type, int id, String name, int min, int max, Location lobby) {
		timer = main.getConfiguration().getInt("Game.WaitTime");
		voter = new GameVoter(this);
		settings = new GameSettings(min, max);
		this.id = id;
		this.type = type;
		this.main = main;
		this.lobby = lobby;
		this.name = name;
	}

	public int getID() {
		return id;
	}

	public int getTimer() {
		return timer;
	}
	
	public String getName() {
		return name;
	}
	
	public GameType getType() {
		return type;
	}

	public GameState getState() {
		return state;
	}

	public void setTimer(int time) {
		timer = time;
	}

	public boolean isGameStarted() {
		return isGameStarted;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public Location getLobby() {
		return lobby;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Statusbar> getBoards() {
		return boards;
	}
	
	public List<Location> getSigns() {
		return signs;
	}

	public void broadcast(String message) {
		players.forEach(p -> p.sendMessage(message));
	}

	public Player[] getTop() {
		return top;
	}

	public int getMode() {
		return mode;
	}
	
	public GameVoter getVoter() {
		return voter;
	}

	public int getRollbackSize() {
		return rollback_size;
	}

	public void setRollbackSize(int size) {
		this.rollback_size = size;
	}
	
	public boolean hasTeam(TeamColor team) {
		return false;
	}
	
	public GameSettings getSettings() {
		return settings;
	}
	
	public abstract List<? extends GameTeam> getIslands();

	public Deque<BlockState> getRollback() {
		return rollback;
	}

	public void startGame() {
		start = System.currentTimeMillis();
		main.getManager().updateSigns(this);
		main.getServer().getPluginManager().registerEvents(this, main);
		main.getServer().getPluginManager().callEvent(new GameStartEvent(this));
	}

	public void addSpectator(Player p, boolean respawn) {
		p.getInventory().clear();
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
		spectators.put(p, new Spectator(this, respawn));
		p.setFallDistance(0);
		p.setGameMode(GameMode.SPECTATOR);
		for (Player pl : players) {
			if (!spectators.containsKey(pl)) {
				pl.hidePlayer(main, p);
			}
		}
		if (!respawn) {
			p.getInventory().setItem(8, ItemBuilder.create(Material.RED_BED, Messages.LEAVE_ITEM_NAME.toString()));
			if (main.getBungee() == null) {
				p.getInventory().setItem(7, ItemBuilder.create(Material.PAPER, Messages.PLAY_AGAIN_NAME.toString()));
			}
		}
	}
	
	public Map<Player, Spectator> getSpectators() {
		return spectators;
	}

	public void removeSpectator(Player p) {
		Spectator spec = spectators.remove(p);
		if (spec != null) {
			if (spec.isRespawning()) {
				for (Player pl : players) {
					pl.showPlayer(main, p);
				}
			} else {
				p.closeInventory();
				p.getInventory().clear();
			}
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
			p.setGameMode(GameMode.SURVIVAL);
		}
	}
	
	public void removePlayer(Player p) {
		players.remove(p);
		if (start < 0) {
			for (Player broadcast : players) {
				broadcast.sendMessage(Messages.PREFIX + " " + main.getPlaceholder().replace(p, this, Messages.GAME_LEAVE.toString()));
			}
		}
		main.getManager().updateSigns(this);
		if (start > 0 && players.size() < 1) {
			main.getManager().stopGame(this);
		}
		for (Statusbar board : boards) {
			if (board.getPlayer() == p) {
				board.reset();
				boards.remove(board);
				break;
			}
		}
	}


	public void isGameStarted(boolean val) {
		if (val) {
			isGameStarted = true;
		} else {
			isGameStarted = false;
			timer = main.getConfiguration().getInt("Game.WaitTime");
			for (Statusbar board : boards) {
				updateSidebar(board.getSidebar());
			}
		}
		main.getManager().updateSigns(this);
	}

	public void sendReward(List<Player> winners, Player[] top, Player winner) {
		SQLDatabase sql = main.getSQLDatabase();
		if (sql != null) {
			long timeplayed = (System.currentTimeMillis() - start) / 1000;
			for (Player p : players) {
				int win = 0;
				int lose = 0;
				if (winner != null) {
					if (winner == p) {
						win = 1;
					} else {
						lose = 1;
					}
				}
				if (winners != null) {
					if (winners.contains(p)) {
						win = 1;
					} else {
						lose = 1;
					}
				}
				if (top != null) {
					if (top[0] == p) {
						win = 1;
					} else {
						lose = 1;
					}
				}
				BedWars bedwars = (BedWars) this;
				PlayerData data = bedwars.getDatas().get(p);
				if (data != null) {
					sql.addData(new SQLData(type.getName(), p.getUniqueId(), p.getName(), win, lose, data.getStats()[1], data.getStats()[0], data.getStats()[2], data.getStats()[3], data.getStats()[4], (int) timeplayed, data.getStats()[5]));
				}
			}
		}
		if (top != null) {
			List<String> ctop = main.getConfiguration().getStringList("Reward.Top");
			for (String command : ctop) {
				if (top[0] != null && command.contains("%first%")) {
					main.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%first%", top[0].getName()));
				} else if (top[1] != null && command.contains("%second%")) {
					main.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%second%", top[1].getName()));
				} else if (top[2] != null && command.contains("%third%")) {
					main.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%third%", top[2].getName()));
				} else if (command.contains("%other%")) {
					for (Player p : players) {
						if (!DataTable.contains(top, p)) {
							main.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%other%", p.getName()));
						}
					}
				}
			}
		}
	}
	
	public void sendReward(String path, Player p) {
		if (p == null) {
			return;
		}
		List<String> list = main.getConfiguration().getStringList(path);
		if (list != null) {
			for (String command : list) {
				main.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
			}
		}
	}
	
	public Location getSpectator() {
		return spectator;
	}

	public void endGame() {
		GameEndEvent e = new GameEndEvent(players, top);
		main.getServer().getPluginManager().callEvent(e);
		top[0] = null;
		top[1] = null;
		top[2] = null;
		start = -1;
		spectators.clear();
		for (int x = 0; x < top.length; x++) {
			top[x] = null;
		}
		HandlerList.unregisterAll(this);
	}

	public void tick(long tick) {
	}

	public void run() {
		if (state == GameState.END) {
			if (timer <= 0) {
				main.getManager().stopGame(this);
				return;
			}
		} else if (state == GameState.WAITING) {
			if (timer > 15 && players.size() >= settings.getMax()) {
				timer = 11;
			}
			if (players.size() >= settings.getMin() && timer <= 0) {
				timer = 0;
				for (Statusbar board : boards) {
					board.getSidebar().reset();
				}
				state = GameState.IN_GAME;
				startGame();
				return;
			}
			if (players.size() < settings.getMin()) {
				isGameStarted(false);
				for (Player p : players) {
					p.sendMessage(Messages.PREFIX + " " + Messages.GAME_NO_PLAYERS);
				}
				return;
			} else {
				for (Player p : players) {
					if (timer <= 5) {
						p.playSound(p.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0F, 1.0F);
						p.sendMessage(Messages.PREFIX + " " + Messages.GAME_START.toString().replace("%timer%", timer + ""));
					} else if (timer % 10 == 0) {
						p.sendMessage(Messages.PREFIX + " " + Messages.GAME_START.toString().replace("%timer%", timer + ""));
					}
				}
			}
		}
		timer--;
	}

	public void sendTop() {
		for (Player p : players) {
			p.sendMessage(main.getPlaceholder().centerMessage("§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));
			p.sendMessage(main.getPlaceholder().centerMessage(type.getColor() + "§l  " + type.getName()));
			p.sendMessage("");
			if (top[0] != null) {
				p.sendMessage(main.getPlaceholder().centerMessage(Messages.GAME_TOP_1ST.toString().replace("%player%", top[0].getName())));
			}
			if (top[1] != null) {
				p.sendMessage(main.getPlaceholder().centerMessage(Messages.GAME_TOP_2ND.toString().replace("%player%", top[1].getName())));
			}
			if (top[2] != null) {
				p.sendMessage(main.getPlaceholder().centerMessage(Messages.GAME_TOP_3RD.toString().replace("%player%", top[2].getName())));
			}
			p.sendMessage("");
			p.sendMessage(main.getPlaceholder().centerMessage("§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));
		}
	}

	public Statusbar updateSidebar(StatusbarSideBar status) {
		if (state == GameState.WAITING) {
			status.updateLine(8, "");
			status.updateLine(7, Messages.SCOREBOARD_LOBBY_NAME.toString() + name);
			status.updateLine(6, Messages.SCOREBOARD_LOBBY_PLAYERS.toString() + players.size());
			status.updateLine(4, "");
			if (isGameStarted) {
				status.updateLine(3, Messages.SCOREBOARD_LOBBY_GAME_START.toString() + timer);
			} else {
				status.updateLine(3, Messages.SCOREBOARD_LOBBY_WAITING.toString());
			}
			status.updateLine(2, "");
			status.updateLine(1, Messages.SCOREBOARD_LOBBY_SERVER.toString());
		}
		return status.getBoard();
	}

}
