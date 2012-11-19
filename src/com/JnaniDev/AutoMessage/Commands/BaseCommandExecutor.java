package com.JnaniDev.AutoMessage.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.JnaniDev.AutoMessage.AutoMessage;

public class BaseCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "The specified command was not found!  Type /sn help to view all commands!");
			return false;
		}
		
		if(AutoMessage.getCommandManager().getCommand(args[0]) == null) {
			sender.sendMessage(ChatColor.RED + "The specified command was not found!");
			return false;
		}
		
		BaseCommand command = SupernaturalRaces.getCommandManager().getCommand(args[0]);

		if((args.length < command.min()) || (args.length > command.max() && command.max() != -1)) {
			sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " " + command.aliases()[0] + " " + command.usage());
			return false;
		}
		
		SupernaturalRaces.getCommandManager().dispatchCommand(command, sender, cmd, commandLabel, args);
		return true;
	}
}
