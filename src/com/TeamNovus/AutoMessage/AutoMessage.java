package com.TeamNovus.AutoMessage;

import java.io.File;

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
		getServer().getScheduler().cancelTasks(this);
		
		// Nullify all static variables
		plugin = null;
		messageListManager = null;		
	}

	public void reloadConfiguration() {
			if(!(new File(getDataFolder() + File.separator + "config.yml").exists())) {
				saveDefaultConfig();
			}
			
			try {
				reloadConfig();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			messageListManager.clear();
			for(String key : getConfig().getConfigurationSection("message-lists").getKeys(false)) {
				MessageList list = new MessageList();
				list.setEnabled(getConfig().getBoolean("message-lists." + key + ".enabled"));
				list.setInterval(getConfig().getInt("message-lists." + key + ".interval"));
				list.setRandom(getConfig().getBoolean("message-lists." + key + ".random"));
				list.setPrefix(getConfig().getString("message-lists." + key + ".prefix"));
				list.setMessages(getConfig().getStringList("message-lists." + key + ".messages"));
				messageListManager.setList(key, list);
			}
			messageListManager.schedule();
	}
	
	public void saveConfiguration() {
		if(!(new File(getDataFolder() + File.separator + "config.yml").exists())) {
			saveDefaultConfig();
		}
		
		for(String key : messageListManager.getMessageLists().keySet()) {
			MessageList list = messageListManager.getExactList(key);
			getConfig().set("message-lists." + key + ".enabled", list.isEnabled());
			getConfig().set("message-lists." + key + ".interval", list.getInterval());
			getConfig().set("message-lists." + key + ".random", list.isRandom());
			getConfig().set("message-lists." + key + ".prefix", list.getPrefix());
			getConfig().set("message-lists." + key + ".messages", list.getMessages());
		}
		saveConfig();
	}
	
	public static AutoMessage getPlugin() {
		return plugin;
	}
	
	public static MessageListManager getMessageListManager() {
		return messageListManager;
	}
}
