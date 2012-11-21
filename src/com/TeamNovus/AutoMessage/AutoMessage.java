package com.TeamNovus.AutoMessage;

import java.io.File;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.TeamNovus.AutoMessage.Managers.MessageListManager;
import com.TeamNovus.AutoMessage.Models.MessageList;

public class AutoMessage extends JavaPlugin {
	private static AutoMessage plugin;
	private static MessageListManager messageListManager;
	
	@Override
	public void onEnable() {
		plugin = this;
		messageListManager = new MessageListManager();
		
		reloadConfiguration();
	}

	@Override
	public void onDisable() {
		messageListManager = null;
		
		getServer().getScheduler().cancelTasks(this);
		
		plugin = null;
	}

	public void reloadConfiguration() {
		try {
			if(!(new File(getDataFolder() + File.separator + "config.yml").exists())) {
				saveDefaultConfig();
			}
			reloadConfig();
			
			HashMap<String, MessageList> messageLists = new HashMap<String, MessageList>();
			for(String key : getConfig().getConfigurationSection("message-lists").getKeys(false)) {
				MessageList list = new MessageList();
				list.setEnabled(getConfig().getBoolean("message-lists." + key + ".enabled"));
				list.setInterval(getConfig().getInt("message-lists." + key + ".interval"));
				list.setRandom(getConfig().getBoolean("message-lists." + key + ".random"));
				list.setPrefix(getConfig().getString("message-lists." + key + ".prefix"));
				list.setMessages(getConfig().getStringList("message-lists." + key + ".messages"));
				messageLists.put(key, list);
			}
			messageListManager.setMessageLists(messageLists);
			messageListManager.schedule();
			
		} catch (Exception e) {
			getLogger().warning("There was an error reading the config!  Plugin automatically disabling...");
			getLogger().warning("Check your config for any errors with formatting.  See stack trace below.");
			e.printStackTrace();
			setEnabled(false);
		}
	}
	
	public static AutoMessage getPlugin() {
		return plugin;
	}
	
	public static MessageListManager getMessageListManager() {
		return messageListManager;
	}
}
