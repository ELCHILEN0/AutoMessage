package com.JnaniDev.AutoMessage;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import com.JnaniDev.AutoMessage.Commands.BaseCommandExecutor;
import com.JnaniDev.AutoMessage.Managers.CommandManager;
import com.JnaniDev.AutoMessage.Managers.MessageListManager;
import com.JnaniDev.AutoMessage.Models.MessageList;

public class AutoMessage extends JavaPlugin {
	private static AutoMessage plugin;
	private static CommandManager commandManager;
	private static MessageListManager messageListManager;
	
	@Override
	public void onEnable() {
		plugin = this;
		commandManager = new CommandManager();
		messageListManager = new MessageListManager();
		
		setupMetrics();
		reloadConfiguration();
		getCommand("automessage").setExecutor(new BaseCommandExecutor());		
	}

	@Override
	public void onDisable() {
		plugin = null;
		commandManager = null;
		messageListManager = null;
	}
	
	public void setupMetrics() {
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveConfiguration() {
		for(String key : AutoMessage.getMessageListManager().getLists().keySet()) {
			getConfig().set("message-lists" + key + ".interval", messageListManager.getList(key).getInterval());
			getConfig().set("message-lists" + key + ".random", messageListManager.getList(key).isRandom());
			getConfig().set("message-lists" + key + ".prefix", messageListManager.getList(key).getPrefix());
			getConfig().set("message-lists" + key + ".messages", messageListManager.getList(key).getMessages());
		}

		saveConfig();
	}

	public void reloadConfiguration() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
				
		try {
			reloadConfig();
			
			Map<String, Object> values = getConfig().getConfigurationSection("message-lists").getValues(false);			
			for(String key : values.keySet()) {
				MessageList list = new MessageList();
				list.setInterval(getConfig().getInt("message-lists" + key + ".interval"));
				list.setRandom(getConfig().getBoolean("message-lists" + key + ".random"));
				list.setPrefix(getConfig().getString("message-lists" + key + ".prefix"));
				list.setMessages(getConfig().getStringList("message-lists" + key + ".messages"));
				messageListManager.putList(key, new MessageList());
			}
		} catch (Exception e) {
			getLogger().warning("There was an error reading the config.  See stacktrace below.  Disabling plugin...");
			e.printStackTrace();
			setEnabled(false);
		}
	}
	
	public static AutoMessage getPlugin() {
		return plugin;
	}
	
	public static CommandManager getCommandManager() {
		return commandManager;
	}

	public static MessageListManager getMessageListManager() {
		return messageListManager;
	}
}
