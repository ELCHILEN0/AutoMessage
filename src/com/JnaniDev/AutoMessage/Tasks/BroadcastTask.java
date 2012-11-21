package com.JnaniDev.AutoMessage.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.JnaniDev.AutoMessage.AutoMessage;
import com.JnaniDev.AutoMessage.Models.MessageList;

public class BroadcastTask implements Runnable {
	private String key;
	
	public BroadcastTask(String key) {
		this.key = key;
	}
	
	public void run() {
		if(AutoMessage.getMessageListManager() == null) return;
		
		MessageList list = AutoMessage.getMessageListManager().getList(key);
		
		if(list.getMessage() == null) return;
		
		String[] split = list.getMessage().split("\\\\n");
		for (int i = 0; i < split.length; i++) {
			String line = split[i];
			if(line.startsWith("/")) {
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), line.replaceFirst("/", ""));
			} else {
				if(line.equals(split[0])) {
					line = list.getPrefix() + " " + line;
				}
				broadcast(ChatColor.translateAlternateColorCodes("&".charAt(0), line));
			}
		}
		list.setCurrentMessage(list.getCurrentMessage() + 1);
	}
	
	public void broadcast(String message) {
		if(AutoMessage.getPlugin().getConfig().getBoolean("settings.enabled")) {
			if((Bukkit.getServer().getOnlinePlayers().length > AutoMessage.getPlugin().getConfig().getInt("settings.min-players"))) {
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					if(player.hasPermission(String.format("automessage.recieve.%s", key))) {
						player.sendMessage(parseVariables(message, player));
					}
				}
				
				if(AutoMessage.getPlugin().getConfig().getBoolean("settings.log-to-console")) {
					Bukkit.getServer().getConsoleSender().sendMessage(parseVariables(message, Bukkit.getServer().getConsoleSender()));
				}
			}
		}
	}

	public String parseVariables(String message, CommandSender reciever) {
		message = message.replaceAll("\\{onlinecount\\}", String.valueOf(Bukkit.getServer().getOnlinePlayers().length));
		message = message.replaceAll("\\{maxplayers\\}", String.valueOf(Bukkit.getServer().getMaxPlayers()));
		message = message.replaceAll("\\{servername\\}", String.valueOf(Bukkit.getServer().getName()));
		message = message.replaceAll("\\{servermotd\\}", String.valueOf(Bukkit.getServer().getMotd()));
		message = message.replaceAll("\\{serverip\\}", String.valueOf(Bukkit.getServer().getIp()));
		message = message.replaceAll("\\{serverport\\}", String.valueOf(Bukkit.getServer().getPort()));
		message = message.replaceAll("\\{playername\\}", String.valueOf(reciever.getName()));
		return message;
	}
	
}
