package com.JnaniDev.AutoMessage;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class AMCommandExecutor implements CommandExecutor {
	private AutoMessage plugin;

	public AMCommandExecutor(AutoMessage plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if((args.length == 0) || (args[0].equalsIgnoreCase("info"))) {
			sender.sendMessage(ChatColor.DARK_RED + "<>---------------[ " + ChatColor.GOLD + "AutoMessage" + ChatColor.DARK_RED + " ]---------------<>");
		     sender.sendMessage(ChatColor.DARK_RED + "About: " + ChatColor.GOLD + plugin.getDescription().getDescription());
		     sender.sendMessage(ChatColor.DARK_RED + "Author: " + ChatColor.GOLD + plugin.getDescription().getAuthors().get(0));
		     sender.sendMessage(ChatColor.DARK_RED + "Website: " + ChatColor.GOLD + plugin.getDescription().getWebsite());
		     sender.sendMessage(ChatColor.DARK_RED + "Version: " + ChatColor.GOLD + plugin.getDescription().getVersion());
		     sender.sendMessage(ChatColor.DARK_RED + "See all commands by doing " + ChatColor.GOLD + "/" + commandLabel + " help" + ChatColor.DARK_RED + ".");
		     return true;
		}

		if(args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(ChatColor.DARK_RED + "<>---------------[ " + ChatColor.GOLD + "AutoMessage" + ChatColor.DARK_RED + " ]---------------<>");
		    sender.sendMessage(ChatColor.GRAY + "Required Values: < > Optional Values: [ ]");
			sender.sendMessage(ChatColor.DARK_RED + " - " + ChatColor.GOLD + "/" + commandLabel + " reload");
			sender.sendMessage(ChatColor.DARK_RED + " - " + ChatColor.GOLD + "/" + commandLabel + " settings enabled [true/false]");
			sender.sendMessage(ChatColor.DARK_RED + " - " + ChatColor.GOLD + "/" + commandLabel + " settings log-to-console [true/false]");
			sender.sendMessage(ChatColor.DARK_RED + " - " + ChatColor.GOLD + "/" + commandLabel + " settings pause-on-empty [true/false]");
			sender.sendMessage(ChatColor.DARK_RED + " - " + ChatColor.GOLD + "/" + commandLabel + " settings auto-update [true/false]");
			return true;
		} else if(args[0].equalsIgnoreCase("reload")) {
			plugin.reloadConfiguration();
			sender.sendMessage(ChatColor.GREEN + "Configuration reloaded!");
			return true;
		} else if(args[0].equalsIgnoreCase("settings")) {
			if(args.length > 1) {
				if(args[1].equalsIgnoreCase("enabled")) {
					return changeSettingsKey("enabled", sender, commandLabel, args);
				} else if(args[1].equalsIgnoreCase("log-to-console")) {
					return changeSettingsKey("log-to-console", sender, commandLabel, args);
				} else if(args[1].equalsIgnoreCase("pause-on-empty")) {
					return changeSettingsKey("pause-on-empty", sender, commandLabel, args);
				} else if(args[1].equalsIgnoreCase("auto-update")) {
					return changeSettingsKey("auto-update", sender, commandLabel, args);
				}
			}
		}

		sender.sendMessage(ChatColor.RED + "No command was found by this name.");
		return false;
	}
	
	public boolean changeSettingsKey(String key, CommandSender sender, String commandLabel, String[] args) {
		String node = String.format("settings.%s", key);
		String permission = String.format("automessage.config.%s", key);
		
		if(!(sender.hasPermission(permission))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
			return false;
		}
		
		if(args.length > 2) {
			plugin.getConfig().set(node, Boolean.valueOf(args[1]));
			sender.sendMessage(ChatColor.GREEN + WordUtils.capitalize(key) + ": " + plugin.getConfig().getBoolean(node));
			return true;
		}
		
		plugin.getConfig().set(node, !(plugin.getConfig().getBoolean(node)));
		sender.sendMessage(ChatColor.GREEN + WordUtils.capitalize(key) + ": " + plugin.getConfig().getBoolean(node));
		plugin.saveConfig();
		return true;
	}
}
