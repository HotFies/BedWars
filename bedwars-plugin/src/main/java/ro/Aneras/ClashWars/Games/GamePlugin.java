package ro.Aneras.ClashWars.Games;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import ro.Aneras.ClashWars.Handler.Commands.CommandJoin;
import ro.Aneras.ClashWars.Handler.Configuration.Configuration;
import ro.Aneras.ClashWars.Handler.Tools.Serializer;
import ro.Aneras.ClashWars.Main;
import ro.Aneras.ClashWars.Games.Bedwars.BedWars;
import ro.Aneras.ClashWars.Games.Bedwars.BedWarsIsland;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.BedWarsCreate;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.BedWarsDelete;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.BedWarsQuickJoin;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.BedWarsAutoJoin;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.BedWarsStart;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsAddDiamond;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsAddEmerald;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsAddTeam;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsCustom;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsFinish;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsSetBed;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsSetIron;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsSetLobby;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsSetShop;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsSetSpawn;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsSetSpectator;
import ro.Aneras.ClashWars.Games.Bedwars.Commands.Setup.BedWarsSetUpgrade;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Handler.Cache.JoinItem;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;
import ro.Aneras.ClashWars.Handler.Commands.CommandInterface;
import ro.Aneras.ClashWars.Handler.Commands.CommandQuickJoin;
import ro.Aneras.ClashWars.Handler.Commands.CommandReload;
import ro.Aneras.ClashWars.Handler.Commands.CommandSetlobby;

public class GamePlugin {
	
	public List<CommandInterface> register(Main main, GameType type) {
		List<CommandInterface> cmd = new ArrayList<CommandInterface>();
		if (type == GameType.BEDWARS) {
			cmd.add(new BedWarsCreate(main));
			cmd.add(new BedWarsDelete(main));
			cmd.add(new BedWarsSetLobby(main));
			cmd.add(new BedWarsAddDiamond(main));
			cmd.add(new BedWarsAddEmerald(main));
			cmd.add(new BedWarsSetSpectator(main));
			cmd.add(new BedWarsFinish(main));
			cmd.add(new BedWarsAddTeam(main));
			cmd.add(new BedWarsSetSpawn(main));
			cmd.add(new BedWarsSetIron(main));
			cmd.add(new BedWarsSetShop(main));
			cmd.add(new BedWarsSetUpgrade(main));
			cmd.add(new BedWarsSetBed(main));
			cmd.add(new BedWarsCustom(main));
			cmd.add(new BedWarsStart(main));
			cmd.add(new BedWarsQuickJoin(main));
			cmd.add(new BedWarsAutoJoin(main));
		}
		if (type == null) {
			cmd.add(new CommandQuickJoin(main));
			cmd.add(new CommandReload(main));
			cmd.add(new CommandSetlobby(main));
		} else {
			cmd.add(new CommandJoin(main, type));
		}
		return cmd;
	}
	
	public List<JoinItem> loadJoinItems(Configuration conf) {
		List<JoinItem> items = new ArrayList<JoinItem>();
		if (conf.getString("JoinItems") != null && !conf.isString("JoinItems")) {
			for (String key : conf.getConfigurationSection("JoinItems").getKeys(false)) {
				int slot = conf.getInt("JoinItems." + key + ".Slot");
				boolean enabled = conf.getBoolean("JoinItems." + key + ".Enabled");
				Material type = Material.valueOf(conf.getString("JoinItems." + key + ".Material"));
				int data = conf.getInt("JoinItems." + key + ".Data");
				String command = conf.getString("JoinItems." + key + ".Command");
				String name = conf.getString("JoinItems." + key + ".Name");
				if (enabled && type != null) {
					items.add(new JoinItem(slot, data, type, command, name));
				}
			}
		}
		return items;
	}
	
	public List<Game> loadDatabase(Main main, GameType type, Configuration conf) {
		List<Game> games = new ArrayList<Game>();
		if (conf.getString("Game") != null && !conf.isString("Game")) {
			if (type == GameType.BEDWARS) {
				for (String ID : conf.getConfigurationSection("Game").getKeys(false)) {
					try {
					int min = conf.getInt("Game." + ID + ".Min");
					int mode = conf.getInt("Game." + ID + ".Mode");
					String map = conf.getString("Game." + ID + ".Name");;
					Location lobby = Serializer.getDeserializedLocation(conf.getString("Game." + ID + ".Lobby"));
					Location spectator = Serializer.getDeserializedLocation(conf.getString("Game." + ID + ".Spectator"));
					List<Location> diamond = Serializer.getDeserializedLocations(conf.getStringList("Game." + ID + ".Diamond"));
					List<Location> emerald = Serializer.getDeserializedLocations(conf.getStringList("Game." + ID + ".Emerald"));
					BedWars bw = new BedWars(main, map, Integer.parseInt(ID), min, mode, lobby, diamond, emerald, spectator);
					if (mode == 5) {
						bw.getSettings().setMax(conf.getInt("Game." + ID + ".Max"));
						bw.setIsland(conf.getInt("Game." + ID + ".Islands"));
					}
					games.add(bw);
					if (conf.getString("Game."+ID+".Teams") != null) {
						for (TeamColor team : TeamColor.values()) {
							if (conf.get("Game." + ID + ".Teams."+team.name()) != null) {
								Location bed = Serializer.getDeserializedLocation(conf.getString("Game." + ID + ".Teams."+team.name()+".Bed"));
								Location iron = Serializer.getDeserializedLocation(conf.getString("Game." + ID + ".Teams."+team.name()+".Iron"));
								Location shop = Serializer.getDeserializedLocation(conf.getString("Game." + ID + ".Teams."+team.name()+".Shop"));
								Location upgrade = Serializer.getDeserializedLocation(conf.getString("Game." + ID + ".Teams."+team.name()+".Upgrade"));
								Location spawn = Serializer.getDeserializedLocation(conf.getString("Game." + ID + ".Teams."+team.name()+".Spawn"));
								bw.getManager().addTeam(new BedWarsIsland(team, spawn, shop, upgrade, iron, bed));
							}
						}
					}
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§cThe ID '"+ID+"' couldn't be loaded: " + e.getMessage());
					}
				}
			}
		}
		return games;
	}
	
}
