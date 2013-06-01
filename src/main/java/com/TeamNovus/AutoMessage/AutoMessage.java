package com.TeamNovus.AutoMessage;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.TeamNovus.AutoMessage.Util.Metrics;
import com.TeamNovus.AutoMessage.Util.Updater;
import com.TeamNovus.AutoMessage.Util.Updater.UpdateResult;
import com.TeamNovus.AutoMessage.Util.Updater.UpdateType;
import com.TeamNovus.AutoMessage.Commands.DefaultCommands;
import com.TeamNovus.AutoMessage.Commands.PluginCommands;
import com.TeamNovus.AutoMessage.Commands.Core.BaseCommandExecutor;
import com.TeamNovus.AutoMessage.Commands.Core.CommandManager;
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
		
		// Check for updates.
		if(getConfig().getBoolean("settings.auto-update")) {
			getLogger().info("Auto-update enabled!  Checking for updates...");
			
			UpdateResult result = new Updater(this, "automessage", this.getFile(), UpdateType.NO_DOWNLOAD, false).getResult();
			switch (result) {
			case UPDATE_AVAILABLE:
				getLogger().info("An update is available for a download! Automatically downloading...");
				new Updater(this, "automessage", this.getFile(), UpdateType.DEFAULT, true);
				getLogger().info("Update downloaded and installed! Restart server for changes to take effect!");
				break; 

			default:
				getLogger().info("There are no availiable updates for download.");
				break;
			}
		}
		
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
				
				if(getConfig().contains("message-lists." + key + ".prefix"))
					list.setPrefix(getConfig().getString("message-lists." + key + ".prefix"));

				if(getConfig().contains("message-lists." + key + ".suffix"))
					list.setSuffix(getConfig().getString("message-lists." + key + ".suffix"));
				
				if(getConfig().contains("message-lists." + key + ".messages"))
					list.setMessages(getConfig().getStringList("message-lists." + key + ".messages"));
				
				MessageLists.setList(key, list);
			}
			
			MessageLists.schedule();
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
			getConfig().set("message-lists." + key + ".prefix", list.getPrefix());
			getConfig().set("message-lists." + key + ".suffix", list.getSuffix());
			getConfig().set("message-lists." + key + ".messages", list.getMessages());
		}
		saveConfig();
	}
	
	public static AutoMessage getPlugin() {
		return plugin;
	}
}
