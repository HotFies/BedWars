package ro.Aneras.ClashWars.Games;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import ro.Aneras.ClashWars.Handler.Tools.ItemBuilder;
import ro.Aneras.ClashWars.Handler.Tools.RandomFetcher;
import ro.Aneras.ClashWars.Messages;
import ro.Aneras.ClashWars.Handler.Cache.TeamColor;

public class GameVoter {
	
	private Game game;
	private Inventory inventory;
	private boolean refresh;
	private Map<UUID, GameTeam> teams = new HashMap<>();
	private Map<TeamColor, List<Player>> voters = new EnumMap<>(TeamColor.class);
	
	public GameVoter(Game game) {
		this.game = game;
		inventory = Bukkit.createInventory(null, 9, Messages.SELECTOR_NAME.toString());
		inventory.setItem(8, ItemBuilder.create(Material.BARRIER, 1, Messages.SELECTOR_CLEAN_NAME.toString(), Messages.SELECTOR_CLEAN_CHOOSE.toString()));
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public boolean isRefresh() {
		return refresh;
	}
	
	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}
	
	public void refresh() {
		
		int islandMax = game.getSettings().getMax() / game.getIslands().size();
		int playing = Math.max(game.getSettings().getMin(), game.getPlayers().size());
		
		int reqIslands = Math.max(2, (playing - 1) / islandMax + 1);
		int avgIslandMax = Math.max(1, playing / reqIslands);
		int extraIsland = playing - avgIslandMax * reqIslands;
		
		for (int x = 0; x < game.getIslands().size(); x++) {
			GameTeam team = game.getIslands().get(x);
			if (x < reqIslands) {
				
				List<Player> list = voters.get(team.getColor());
				int size = list == null ? 0 : list.size();
				int max = (x < extraIsland ? avgIslandMax + 1 : avgIslandMax);
				
				while (size > max) {
					Player p = list.get(RandomFetcher.getRandom(size));
					remove(p, team);
					size--;
					p.sendMessage(Messages.SELECTOR_RANDOM_REMOVED.toString());
				}
				
				TeamColor color = team.getColor();
				List<String> lore = new ArrayList<>();
				if (size > 0 && islandMax <= 8) {
					lore.add(Messages.SELECTOR_VOTERS.toString());
					for (Player p : list) {
						lore.add(" §7- "+color.getTeamColor()+" " + p.getName());
					}
					lore.add("");
				}
				lore.add(Messages.SELECTOR_TEAM_LORE.toString().replace("%team%", color.getColoredName()));
				inventory.setItem(x, ItemBuilder.create(color.getRealMaterial(), 1, color.getColoredName() + (" §8(§d" + size + "§b/§d" + max + "§8)"), lore));
			} else {
				inventory.setItem(x, null);
				List<Player> list = voters.remove(team.getColor());
				if (list != null) {
					for (Player p : list) {
						teams.remove(p.getUniqueId());
						p.sendMessage(Messages.SELECTOR_TEAM_REMOVED.toString());
					}
				}
			}
		}
	}
	
	public GameTeam get(Player p) {
		return teams.get(p.getUniqueId());
	}
	
	public boolean canPartyJoin(int size) {
		
		int islandMax = game.getSettings().getMax() / game.getIslands().size();
		int playing = Math.max(game.getSettings().getMin(), game.getPlayers().size() + size);
		
		int reqIslands = Math.max(2, (playing - 1) / islandMax + 1);
		int avgIslandMax = Math.max(1, playing / reqIslands);
		int extraIsland = playing - avgIslandMax * reqIslands;
		
		if (avgIslandMax >= size) {
			for (int x = 0; x < reqIslands; x++) {
				List<Player> team = voters.get(game.getIslands().get(x).getColor());
				if (team == null || team.size() + size <= avgIslandMax) {
					return true;
				}
			}
		} else if (avgIslandMax + 1 >= size) {
			for (int x = 0; x < extraIsland; x++) {
				List<Player> team = voters.get(game.getIslands().get(x).getColor());
				if (team == null || team.size() + size <= avgIslandMax + 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	public GameTeam getFittingTeam(int size) {
		
		int islandMax = game.getSettings().getMax() / game.getIslands().size();
		int playing = Math.max(game.getSettings().getMin(), game.getPlayers().size() + size);
		
		int reqIslands = Math.max(2, (playing - 1) / islandMax + 1);
		int avgIslandMax = Math.max(1, playing / reqIslands);
		int extraIsland = playing - avgIslandMax * reqIslands;
		
		if (avgIslandMax >= size) {
			for (int x = 0; x < reqIslands; x++) {
				GameTeam is = game.getIslands().get(x);
				List<Player> team = voters.get(is.getColor());
				if (team == null || team.size() + size <= avgIslandMax) {
					return is;
				}
			}
		} else if (avgIslandMax + 1 >= size) {
			for (int x = 0; x < extraIsland; x++) {
				GameTeam is = game.getIslands().get(x);
				List<Player> team = voters.get(is.getColor());
				if (team == null || team.size() + size <= avgIslandMax + 1) {
					return is;
				}
			}
		}
		return null;
	}
	
	public void click(Player p, int slot) {
		if (slot == 8) {
			p.closeInventory();
			GameTeam team = teams.get(p.getUniqueId());
			if (team != null) {
				remove(p, team);
				p.sendMessage(Messages.SELECTOR_CLEANED_CHOSEN.toString());
				p.playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
				refresh = true;
			}
		} else {
			int islandMax = game.getSettings().getMax() / game.getIslands().size();
			int playing = Math.max(game.getSettings().getMin(), game.getPlayers().size());
			
			int reqIslands = Math.max(2, (playing - 1) / islandMax + 1);
			int avgIslandMax = Math.max(1, playing / reqIslands);
			int extraIsland = playing - avgIslandMax * reqIslands;
			
			if (slot < reqIslands) {
				p.closeInventory();
				GameTeam team = game.getIslands().get(slot);
				if (get(p) == team) {
					p.sendMessage(Messages.SELECTOR_ALREADY_IN_TEAM.toString());
				} else {
					List<Player> list = voters.get(team.getColor());
					int teamSize = list == null ? 0 : list.size();
					if (teamSize < (slot < extraIsland ? avgIslandMax + 1 : avgIslandMax)) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
						p.sendMessage(Messages.SELECTOR_CHOOSE_TEAM.toString().replace("%team%", team.getColor().getColoredName()));
						add(p, team);
					} else {
						p.sendMessage(Messages.SELECTOR_IS_FULL.toString());
					}
				}
			}
		}
	}
	
	public void clear() {
		teams.clear();
		voters.clear();
	}
	
	public void add(Player p, GameTeam team) {
		GameTeam temp = teams.get(p.getUniqueId());
		if (temp != null) {
			remove(p, temp);
		}
		teams.put(p.getUniqueId(), team);
		List<Player> list = voters.get(team.getColor());
		if (list == null) {
			list = new ArrayList<>();
			voters.put(team.getColor(), list);
		}
		list.add(p);
		refresh = true;
	}
	
	public void remove(Player p) {
		GameTeam team = teams.get(p.getUniqueId());
		if (team != null) {
			remove(p, team);
		}
		refresh = true;
	}
	
	private void remove(Player p, GameTeam team) {
		teams.remove(p.getUniqueId());
		List<Player> list = voters.get(team.getColor());
		list.remove(p);
		if (list.isEmpty()) {
			voters.remove(team.getColor());
		}
	}
	
}
