package com.TeamNovus.AutoMessage.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MessageList {
	private boolean enabled = true;
	private int interval = 45;
	private long expiry = -1L;
	private boolean random = false;
	private String prefix = "[&bPrefix&r] ";
	private String suffix = "[&4Suffix&r]";
	private List<String> messages = new ArrayList<String>();
	
	private Integer currentIndex = 0;
	
	public Boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	
	public Long getExpiry() {
		return expiry;
	}
	
	public void setExpiry(Long expiry) {
		this.expiry = expiry;
	}
	
	public Boolean isExpired() {
		return System.currentTimeMillis() >= expiry && expiry != -1;
	}

	public Boolean isRandom() {
		return random;
	}

	public void setRandom(Boolean random) {
		this.random = random;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	public void addMessage(String message) {
		this.messages.add(message);
	}
	
	public String getMessage(Integer index) {
		try {
			return this.messages.get(index.intValue());
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void addMessage(Integer index, String message) {
		try {
			this.messages.add(index.intValue(), message);
		} catch (IndexOutOfBoundsException e) {
			this.messages.add(message);
		}
	}
	
	public boolean editMessage(Integer index, String message) {
		try {
			return this.messages.set(index.intValue(), message) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public boolean removeMessage(Integer index) {
		try {
			return this.messages.remove(index.intValue()) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public String getCurrentMessage() {
		if(currentIndex >= messages.size()) {
			currentIndex = 0;
		}
		
		if(random) {
			currentIndex = new Random().nextInt(messages.size());
		}
		
		try {
			return messages.get(currentIndex);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void setCurrentIndex(Integer currentIndex) {
		this.currentIndex = currentIndex;
	}
	
	public Integer getCurrentIndex() {
		return currentIndex;
	}
	
	public void broadcast(int index) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			broadcastTo(index, player);
		}
		
		broadcastTo(index, Bukkit.getConsoleSender());
	}
	
	public void broadcastTo(int index, CommandSender to) {
		if(getMessage(index) != null) {
			String[] lines = getMessage(index).split("\\\\n");

			List<String> messages = new ArrayList<String>();
			List<String> commands = new ArrayList<String>();
			
			for (String line : lines) {
				if(line.startsWith("/")) {
					commands.add(line.replaceFirst("/", ""));
				} else {
					messages.add(line);
				}
			}
			
			if(messages.size() >= 1) {
				messages = Arrays.asList((getPrefix() + StringUtils.join(messages.toArray(), "\n") + getSuffix()).split("\\\\n"));
			}
			
			for(String message : messages) {
				if(to instanceof Player) {
					message = message.replace("{NAME}", 			((Player) to).getName());
					message = message.replace("{DISPLAYNAME}", 		((Player) to).getDisplayName());
					message = message.replace("{WORLD}", 			((Player) to).getWorld().getName());
					message = message.replace("{BIOME}", 			((Player) to).getLocation().getBlock().getBiome().toString());
					message = message.replace("{ONLINE}", 			Bukkit.getServer().getOnlinePlayers().length + "");
					message = message.replace("{MAX_ONLINE}", 		Bukkit.getServer().getMaxPlayers() + "");						
				} else if(to instanceof ConsoleCommandSender) {
					message = message.replace("{NAME}", 			"CONSOLE");
					message = message.replace("{DISPLAYNAME}", 		"CONSOLE");
					message = message.replace("{WORLD}", 			"UNKNOWN");
					message = message.replace("{BIOME}", 			"UNKNOWN");
					message = message.replace("{ONLINE}", 			Bukkit.getServer().getOnlinePlayers().length + "");
					message = message.replace("{MAX_ONLINE}", 		Bukkit.getServer().getMaxPlayers() + "");						
				}
				
				to.sendMessage(ChatColor.translateAlternateColorCodes("&".charAt(0), message));
			}
			
			for(String command : commands) {
				if(to instanceof ConsoleCommandSender) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
				}
			}
		}
	}
}
