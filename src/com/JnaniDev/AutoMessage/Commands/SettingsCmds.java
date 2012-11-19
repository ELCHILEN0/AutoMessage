package com.JnaniDev.AutoMessage.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.JnaniDev.AutoMessage.AutoMessage;

public class SettingsCmds {
	private AutoMessage plugin;

	public SettingsCmds(AutoMessage plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String command = ChatColor.GRAY + "- " + ChatColor.GOLD + "/" + commandLabel + " %s ";
		
		if(args[0].equalsIgnoreCase("reload")) {
			if(sender.hasPermission("automessage.reload")) {
				plugin.reloadConfiguration();
				sender.sendMessage(ChatColor.GREEN + "Configuration Reloaded Sucessfully!");
			}
			return true;
		}
		
		if((args.length > 1) && (args[0].equalsIgnoreCase("settings"))  && (args[1].equalsIgnoreCase("enabled"))) {
			if(args.length > 2) {
				// Set to Value
				plugin.getConfig().set("settings.enabled", Boolean.valueOf(args[2]));
			} else {
				// Toggle
				plugin.getConfig().set("settings.enabled", !(plugin.getConfig().getBoolean("settings.enabled")));
			}
			sendMessage(sender, ChatColor.GREEN + "Enabled: " + ChatColor.YELLOW + String.valueOf(plugin.getConfig().getBoolean("settings.enabled")));
			plugin.saveConfiguration();
			return true;
		}
		
		if((args.length > 1) && (args[0].equalsIgnoreCase("settings"))  && (args[1].equalsIgnoreCase("min-players"))) {
			if(args.length > 2) {
				// Set to Value
				if(!(isInteger(args[2]))) {
					sendMessage(sender, ChatColor.RED + "The specified ammount is not an Integer!");
					return true;
				}
				
				plugin.getConfig().set("settings.min-players", Boolean.valueOf(args[2]));
			} else {
				// Display Usage
				sendMessage(sender, String.format(command, "settings min-players <MinPlayers>"));
			}
			sendMessage(sender, ChatColor.GREEN + "Auto Update: " + ChatColor.YELLOW + String.valueOf(plugin.getConfig().getInt("settings.min-players")));
			plugin.saveConfiguration();
			return true;
		}
		
		if((args.length > 1) && (args[0].equalsIgnoreCase("settings"))  && (args[1].equalsIgnoreCase("log-to-console"))) {
			if(args.length > 2) {
				// Set to Value
				plugin.getConfig().set("settings.log-to-console", Boolean.valueOf(args[2]));
			} else {
				// Toggle
				plugin.getConfig().set("settings.log-to-console", !(plugin.getConfig().getBoolean("settings.log-to-console")));
			}
			sendMessage(sender, ChatColor.GREEN + "Log To Console: " + ChatColor.YELLOW + String.valueOf(plugin.getConfig().getBoolean("settings.log-to-console")));
			plugin.saveConfiguration();
			return true;
		}
		
		if((args.length > 1) && (args[0].equalsIgnoreCase("settings"))  && (args[1].equalsIgnoreCase("auto-update"))) {
			if(args.length > 2) {
				// Set to Value
				plugin.getConfig().set("settings.auto-update", Boolean.valueOf(args[2]));
			} else {
				// Toggle
				plugin.getConfig().set("settings.auto-update", !(plugin.getConfig().getBoolean("settings.auto-update")));
			}
			sendMessage(sender, ChatColor.GREEN + "Auto Update: " + ChatColor.YELLOW + String.valueOf(plugin.getConfig().getBoolean("settings.auto-update")));
			plugin.saveConfiguration();
			return true;
		}
		
		return false;
	}	
	
	public boolean isInteger(String s) {
		try {
			Integer.valueOf(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public void sendMessage(CommandSender sender, String s) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes("&".charAt(0), s));
	}
}
