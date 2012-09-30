package com.JnaniDev.AutoMessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BroadcastThread implements Runnable {
	private AutoMessage plugin;
	private MessageList list;
	private String key;
	
	public BroadcastThread(AutoMessage plugin, MessageList list, String key) {
		this.plugin = plugin;
		this.list = list;
		this.key = key;
	}
	
	public void run() {
		for(String line : list.getCurrentMessage()) {
			if(line.equals(list.getCurrentMessage()[0])) {
				line = list.getPrefix() + line;
			}
			broadcast(ChatColor.translateAlternateColorCodes("&".charAt(0), line));
		}
		list.setMessageIndex(list.getMessageIndex() + 1);
	}
	
	public void broadcast(String message) {
		if(plugin.getConfig().getBoolean("settings.enabled")) {
			if(!(plugin.getConfig().getBoolean("settings.pause-on-empty")) || (plugin.getServer().getOnlinePlayers().length > 0)) {
				for(Player player : plugin.getServer().getOnlinePlayers()) {
					if(player.hasPermission(String.format("automessage.recieve.%s", key))) {
						player.sendMessage(message);
					}
				}
				
				if(plugin.getConfig().getBoolean("settings.log-to-console")) {
					plugin.getServer().getConsoleSender().sendMessage(message);
				}
			}
		}
	}

}
