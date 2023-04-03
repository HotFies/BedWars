package ro.Aneras.ClashWars.Handler.integration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Main;

public class PapiSqlHook extends PapiHook {

	private Leaderboard[] leaderboards = {
			new Leaderboard("wins", "WINS"),
			new Leaderboard("loses", "LOSES"),
			new Leaderboard("kills", "KILLS"),
			new Leaderboard("finalkills", "FINAL_KILLS"),
			new Leaderboard("deaths", "DEATHS"),
			new Leaderboard("finaldeaths", "FINAL_DEATHS"),
			new Leaderboard("bedsdestroyed", "BEDS_DESTROYED"),
			new Leaderboard("playtime", "PLAYTIME"),
			new Leaderboard("levels", "LEVELS")
	};
	private Map<UUID, PlayerData> playerData = new LinkedHashMap<>();
	
	private static final String[] TOP = { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten" };
	
	public PapiSqlHook(Main main) {
		super(main);
	}
	
	private boolean endsWith(String text, String prefix, int offset) {
		return (offset + prefix.length() == text.length()) && text.startsWith(prefix, offset);
	}
	
	public void onJoin(Player p) {
		PlayerData data = new PlayerData(1, 0, new int[leaderboards.length]);
		playerData.put(p.getUniqueId(), data);
		refresh(p.getUniqueId(), data);
	}
	
	public void onLeave(Player p) {
		playerData.remove(p.getUniqueId());
	}
	
	void refresh(UUID uuid, PlayerData data) {
		main.getExecutor().execute(() -> {
			PlayerData temp = new PlayerData(1, 0, new int[leaderboards.length]);
			for (int i = 0; i < leaderboards.length; i++) {
				String url = "SELECT " + leaderboards[i].column + " FROM BedWars WHERE UUID = ? LIMIT 1";
				try (PreparedStatement statement = main.getSQLDatabase().getConnection().prepareStatement(url)) {
					statement.setString(1, uuid.toString());
					try (ResultSet result = statement.executeQuery()) {
						if (result.next()) {
							temp.stats[i] = result.getInt(1);
						}
					}
				} catch (Exception ignored) {}
			}
			try (ResultSet result = main.getSQLDatabase().getConnection().createStatement().executeQuery("SELECT LEVELS, EXPERIENCE FROM BedWars WHERE UUID = '" + uuid + "' LIMIT 1")) {
				if (result.next()) {
					temp.levels = result.getInt(1);
					temp.experience = result.getInt(2);
				}
			} catch (Exception ignored) {}
			Bukkit.getScheduler().runTask(main, () -> {
				if (Bukkit.getPlayer(uuid) != null) {
					data.levels = temp.levels;
					data.experience = temp.experience;
					data.stats = temp.stats;
				}
			});
		});
	}
	
	@Override
	public String onRequest(OfflinePlayer p, String identifier) {
		if (identifier.startsWith("leaderboard_")) {
			for (Leaderboard leaderboard : leaderboards) {
				if (identifier.startsWith(leaderboard.id, 12)) {
					int offset = 12 + leaderboard.id.length();
					if (identifier.startsWith("_name_", offset)) {
						offset += 6;
						for (int i = 0; i < 10; i++) {
							if (endsWith(identifier, TOP[i], offset)) {
								return leaderboard.names[i];
							}
						}
					} else if (identifier.startsWith("_value_", offset)) {
						offset += 7;
						for (int i = 0; i < 10; i++) {
							if (endsWith(identifier, TOP[i], offset)) {
								return String.valueOf(leaderboard.values[i]);
							}
						}
					}
				}
			}
		} else if (identifier.startsWith("stats_")) {
			if (p != null) {
				PlayerData data = playerData.get(p.getUniqueId());
				if (data != null) {
					for (int i = 0; i < leaderboards.length; i++) {
						if (endsWith(identifier, leaderboards[i].id, 6)) {
							return String.valueOf(data.stats[i]);
						}
					}
				}
			}
		} else if (identifier.startsWith("levels")) {
			if (p != null) {
				PlayerData data = playerData.get(p.getUniqueId());
				if (data != null) {
					
					int levels = data.levels;
					int experience = data.experience;
					
					int experienceReq;
					switch (levels % 100) {
					case 0:
						experienceReq = 500;
						break;
					case 1:
						experienceReq = 1000;
						break;
					case 2:
						experienceReq = 2000;
						break;
					case 3:
						experienceReq = 3500;
						break;
					default:
						experienceReq = 5000;
						break;
					}
					if (identifier.length() == 6) {
						StringBuilder builder = new StringBuilder();
						builder.append(levels);
						builder.append('✩');
						if (levels < 100) {
							builder.insert(0, "§7");
						} else if (levels < 200) {
							builder.insert(0, "§f");
						} else if (levels < 300) {
							builder.insert(0, "§6");
						} else if (levels < 400) {
							builder.insert(0, "§b");
						} else if (levels < 500) {
							builder.insert(0, "§2");
						} else if (levels < 600) {
							builder.insert(0, "§3");
						} else if (levels < 700) {
							builder.insert(0, "§4");
						} else if (levels < 800) {
							builder.insert(0, "§d");
						} else if (levels < 900) {
							builder.insert(0, "§5");
						} else if (levels < 1000) {
							builder.insert(0, "§9");
						} else {
							String colours = "c6eab5d";
							int length = builder.length();
							for (int i = 0; i < length; i++) {
								builder.insert(i * 3, new char[] { '§', colours.charAt(i)});
							}
						}
						return builder.toString();
					} else if (endsWith(identifier, "exp", 7)) {
						return contractNumber(experience);
					} else if (endsWith(identifier, "exp_req", 7)) {
						return contractNumber(experienceReq);
					} else if (endsWith(identifier, "exp_bar", 7)) {
						return formatPercentage('■', 10, experience/(double)experienceReq);
					}
				}
			}
		} else {
			return super.onRequest(p, identifier);
		}
		return null;
	}
	
	private static String contractNumber(int nr) {
		if (nr >= 1000) {
			StringBuilder builder = new StringBuilder();
			if (nr % 1000 == 0) {
				builder.append(nr / 1000);
			} else {
				builder.append(formatDecimal(nr / 1000.0, 1));
			}
			builder.append('k');
			return builder.toString();
		}
		return String.valueOf(nr);
	}
	
    private static String formatDecimal(double decimal, int precision) {
        int end = 0;
        boolean found = false;
        StringBuilder builder = new StringBuilder();
        builder.append(decimal);
        while (end < builder.length()) {
            if (found && (precision-- <= 0)) {
                break;
            } else if (builder.charAt(end) == '.') {
                found = true;
            }
            end++;
        }
        builder.setLength(end);
        while (precision > 0) {
            builder.append('0');
            precision--;
        }
        return builder.toString();
    }
	
    private static String formatPercentage(char barStyle, int barLength, double percent) {
        char[] chars = new char[barLength + 4];
        Arrays.fill(chars, barStyle);
        chars[0] = '§';
        chars[1] = 'a';
        int offset = (int) (barLength * percent + 2);
        chars[offset] = '§';
        chars[offset + 1] = '7';
        return new String(chars);
    }
	
	public void onTick(long ticks) {
		if (ticks % main.getSQLDatabase().getRefreshInTicks() == 0) {
			main.getExecutor().execute(() -> {
				for (Leaderboard leaderboard : leaderboards) {
					String url = "SELECT NAME, " + leaderboard.column + " FROM BedWars ORDER BY " + leaderboard.column + " DESC LIMIT 10";
					try {
						ResultSet result = main.getSQLDatabase().getConnection().prepareStatement(url).executeQuery();
						for (int i = 0; i < 10; i++) {
							if (result.next()) {
								leaderboard.names[i] = result.getString(1);
								leaderboard.values[i] = result.getInt(2);
							} else {
								leaderboard.names[i] = "None";
								leaderboard.values[i] = 0;
							}
						}
					} catch (Exception ignored) {}
				}
			});
			for (Entry<UUID, PlayerData> entry : playerData.entrySet()) {
				refresh(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private static class Leaderboard {
		
		private String id;
		private String column;
		
		private int[] values = new int[10];
		private String[] names = new String[10];
		
		private Leaderboard(String id, String column) {
			this.id = id;
			this.column = column;
		}
		
	}
	
	private static class PlayerData {
		
		private int levels = 1;
		private int experience;
		private int[] stats;
		
		private PlayerData(int levels, int experience, int[] stats) {
			this.levels = levels;
			this.experience = experience;
			this.stats = stats;
		}
		
	}

}
