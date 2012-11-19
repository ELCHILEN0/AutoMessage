package com.JnaniDev.AutoMessage;

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
			return true;
		} else if(args[0].equalsIgnoreCase("reload")) {
			if(sender.hasPermission("automessage.reload")) {
				plugin.reloadConfiguration();
				sender.sendMessage(ChatColor.GREEN + "Configuration Reloaded Sucessfully!");
			}
			return true;
		} else {
			return false;
		}
	}
}
