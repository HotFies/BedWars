package ro.Aneras.ClashWars.Handler.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import ro.Aneras.ClashWars.Main;

public class Configuration {

	private File file;
	private String name;
	private YamlConfiguration config;

	public Configuration(Main main, String name, boolean newFile, boolean databaseFile) {
		this.name = name;
		File d = null;
		if (databaseFile) {
			d = new File(main.getDataFolder(), "Database");
			d.mkdirs();
		}
		file = new File(databaseFile ? d : main.getDataFolder(), name+".yml");
		ConsoleCommandSender console = main.getServer().getConsoleSender();
		if (file.exists() && !databaseFile) {
			console.sendMessage("§a - Loading "+name+".yml...");
		} else {
			if (!databaseFile) {
				console.sendMessage("§a - Creating a new "+name+".yml");
			}
			if (newFile) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				main.saveResource(name+".yml", true);
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public String getName() {
		return name;
	}
	
	public long getLength() {
		return file.length();
	}
	
	public ConfigurationSection getConfigurationSection(String configuration) {
		return config.getConfigurationSection(configuration);
	}
	
	public List<String> getStringList(String path) {
		return config.getStringList(path);
	}
	
	public Object get(String path) {
		return config.get(path);
	}
	
	public int getInt(String path) {
		return config.getInt(path);
	}
	
	public String getString(String path) {
		return config.getString(path);
	}
	
	public void set(String path, Object value) {
		config.set(path, value);
	}
	
	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}

	public boolean isString(String path) {
		return config.isString(path);
	}
}
