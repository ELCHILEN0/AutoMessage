package com.TeamNovus.AutoMessage.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DefaultCommands {

	@BaseCommand(aliases = { "help", "?" }, description = "View all commands and their info.", usage = "[Page]", hidden = true)
	public void helpCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		sender.sendMessage("Help Info Here!");
	}
	
}
