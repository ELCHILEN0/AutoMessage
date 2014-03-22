package com.TeamNovus.AutoMessage;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.TeamNovus.AutoMessage.Util.Metrics;
import com.TeamNovus.AutoMessage.Commands.DefaultCommands;
import com.TeamNovus.AutoMessage.Commands.PluginCommands;
import com.TeamNovus.AutoMessage.Commands.Common.BaseCommandExecutor;
import com.TeamNovus.AutoMessage.Commands.Common.CommandManager;
import com.TeamNovus.AutoMessage.Models.Message;
import com.TeamNovus.AutoMessage.Models.MessageList;
import com.TeamNovus.AutoMessage.Models.MessageLists;

public class AutoMessage extends JavaPlugin {
	private static AutoMessage plugin;

	@Override
	public void onEnable() {
		plugin = this;

		// Setup the base command.
		getCommand("automessage").setExecutor(new BaseCommandExecutor());

		// Register additional commands.
		CommandManager.register(DefaultCommands.class);
		CommandManager.register(PluginCommands.class);

		// Load the configuration.
		loadConfig();

		// Start metrics.
		try {
			Metrics metrics = new Metrics(this);

			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		saveConfiguration();
		MessageLists.unschedule();

		plugin = null;
	}

	public void loadConfig() {
		if(!(new File(getDataFolder() + File.separator + "config.yml").exists())) {
			saveDefaultConfig();
		}

		try {
			reloadConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}

		MessageLists.clear();

		for(String key : getConfig().getConfigurationSection("message-lists").getKeys(false)) {
			MessageList list = new MessageList();

			if(getConfig().contains("message-lists." + key + ".enabled"))
				list.setEnabled(getConfig().getBoolean("message-lists." + key + ".enabled"));

			if(getConfig().contains("message-lists." + key + ".interval"))
				list.setInterval(getConfig().getInt("message-lists." + key + ".interval"));

			if(getConfig().contains("message-lists." + key + ".expiry"))
				list.setExpiry(getConfig().getLong("message-lists." + key + ".expiry"));

			if(getConfig().contains("message-lists." + key + ".random"))
				list.setRandom(getConfig().getBoolean("message-lists." + key + ".random"));

			LinkedList<Message> finalMessages = new LinkedList<Message>();

			if(getConfig().contains("message-lists." + key + ".messages")) {
				List<String> messages = getConfig().getStringList("message-lists." + key + ".messages");
				
				for (String m : messages) {
					finalMessages.add(new Message(m));
				}
			}

			list.setMessages(finalMessages);

			MessageLists.setList(key, list);
		}

		MessageLists.schedule();

		// Will the conversions to the disk.
		saveConfiguration();
	}

	public void saveConfiguration() {
		if(!(new File(getDataFolder() + File.separator + "config.yml").exists())) {
			saveDefaultConfig();
		}

		for(String key : getConfig().getConfigurationSection("message-lists").getKeys(false)) {
			getConfig().set("message-lists." + key, null);
		}

		for(String key : MessageLists.getMessageLists().keySet()) {
			MessageList list = MessageLists.getExactList(key);
			getConfig().set("message-lists." + key + ".enabled", list.isEnabled());
			getConfig().set("message-lists." + key + ".interval", list.getInterval());
			getConfig().set("message-lists." + key + ".expiry", list.getExpiry());
			getConfig().set("message-lists." + key + ".random", list.isRandom());
			
			List<String> messages = new LinkedList<String>();
			
			for(Message m : list.getMessages()) {
				messages.add(m.getMessage());
			}

			getConfig().set("message-lists." + key + ".messages", messages);
		}

		saveConfig();
	}

	public static AutoMessage getPlugin() {
		return plugin;
	}

	public File getFile() {
		return super.getFile();
	}
}
