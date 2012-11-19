package com.JnaniDev.AutoMessage.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.JnaniDev.AutoMessage.AutoMessage;

public class DefaultCmds implements CommandExecutor {
	private AutoMessage plugin;

	public DefaultCmds(AutoMessage plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if((args.length == 0) || (args[0].equalsIgnoreCase("info"))) {
			sender.sendMessage(ChatColor.DARK_RED + "<>---------------[ " + ChatColor.GOLD + "AutoMessage" + ChatColor.DARK_RED + " ]---------------<>");
			sender.sendMessage(ChatColor.DARK_RED + "About: " + ChatColor.GOLD + plugin.getDescription().getDescription());
			sender.sendMessage(ChatColor.DARK_RED + "Author: " + ChatColor.GOLD + plugin.getDescription().getAuthors().get(0));
			sender.sendMessage(ChatColor.DARK_RED + "Version: " + ChatColor.GOLD + plugin.getDescription().getVersion());
			return true;
		}
		
		if((args.length > 0) && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))) {
			sender.sendMessage(ChatColor.DARK_RED + "<>---------------[ " + ChatColor.GOLD + "AutoMessage" + ChatColor.DARK_RED + " ]---------------<>");
			sender.sendMessage(ChatColor.GRAY + "Required: < > Optional: [ ]");
			String command = ChatColor.GRAY + "- " + ChatColor.GOLD + "/" + commandLabel + " %s ";
			switch (parsePage(args)) {
			case 0:
			case 1:
			default:
				sender.sendMessage(String.format(command, "add <MessageList> [Index] [Message]"));
				sender.sendMessage(String.format(command, "edit <MessageList> <Index> [Message]"));
				sender.sendMessage(String.format(command, "delete <MessageList> <Index>"));
				sender.sendMessage(String.format(command, "list [MessageList]"));
				sender.sendMessage(String.format(command, "broadcast <MessageList> <Index>"));
				sender.sendMessage(String.format(command, "interval <MessageList> <Interval>"));
				sender.sendMessage(ChatColor.GRAY + "For more help do " + ChatColor.GOLD + "/" + commandLabel + " help 2");
				break;
				
			case 2:
				sender.sendMessage(String.format(command, "settings enabled [True/False]"));
				sender.sendMessage(String.format(command, "settings min-players <MinPlayers>"));
				sender.sendMessage(String.format(command, "settings log-to-console [True/False]"));
				sender.sendMessage(String.format(command, "settings auto-update [True/False]"));
				sender.sendMessage(String.format(command, "reload"));
				sender.sendMessage(ChatColor.GRAY + "For more help do " + ChatColor.GOLD + "/" + commandLabel + " help 1");
				break;
			}
			return true;
		}
		return false;
	}
	
	public int parsePage(String[] args) {
		if(args.length > 1) {
			try {
				return Integer.parseInt(args[1]);
			} catch (Exception e) {
				return 1;
			}
		}
		return 1;
	}
}
