package com.TeamNovus.AutoMessage.Commands.Common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Permission;
import java.util.Arrays;

public class BaseCommandExecutor implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(CommandManager.getExtra() + "__________________.[ " + CommandManager.getHighlight() + AutoMessage.getPlugin().getName() + CommandManager.getExtra() + " ].__________________");
			sender.sendMessage(CommandManager.getDark() + "Description: " + CommandManager.getLight() + AutoMessage.getPlugin().getDescription().getDescription());
			sender.sendMessage(CommandManager.getDark() + "Author: " + CommandManager.getLight() + AutoMessage.getPlugin().getDescription().getAuthors().get(0));
			sender.sendMessage(CommandManager.getDark() + "Version: " + CommandManager.getLight() + AutoMessage.getPlugin().getDescription().getVersion());
			sender.sendMessage(CommandManager.getDark() + "Website: " + CommandManager.getLight() + AutoMessage.getPlugin().getDescription().getWebsite());
			sender.sendMessage(CommandManager.getExtra() + "---------------------------------------------------");
			return true;
		}
		
		BaseCommand command = CommandManager.getCommand(args[0]);
		if(command == null) {
			sender.sendMessage(CommandManager.getError() + "The specified command was not found!");
			return true;
		}
		
		Object[] commandArgs = ArrayUtils.remove(args, 0);
		
		if(sender instanceof Player && !(command.player())) {
			sender.sendMessage(CommandManager.getError() + "This command cannot be ran as a player!");
			return true;
		}
		
		if(sender instanceof ConsoleCommandSender && !(command.console())) {
			sender.sendMessage(CommandManager.getError() + "This command cannot be ran from the console!");
			return true;
		}
		
		boolean hasPermission = command.permission().isMulti()
			? ((commandArgs.length > 0) ? Permission.has(command.permission(), sender, (String)commandArgs[0]) : false)
			: Permission.has(command.permission(), sender);
		
		if(command.permission() != null && !(command.permission().equals(Permission.NONE)) && !hasPermission) {
			sender.sendMessage(CommandManager.getError() + "You do not have permission for this command!");
			return true;
		}
		
		if((commandArgs.length < command.min()) || (commandArgs.length > command.max() && command.max() != -1)) {
			sender.sendMessage(CommandManager.getError() + "Usage: /" + commandLabel + " " + command.aliases()[0] + " " + command.usage());
			return true;
		}
		
		CommandManager.execute(command, sender, cmd, commandLabel, commandArgs);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {	
		ArrayList<String> list = new ArrayList<String>();
		
		for(BaseCommand command : CommandManager.getCommands()) {
			list.addAll(Arrays.asList(command.aliases()));
		}
		
		return list;		
	}
}
