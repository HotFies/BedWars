package ro.Aneras.ClashWars.Handler.SQLStats;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Bukkit;
import ro.Aneras.ClashWars.Handler.GameType;
import ro.Aneras.ClashWars.Main;

public class SQLDatabase {
	
	private Main main;
	private boolean mysql;
	private boolean enabled;
	private Connection connection;
	private int refreshInTicks;

	public SQLDatabase(Main main, String host, String database, String username, String password, int port, int refreshInTicks, boolean mysql, boolean useSSL) {
		this.main = main;
		this.refreshInTicks = refreshInTicks;
		try {
			if (mysql) {
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true" + (useSSL ? "&verifyServerCertificate=false&useSSL=true" : ""), username, password);
			} else {
				File f = new File(main.getDataFolder() + "/Stats");
				if (!f.exists()) {
					f.mkdir();
				}
				connection = DriverManager.getConnection("jdbc:sqlite:" + f + "/database.db");
			}
			this.mysql = mysql;
			Statement statement = connection.createStatement();
			for (GameType type : GameType.values()) {
				if (type.isEnabled()) {
					DatabaseMetaData metadata = connection.getMetaData();
					if (metadata.getTables(null, null, type.getName(), new String[] { "TABLE" }).next()) {
						if (metadata.getColumns(null, null, type.getName(), "SCORE").next()) {
							statement.execute("ALTER TABLE " + type.getName() + " DROP COLUMN SCORE;");
						}
						if (checkConversion(metadata, type.getName(), "KILLS")) {
							statement.executeUpdate("ALTER TABLE " + type.getName()
									+ " ADD KILLS INTEGER NOT NULL DEFAULT 0 AFTER LOSES;");
						}
						if (checkConversion(metadata, type.getName(), "FINAL_KILLS")) {
							statement.executeUpdate("ALTER TABLE " + type.getName()
									+ " ADD FINAL_KILLS INTEGER NOT NULL DEFAULT 0 AFTER KILLS;");
						}
						if (checkConversion(metadata, type.getName(), "DEATHS")) {
							statement.executeUpdate("ALTER TABLE " + type.getName()
									+ " ADD DEATHS INTEGER NOT NULL DEFAULT 0 AFTER FINAL_KILLS;");
						}
						if (checkConversion(metadata, type.getName(), "FINAL_DEATHS")) {
							statement.executeUpdate("ALTER TABLE " + type.getName()
									+ " ADD FINAL_DEATHS INTEGER NOT NULL DEFAULT 0 AFTER DEATHS;");
						}
						if (checkConversion(metadata, type.getName(), "BEDS_DESTROYED")) {
							statement.executeUpdate("ALTER TABLE " + type.getName()
									+ " ADD BEDS_DESTROYED INTEGER NOT NULL DEFAULT 0 AFTER FINAL_DEATHS;");
						}
						if (checkConversion(metadata, type.getName(), "LEVELS")) {
							statement.executeUpdate("ALTER TABLE BedWars ADD LEVELS INTEGER NOT NULL DEFAULT 1 AFTER PLAYTIME;");
						}
						if (checkConversion(metadata, type.getName(), "EXPERIENCE")) {
							statement.executeUpdate("ALTER TABLE BedWars ADD EXPERIENCE INTEGER NOT NULL DEFAULT 0 AFTER LEVELS;");
						}
					}
					if (mysql) {
						statement.executeUpdate("CREATE TABLE IF NOT EXISTS BedWars (id INTEGER NOT NULL AUTO_INCREMENT, UUID VARCHAR(36) UNIQUE, NAME VARCHAR(16), WINS INTEGER, LOSES INTEGER, KILLS INTEGER, FINAL_KILLS INTEGER, DEATHS INTEGER, FINAL_DEATHS INTEGER, BEDS_DESTROYED INTEGER, PLAYTIME INTEGER, LEVELS INTEGER, EXPERIENCE INTEGER, PRIMARY KEY (id))");
					} else {
						statement.executeUpdate("CREATE TABLE IF NOT EXISTS BedWars (UUID VARCHAR(36) UNIQUE, NAME VARCHAR(16), WINS INTEGER, LOSES INTEGER, KILLS INTEGER, FINAL_KILLS INTEGER, DEATHS INTEGER, FINAL_DEATHS INTEGER, BEDS_DESTROYED INTEGER, PLAYTIME INTEGER, LEVELS INTEGER, EXPERIENCE INTEGER)");
					}
				}
			}
			statement.close();
			if (canUpdate) {
				enabled = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean canMessage = true;
	private boolean canUpdate =true;
	private boolean checkConversion(DatabaseMetaData metadata, String table, String column) throws SQLException {
		boolean pred = !metadata.getColumns(null, null, table, column).next();
		if (pred && !mysql) {
			if (canMessage) {
				Bukkit.getConsoleSender().sendMessage("§cYou must delete the sqlite database because is outdated!");
				canMessage = false;
				canUpdate = false;
			}
			return false;
		}
		if (pred && canMessage) {
			Bukkit.getConsoleSender().sendMessage("§cThe database requires a conversion, please wait...");
			canMessage = false;
		}
		return pred;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public int getRefreshInTicks() {
		return refreshInTicks;
	}
	
	public void addData(SQLData data) {
		main.getExecutor().submit(new SQLDataRunnable(data));
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	private class SQLDataRunnable implements Runnable {
		
		private SQLData data;

		private SQLDataRunnable(SQLData data) {
			this.data = data;
		}
		
		@Override
		public void run() {
			try (Statement statement = connection.createStatement()) {
				if (mysql) {
					statement.executeUpdate("INSERT INTO "+data.getTypeName()+" (UUID, NAME, WINS, LOSES, KILLS, FINAL_KILLS, DEATHS, FINAL_DEATHS, BEDS_DESTROYED, PLAYTIME, LEVELS, EXPERIENCE) VALUES ('" + data.getUUID() + "', '" + data.getName() + "', " + data.getWin() + ", " + data.getLose() + ", "+data.getKills()+", "+data.getFinalKills()+", "+data.getDeaths()+", "+data.getFinalDeaths()+", "+data.getBedsDestroyed()+", "+data.getPlayTime()+", 1, 0) ON DUPLICATE KEY UPDATE NAME='" + data.getName() + "', WINS=WINS+" + data.getWin() + ", LOSES=LOSES+" + data.getLose() + ", KILLS=KILLS+" + data.getKills() + ", FINAL_KILLS=FINAL_KILLS+" + data.getFinalKills() + ", DEATHS=DEATHS+" + data.getDeaths() + ", FINAL_DEATHS=FINAL_DEATHS+" + data.getFinalDeaths() + ", BEDS_DESTROYED=BEDS_DESTROYED+" + data.getBedsDestroyed() + ", PLAYTIME=PLAYTIME+"+data.getPlayTime()+";");
				} else {
					statement.executeUpdate("INSERT OR IGNORE INTO "+data.getTypeName()+" (UUID, NAME, WINS, LOSES, KILLS, FINAL_KILLS, DEATHS, FINAL_DEATHS, BEDS_DESTROYED, PLAYTIME, LEVELS, EXPERIENCE) VALUES ('" + data.getUUID() + "', '" + data.getName() + "', 0, 0, 0, 0, 0, 0, 0, 0, 1, 0)");
					statement.executeUpdate("UPDATE "+data.getTypeName()+" SET NAME='" + data.getName() + "', WINS=WINS+" + data.getWin() + ", LOSES=LOSES+" + data.getLose() + ", KILLS=KILLS+" + data.getKills() + ", FINAL_KILLS=FINAL_KILLS+" + data.getFinalKills() + ", DEATHS=DEATHS+" + data.getDeaths() + ", FINAL_DEATHS=FINAL_DEATHS+" + data.getFinalDeaths() + ", BEDS_DESTROYED=BEDS_DESTROYED+" + data.getBedsDestroyed() + ", PLAYTIME=PLAYTIME+"+data.getPlayTime()+" WHERE UUID='"+data.getUUID()+"';");
				}
				try (ResultSet result = statement.executeQuery("SELECT LEVELS, EXPERIENCE FROM " + data.getTypeName() + " WHERE UUID = '" + data.getUUID() + "' LIMIT 1;")) {
					if (result.next()) {
						int levels = result.getInt(1);
						int experience = result.getInt(2) + data.getXp();

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
						while (experience >= experienceReq) {
							levels++;
							experience -= experienceReq;
						}
						statement.executeUpdate("UPDATE " + data.getTypeName() + " SET LEVELS=" + levels + ", EXPERIENCE=" + experience + " WHERE UUID='" + data.getUUID() + "';");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
