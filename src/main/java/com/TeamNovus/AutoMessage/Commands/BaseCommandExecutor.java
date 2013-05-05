package com.TeamNovus.AutoMessage.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.TeamNovus.AutoMessage.AutoMessage;

public class BaseCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.GOLD + "<>---------------[ " + ChatColor.DARK_RED + "AutoMessage" + ChatColor.GOLD + " ]---------------<>");
			sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.DARK_RED + AutoMessage.getPlugin().getDescription().getDescription());
			sender.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.DARK_RED + AutoMessage.getPlugin().getDescription().getAuthors().get(0));
			sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.DARK_RED + AutoMessage.getPlugin().getDescription().getVersion());
			sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.DARK_RED + AutoMessage.getPlugin().getDescription().getWebsite());
			sender.sendMessage(ChatColor.DARK_RED + "For help type: " + ChatColor.GOLD + "/am help [Page]");
			return true;
		}
		
		if(AutoMessage.getCommandManager().getCommand(args[0]) == null) {
			sender.sendMessage(ChatColor.RED + "The specified command was not found!");
			return true;
		}
		
		BaseCommand command = AutoMessage.getCommandManager().getCommand(args[0]);

		if((args.length < command.min()) || (args.length > command.max() && command.max() != -1)) {
			sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " " + command.aliases()[0] + " " + command.usage());
			return true;
		}
		
		AutoMessage.getCommandManager().dispatchCommand(command, sender, cmd, commandLabel, args);
		return true;
	}
}
