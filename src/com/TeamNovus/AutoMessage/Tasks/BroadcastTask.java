package com.TeamNovus.AutoMessage.Tasks;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Models.MessageList;

public class BroadcastTask implements Runnable {
	private Entry<String, MessageList> entry;
	
	public BroadcastTask(Entry<String, MessageList> entry) {
		this.entry = entry;
	}

	@Override
	public void run() {
		if(entry != null && AutoMessage.getPlugin().getConfig().getBoolean("settings.enabled")) {
			MessageList list = entry.getValue();
			if(list.getMessages().size() > 0 && list.getCurrentMessage() != null && list.isEnabled()) {
				if(Bukkit.getServer().getOnlinePlayers().length >= AutoMessage.getPlugin().getConfig().getInt("settings.min-players")) {
					String[] lines = list.getCurrentMessage().split("\\\\n");
					for(int i = 0; i < lines.length; i++) {
						if(lines[i].startsWith("/")) {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), lines[i].replaceFirst("/", ""));
						} else {
							if(i == 0) {
								lines[i] = list.getPrefix() + lines[i];
							}
																					
							for(Player p : Bukkit.getServer().getOnlinePlayers()) {
								if(p.hasPermission("automessage.receive." + entry.getKey())) {
									p.sendMessage(parsePlayer(p, lines[i]));
								}
							}
							
							if(AutoMessage.getPlugin().getConfig().getBoolean("settings.log-to-console")) {
								Bukkit.getConsoleSender().sendMessage(parseConsole(lines[i]));
							}
						}
					}
					
					list.setCurrentIndex(list.getCurrentIndex() + 1);
				}
			}
		}
	}
	
	public static String parsePlayer(Player reciever, String message) {
		message = message.replace("{NAME}", reciever.getName());
		message = message.replace("{DISPLAYNAME}", reciever.getDisplayName());
		message = message.replace("{WORLD}", reciever.getWorld().getName());
		message = message.replace("{BIOME}", reciever.getLocation().getBlock().getBiome().toString());
		message = message.replace("{ONLINE}", Bukkit.getServer().getOnlinePlayers().length + "");
		message = message.replace("{MAX_ONLINE}", Bukkit.getServer().getMaxPlayers() + "");
		message = ChatColor.translateAlternateColorCodes("&".charAt(0), message);
		return message;
	}
	
	public static String parseConsole(String message) {
		message = message.replace("{NAME}", "CONSOLE");
		message = message.replace("{DISPLAYNAME}", "CONSOLE");
		message = message.replace("{WORLD}", "UNKNOWN");
		message = message.replace("{BIOME}", "UNKNOWN");
		message = message.replace("{ONLINE}", Bukkit.getServer().getOnlinePlayers().length + "");
		message = message.replace("{MAX_ONLINE}", Bukkit.getServer().getMaxPlayers() + "");
		message = ChatColor.translateAlternateColorCodes("&".charAt(0), message);
		return message;
	}
}
