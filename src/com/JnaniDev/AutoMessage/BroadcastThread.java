package com.JnaniDev.AutoMessage;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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
			if(line.startsWith("/")) {
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), line.replaceFirst("/", ""));
			} else {
				if(line.equals(list.getCurrentMessage()[0])) {
					line = list.getPrefix() + line;
				}
				broadcast(ChatColor.translateAlternateColorCodes("&".charAt(0), line));
			}
		}
		list.setMessageIndex(list.getMessageIndex() + 1);
	}
	
	public void broadcast(String message) {
		if(plugin.getConfig().getBoolean("settings.enabled")) {
			if((plugin.getServer().getOnlinePlayers().length > plugin.getConfig().getInt("settings.min-players"))) {
				for(Player player : plugin.getServer().getOnlinePlayers()) {
					if(player.hasPermission(String.format("automessage.recieve.%s", key))) {
						player.sendMessage(parseVariables(message, player));
					}
				}
				
				if(plugin.getConfig().getBoolean("settings.log-to-console")) {
					plugin.getServer().getConsoleSender().sendMessage(parseVariables(message, plugin.getServer().getConsoleSender()));
				}
			}
		}
	}

	public String parseVariables(String message, CommandSender reciever) {
		message = message.replaceAll("\\{onlinecount\\}", String.valueOf(plugin.getServer().getOnlinePlayers().length));
		message = message.replaceAll("\\{maxplayers\\}", String.valueOf(plugin.getServer().getMaxPlayers()));
		message = message.replaceAll("\\{servername\\}", String.valueOf(plugin.getServer().getName()));
		message = message.replaceAll("\\{servermotd\\}", String.valueOf(plugin.getServer().getMotd()));
		message = message.replaceAll("\\{serverip\\}", String.valueOf(plugin.getServer().getIp()));
		message = message.replaceAll("\\{serverport\\}", String.valueOf(plugin.getServer().getPort()));
		message = message.replaceAll("\\{playername\\}", String.valueOf(reciever.getName()));
		return message;
	}
	
}
