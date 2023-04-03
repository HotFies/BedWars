package ro.Aneras.ClashWars;

import org.bukkit.*;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import ro.Aneras.ClashWars.Handler.Bungee.AutoJoin;
import ro.Aneras.ClashWars.Handler.Bungee.AutoScaleAutoJoin;
import ro.Aneras.ClashWars.Handler.Bungee.StandardAutoJoin;
import ro.Aneras.ClashWars.Handler.Manager.GameListener;
import ro.Aneras.ClashWars.Handler.Manager.GameManager;
import ro.Aneras.ClashWars.Handler.Tools.DataTable;
import ro.Aneras.ClashWars.Handler.Tools.RandomFetcher;
import ro.Aneras.ClashWars.Handler.Tools.Serializer;
import ro.Aneras.ClashWars.Version.v1_14_R1.v1_14_R1;
import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Games.GamePlugin;
import ro.Aneras.ClashWars.Handler.Cache.JoinItem;
import ro.Aneras.ClashWars.Handler.Commands.CommandExe;
import ro.Aneras.ClashWars.Handler.Configuration.Configuration;
import ro.Aneras.ClashWars.Handler.GameSetup;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Party.PartyManager;
import ro.Aneras.ClashWars.Handler.Placeholder.Placeholder;
import ro.Aneras.ClashWars.Handler.SQLStats.SQLDatabase;
import ro.Aneras.ClashWars.Handler.integration.PapiAdapter;
import ro.Aneras.ClashWars.Handler.integration.PapiHook;
import ro.Aneras.ClashWars.Handler.integration.PapiSqlHook;
import ro.Aneras.ClashWars.Version.VersionInterface;
import ro.Aneras.ClashWars.Version.v1_15_R1.v1_15_R1;
import ro.Aneras.ClashWars.Version.v1_16_R2.v1_16_R2;
import ro.Aneras.ClashWars.Version.v1_16_R3.v1_16_R3;
import ro.Aneras.ClashWars.Version.v1_17_R1.v1_17_R1;
import ro.Aneras.ClashWars.Version.v1_18_R1.v1_18_R1;
import ro.Aneras.ClashWars.Version.v1_18_R2.v1_18_R2;
import ro.Aneras.ClashWars.Version.v1_19_R1.v1_19_R1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends JavaPlugin {

	private AutoJoin mode;
	private PapiAdapter papi;
	private SQLDatabase sql;
	private UpdateTask update;
	private GamePlugin plugin;
	private GameManager manager;
	private Configuration signs;
	private Configuration config;
	private List<JoinItem> items;
	private Configuration messages;
	private Configuration gamelist;
	private Placeholder placeholder;
	private VersionInterface version;
	private PartyManager partymanager;
	private List<FireworkEffect> effects;
	private List<Configuration> databases;
	private HashMap<Player, GameSetup> setup;
	private ExecutorService executor;
	private boolean isStopping;

	@Override
	public void onEnable() {
		getDataFolder().mkdirs();
		ConsoleCommandSender console = getServer().getConsoleSender();
		String ver = DataTable.getServerVersion();
		if (ver.equals("v1_14_R1")) {
			version = new v1_14_R1();
		} else if (ver.equals("v1_15_R1")) {
			version = new v1_15_R1();
		} else if (ver.equals("v1_16_R2")) {
			version = new v1_16_R2();
		} else if (ver.equals("v1_16_R3")) {
			version = new v1_16_R3();
		} else if (ver.equals("v1_17_R1")) {
			version = new v1_17_R1();
		} else if (ver.equals("v1_18_R1")) {
			version = new v1_18_R1();
		} else if (ver.equals("v1_18_R2")) {
			version = new v1_18_R2();
		} else if (ver.equals("v1_19_R1")) {
			version = new v1_19_R1();
		} else {
			console.sendMessage("§c=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			console.sendMessage("§cClashWars only works on: ");
			console.sendMessage("§c - 1.14.4");
			console.sendMessage("§c - 1.15.2");
			console.sendMessage("§c - 1.16.2");
			console.sendMessage("§c - 1.16.3");
			console.sendMessage("§c - 1.16.4");
			console.sendMessage("§c - 1.17.1");
			console.sendMessage("§c - 1.18.1");
			console.sendMessage("§c - 1.19");
			console.sendMessage("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException ex) {}
			super.setEnabled(false);
			return;
		}
		Bukkit.getScheduler().runTaskLater(this, () -> {
		console.sendMessage("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		console.sendMessage("§aClashWars plugin is loading... ");
		console.sendMessage("§a - Version: " + getDescription().getVersion());
		console.sendMessage("§a - Author: Fr33styler");
		plugin = new GamePlugin();
		placeholder = new Placeholder();
		config = new Configuration(this, "config", false, false);
		items = plugin.loadJoinItems(config);
		messages = new Configuration(this, "messages", true, false);
		for (Messages msg : Messages.values()) {
			if (msg.getList() == null && messages.getString("Messages." + msg.name()) == null) {
				messages.set("Messages." + msg.name(), msg.toString().replace("§", "&"));
			}
			if (msg.getList() != null && messages.getString("MessagesList." + msg.name()) == null) {
				List<String> list = new ArrayList<String>();
				for (String s : msg.getList()) {
					list.add(s.replace("§", "&").replace("▪", "~"));
				}
				messages.set("MessagesList." + msg.name(), list);
			}
		}
		for (String name : messages.getConfigurationSection("Messages").getKeys(false)) {
			Messages msg = Messages.getEnum(name);
			if (msg == null || msg.getList() != null) {
				messages.set("Messages." + name, null);
			} else {
				msg.setMessage(messages.getString("Messages." + name));
			}
		}
		for (String name : messages.getConfigurationSection("MessagesList").getKeys(false)) {
			Messages msg = Messages.getEnum(name);
			if (msg == null) {
				messages.set("MessagesList." + name, null);
			} else {
				msg.getList().clear();
				for (String s : messages.getStringList("MessagesList." + name)) {
					msg.getList().add(s.replace('&', '§').replace("~", "▪"));
				}
			}
		}
		messages.save();
		effects = new ArrayList<FireworkEffect>();
		Builder b = FireworkEffect.builder().trail(false).flicker(false);
		b.withColor(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE);
		effects.add(b.with(Type.BURST).build());
		effects.add(b.with(Type.BALL).build());
		effects.add(b.with(Type.BALL_LARGE).build());
		update = new UpdateTask(this);
		manager = new GameManager(this);
		String lobby = config.getString("Game.Lobby");
		if (lobby != null) {
			manager.spawn = Serializer.getDeserializedLocation(lobby);
		}
		setup = new HashMap<Player, GameSetup>();
		gamelist = new Configuration(this, "games", true, false);
		for (GameType type : GameType.values()) {
			if (gamelist.getString("Games." + type.name()) == null) {
				gamelist.set("Games." + type.name() + ".Enabled", true);
			}
		}
		try {
			databases = new ArrayList<Configuration>();
			//Field f = getServer().getClass().getDeclaredField("commandMap");
			//f.setAccessible(true);
			//CommandMap cmap = (CommandMap) f.get(getServer());
			for (String name : gamelist.getConfigurationSection("Games").getKeys(false)) {
				GameType type = GameType.getEnum(name);
				if (type == null) {
					gamelist.set("Games." + name, null);
				} else {
					Main main = this;
					type.setTimer(gamelist.getInt("Games." + type.name() + ".Timer"));
					if (gamelist.getBoolean("Games." + type.name() + ".Enabled")) {
						Configuration database = new Configuration(this, type.getName(), true, true);
						manager.getGames().addAll(plugin.loadDatabase(main, type, database));
						databases.add(database);
						PluginCommand pluginCommand = getCommand("bedwars");
						if (pluginCommand != null) {
							pluginCommand.setExecutor(new CommandExe(type.getName(), type.getCommand(), plugin.register(this, type)));
						}
						//cmap.register("bedwars", new CommandExe(type.getName(), type.getCommand(), plugin.register(this, type), type.getAlias()));
					} else {
						type.isEnabled(false);
					}
				}
			}
			PluginCommand pluginCommand = getCommand("clashwars");
			if (pluginCommand != null) {
				pluginCommand.setExecutor(new CommandExe("ClashWars", "clashwars", plugin.register(this, null)));
			}
			partymanager = new PartyManager(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (config.getBoolean("BungeeMode.Enabled")) {
			String server = config.getString("BungeeMode.ServerOnGameEnd");
			boolean stop = config.getBoolean("BungeeMode.StopServerOnEnd");
			boolean autoScale = config.getBoolean("BungeeMode.AutoScale");
			boolean kickPlayer = config.getBoolean("BungeeMode.KickPlayer");

			if (autoScale) {
				mode = new AutoScaleAutoJoin(this, server, kickPlayer);
			} else {
				mode = new StandardAutoJoin(this, stop, server, kickPlayer);
			}
		}
		gamelist.save();
		signs = new Configuration(this, "signs", true, false);
		Location l = new Location(null, 0, 0, 0);
		if (signs.get("Signs") != null) {
			for (String s : signs.getConfigurationSection("Signs").getKeys(false)) {
				String[] data = s.split(",");
				try {
					GameType type = GameType.getEnum(data[0]);
					int id = Integer.parseInt(data[1]);
					Game g = manager.getGame(id, type);
					if (g != null) {
						String sign = signs.getString("Signs." + s);
						String[] split = sign.split(",");
						World world = Bukkit.getWorld(split[0]);
						if (world == null) {
							console.sendMessage("§cThe sign at World: " + world + " X: " + split[1] + " Y: " + split[2] + " Z: " + split[3] + " can't be loaded!");
						} else {
							l.setWorld(world);
							l.setX(Integer.parseInt(split[1]));
							l.setY(Integer.parseInt(split[2]));
							l.setZ(Integer.parseInt(split[3]));
							Block block = l.getBlock();
							if (block.getBlockData() instanceof WallSign) {
								g.getSigns().add(block.getLocation());
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		executor = Executors.newSingleThreadExecutor();
		getServer().getPluginManager().registerEvents(new GameListener(this), this);
		if (config.getBoolean("SQLDatabase.Enabled")) {
			boolean mysql = config.getBoolean("SQLDatabase.MySQL");
			console.sendMessage("§a - Loading "+(mysql ? "MySQL" : "SQLite")+"...");
			String host = config.getString("SQLDatabase.Host");
			String database = config.getString("SQLDatabase.Database");
			String username = config.getString("SQLDatabase.Username");
			String password = config.getString("SQLDatabase.Password");
			int port = config.getInt("SQLDatabase.Port");
			int refreshInTicks = config.getInt("SQLDatabase.RefreshInMinutes") * 1200;
			boolean useSSL = config.getBoolean("SQLDatabase.UseSSL");
			sql = new SQLDatabase(this, host, database, username, password, port, refreshInTicks, mysql, useSSL);
			if (!sql.isEnabled()) {
				sql = null;
			}
		}
		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
			if (sql == null) {
				papi = new PapiHook(this);
			} else {
				papi = new PapiSqlHook(this);
			}
			Bukkit.getOnlinePlayers().forEach(p -> papi.onJoin(p));
			papi.enable();
		}
		console.sendMessage("§aClashWars has been loaded!");
		console.sendMessage("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		}, 20L);
	}
	
	@Override
	public void onDisable() {
		isStopping = true;
		if (executor != null) {
			executor.shutdown();
			executor = null;
		}
		for (Game g : manager.getGames()) {
			while (g.getRollback().size() > 0) {
				g.getRollback().removeLast().update(true, false);
			}
			manager.stopGame(g);
			manager.updateSigns(g);
		}
		if (papi != null) {
			papi.disable();
			papi = null;
		}
		if (sql != null) {
			sql.closeConnection();
			sql = null;
		}
		HandlerList.unregisterAll(this);
		manager = null;
		if (update != null) {
			update.cancel();
			update = null;
		}
		isStopping = false;
	}

	public boolean isStopping() {
		return isStopping;
	}

	public void explode(Location l) {
		Firework f = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(effects.get(RandomFetcher.getRandom(effects.size())));
		f.setFireworkMeta(fm);
		f.detonate();
	}
	
	public void explodeColor(Location l, Color color) {
		Firework f = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
		FireworkMeta fm = f.getFireworkMeta();
		Builder b = FireworkEffect.builder().trail(false).flicker(false);
		b.withColor(color);
		int procent = RandomFetcher.getRandom(100);
		if (procent < 35) {
			fm.addEffect(b.with(Type.BALL_LARGE).build());
		} else if (procent < 65) {
			fm.addEffect(b.with(Type.BALL).build());
		} else {
			fm.addEffect(b.with(Type.BURST).build());
		}
		f.setFireworkMeta(fm);
		f.detonate();
	}
	
	public SQLDatabase getSQLDatabase() {
		return sql;
	}
	
	public PapiAdapter getPapiHook() {
		return papi;
	}
	
	public AutoJoin getBungee() {
		return mode;
	}
	
	public Configuration getSigns() {
		return signs;
	}
	
	public List<JoinItem> getJoinItems() {
		return items;
	}
	
	public HashMap<Player, GameSetup> getSetups() {
		return setup;
	}
	
	public Configuration getConfiguration() {
		return config;
	}
	
	public GameManager getManager() {
		return manager;
	}
	
	public PartyManager getPartyManager() {
		return partymanager;
	}
	
	public Placeholder getPlaceholder() {
		return placeholder;
	}
	
	public UpdateTask getUpdateTask() {
		return update;
	}
	
	public VersionInterface getVersion() {
		return version;
	}
	
	public ExecutorService getExecutor() {
		return executor;
	}
	
	public Configuration getDatabase(GameType type) {
		Configuration database = null;
		for (Configuration c : databases) {
			if (c.getName().equalsIgnoreCase(type.getName())) {
				database = c;
				break;
			}
		}
		return database;
	}
	
}
