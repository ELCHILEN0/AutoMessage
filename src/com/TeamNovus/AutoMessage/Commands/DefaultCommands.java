package com.TeamNovus.AutoMessage.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.TeamNovus.AutoMessage.AutoMessage;

public class DefaultCommands {

	@BaseCommand(aliases = { "help", "?" }, description = "View all commands and their info.", usage = "[Page]", hidden = true)
	public void helpCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "<>---------------[ " + ChatColor.DARK_RED + "AutoMessage" + ChatColor.GOLD + " ]---------------<>");
		sender.sendMessage(ChatColor.GRAY + "Required: < > Optional: [ ]");
		for(BaseCommand command : AutoMessage.getCommandManager().getCommands()) {
			if(!(command.hidden())) {
					sender.sendMessage(ChatColor.GOLD + "- " + "/" + commandLabel + " " + command.aliases()[0] + (command.usage() != "" ? " " + command.usage() : "") + ChatColor.DARK_RED + " - " + ChatColor.GOLD + command.description());
			}
		}
		sender.sendMessage(ChatColor.DARK_RED + "For help type: " + ChatColor.GOLD + "/am help [Page]");
	}
	
}
