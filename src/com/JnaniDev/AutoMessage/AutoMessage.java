package com.JnaniDev.AutoMessage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class AutoMessage extends JavaPlugin {
	final Logger log = Logger.getLogger("Minecraft");
	public Map<String, MessageList> messageLists = new HashMap<String, MessageList>();
	
	public void onEnable() {
		long st = System.currentTimeMillis();
		// Begin enable code
		reloadConfiguration();
		// End enable code
		long et = System.currentTimeMillis();
		log.info(String.format("[%s] %s is now enabled! Took %sms.", getDescription().getName(), getDescription().getName(), (et-st)));
		
	}

	public void onDisable() {
		log.info("[AutoMessage] AutoMessage Disabled!");
	}
	
	public void setupMetrics() {
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			log.info(e.getMessage());
		}
	}

	public void saveConfiguration() {
		for(String key : messageLists.keySet()) {
			getConfig().set("message-lists." + key, messageLists.get(key).toMap());
		}

		saveConfig();
	}

	public void reloadConfiguration() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
		
		reloadConfig();
		
		// Set the message list to the new values
		messageLists.clear();
		Map<String, Object> values = getConfig().getConfigurationSection("message-lists").getValues(false);
		
		for(Entry<String, Object> value : values.entrySet()) {
			messageLists.put(value.getKey(), new MessageList(value.getValue()));
		}
		
		// Schedule all the message lists
		schedule();
	}
	
	// Schedule all the messages at their intervals.
	public void schedule() {
		getServer().getScheduler().cancelTasks(this);
		for(String key : messageLists.keySet()) {
			MessageList list = messageLists.get(key);
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new BroadcastThread(this, list, key),
																list.getInterval() * 20L,
																list.getInterval() * 20L);
		}
	}
}
