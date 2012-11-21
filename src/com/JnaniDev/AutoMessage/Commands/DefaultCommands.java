package com.JnaniDev.AutoMessage.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DefaultCommands {

	@BaseCommand(aliases = { "help", "?" }, description = "Learn about the commands and their usage!", usage = "help [Page]")
	public void onHelpCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.AQUA + "/sn convert <Race> " + ChatColor.GOLD + "- Convert to a race.");
		sender.sendMessage(ChatColor.AQUA + "/sn races " + ChatColor.GOLD + "- List availiable races.");
		sender.sendMessage(ChatColor.AQUA + "/sn race <Race> " + ChatColor.GOLD + "- View generated info about a race.");
		sender.sendMessage(ChatColor.AQUA + "/sn spell <Spell> " + ChatColor.GOLD + "- View generated info about a spell.");
		sender.sendMessage(ChatColor.AQUA + "/sn power " + ChatColor.GOLD + "- View your power.");
		sender.sendMessage(ChatColor.AQUA + "/sn online " + ChatColor.GOLD + "- View online players and their race.");
		sender.sendMessage(ChatColor.AQUA + "/sn tutorial [Chapter] " + ChatColor.GOLD + "- View the tutorial.");
	}

}
