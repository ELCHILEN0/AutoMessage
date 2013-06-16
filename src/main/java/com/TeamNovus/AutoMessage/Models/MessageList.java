package com.TeamNovus.AutoMessage.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
	
	private transient int currentIndex = 0;
	
	public MessageList() {
	    messages.add(new String("This is a &amessage&r in a &amessage-list&r!"));
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	public long getExpiry() {
		return expiry;
	}
	
	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}
	
	public boolean isExpired() {
		return System.currentTimeMillis() >= expiry && expiry != -1;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean random) {
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
	
	public boolean hasMessages() {
		return messages.size() > 0;
	}
	
	public void setCurrentIndex(int index) {
		this.currentIndex = index;
		
		if(currentIndex >= messages.size() || currentIndex < 0) {
			this.currentIndex = 0;
		}
	}
	
	public int getCurrentIndex() {
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
					if(message.contains("{NAME}"))
						message = message.replace("{NAME}", 		((Player) to).getName());
					if(message.contains("{DISPLAY_NAME}"))
						message = message.replace("{DISPLAY_NAME}", ((Player) to).getDisplayName());
					if(message.contains("{WORLD}"))
						message = message.replace("{WORLD}", 		((Player) to).getWorld().getName());
					if(message.contains("{BIOME}"))
						message = message.replace("{BIOME}", 		((Player) to).getLocation().getBlock().getBiome().toString());	
				} else if(to instanceof ConsoleCommandSender) {
					if(message.contains("{NAME}"))
						message = message.replace("{NAME}", 		to.getName());
					if(message.contains("{DISPLAY_NAME}"))
						message = message.replace("{DISPLAY_NAME}", to.getName());
					if(message.contains("{WORLD}"))
						message = message.replace("{WORLD}", 		"UNKNOWN");
					if(message.contains("{BIOME}"))
						message = message.replace("{BIOME}", 		"UNKNOWN");						
				}
				
				if(message.contains("{ONLINE}"))
					message = message.replace("{ONLINE}", 		Bukkit.getServer().getOnlinePlayers().length + "");
				if(message.contains("{MAX_ONLINE}"))
					message = message.replace("{MAX_ONLINE}", 	Bukkit.getServer().getMaxPlayers() + "");
				if(message.contains("{UNIQUE_PLAYERS}"))
					message = message.replace("{UNIQUE_PLAYERS}", Bukkit.getServer().getOfflinePlayers().length + "");
				
				if(message.contains("{YEAR}"))
					message = message.replace("{YEAR}", Calendar.getInstance().get(Calendar.YEAR) + "");
				if(message.contains("{MONTH}"))
					message = message.replace("{MONTH}", Calendar.getInstance().get(Calendar.MONTH) + "");
				if(message.contains("{MONTH}"))
					message = message.replace("{MONTH}", Calendar.getInstance().get(Calendar.MONTH) + "");
				if(message.contains("{DAY_OF_WEEK}"))
					message = message.replace("{DAY_OF_WEEK}", Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + "");
				if(message.contains("{DAY_OF_MONTH}"))
					message = message.replace("{DAY_OF_MONTH}", Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "");
				if(message.contains("{DAY_OF_YEAR}"))
					message = message.replace("{DAY_OF_YEAR}", Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + "");
				if(message.contains("{HOUR}"))
					message = message.replace("{HOUR}", Calendar.getInstance().get(Calendar.HOUR) + "");
				if(message.contains("{HOUR_OF_DAY}"))
					message = message.replace("{HOUR_OF_DAY}", Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "");
				if(message.contains("{MINUTE}"))
					message = message.replace("{MINUTE}", Calendar.getInstance().get(Calendar.MINUTE) + "");
				if(message.contains("{SECOND}"))
					message = message.replace("{SECOND}", Calendar.getInstance().get(Calendar.SECOND) + "");
				
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
