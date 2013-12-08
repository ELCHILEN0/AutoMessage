package com.TeamNovus.AutoMessage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
				
				if(getConfig().contains("message-lists." + key + ".prefix"))
					list.setPrefix(getConfig().getString("message-lists." + key + ".prefix"));

				if(getConfig().contains("message-lists." + key + ".suffix"))
					list.setSuffix(getConfig().getString("message-lists." + key + ".suffix"));
								
				LinkedList<Message> finalMessages = new LinkedList<Message>();
			
				if(getConfig().contains("message-lists." + key + ".messages")) {
					if(getConfig().getList("message-lists." + key + ".messages") instanceof List) {
						List<?> tmp = getConfig().getList("message-lists." + key + ".messages");
							
						// Decide what format the list is in...
						if(tmp.get(0) instanceof String) {
							@SuppressWarnings("unchecked")
							List<String> messages = (List<String>) getConfig().getList("message-lists." + key + ".messages");

							for (String m : messages) {
								finalMessages.add(new Message(m));
							}		
						} else {
							@SuppressWarnings("unchecked")
							List<Map<String, List<String>>> messages = (List<Map<String, List<String>>>) ((Object) getConfig().getMapList("message-lists." + key + ".messages"));

							for(Map<String, List<String>> message : messages) {
								for(Entry<String, List<String>> entry : message.entrySet()) {
									Message m = new Message(entry.getKey());
																
									for(String s : entry.getValue()) {
										m.addArgument(s);
									}
									
									finalMessages.add(m);
								}
							}
						}
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
		
		// TODO: Remove in a few versions ;)
		if(getConfig().contains("settings.auto-update")) {
			getConfig().set("settings.auto-update", null);
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
			
			List<HashMap<String, List<String>>> messages = new LinkedList<HashMap<String,List<String>>>();
			for(Message m : list.getMessages()) {
				HashMap<String, List<String>> ms = new HashMap<String, List<String>>();
				
				ms.put(m.getFormat(), m.getArguments());
				messages.add(ms);
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
