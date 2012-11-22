package com.TeamNovus.AutoMessage.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Models.MessageList;
import com.TeamNovus.AutoMessage.Util.StringUtil;

public class PluginCommands {

	@BaseCommand(aliases = "reload", description = "Reload the configuration from the disk.", usage = "", min = 1, max = 1)
	public void onReloadCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// reload
		if(!(sender.hasPermission("automessage.commands.reload"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}
		
		AutoMessage.getPlugin().reloadConfiguration();
		sender.sendMessage(ChatColor.GREEN + "Configuration reloaded from disk!");
	}
	
	@BaseCommand(aliases = "add", description = "Add  message to a list.", usage = "<List> [Index] [Message]", min = 2)
	public void onAddCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// add <List> [Index] [Message]
		if(!(sender.hasPermission("automessage.commands.add"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}
		
		if(args.length == 2) {
			if(AutoMessage.getMessageListManager().getExactList(args[1]) == null) {
				AutoMessage.getMessageListManager().setList(args[1], new MessageList());
				sender.sendMessage(ChatColor.GREEN + "List added sucessfully!");
			} else {
				sender.sendMessage(ChatColor.RED + "A list already exists by this name!");
			}
		} else {
			if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
				if(args.length >= 3) {
					MessageList list = AutoMessage.getMessageListManager().getBestList(args[1]);
					
					if(args.length >= 4 && StringUtil.isInteger(args[2]) ) {
						list.addMessage(Integer.valueOf(args[2]), StringUtil.concat(args, 3, args.length));
					} else {
						list.addMessage(StringUtil.concat(args, 2, args.length));
					}
					
					AutoMessage.getMessageListManager().setList(AutoMessage.getMessageListManager().getBestKey(args[1]), list);
					AutoMessage.getMessageListManager().schedule();
					
					sender.sendMessage(ChatColor.GREEN + "Message added!");
				} else {
					sender.sendMessage(ChatColor.RED + "Please specify more arguments!");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		}
	}
	
	@BaseCommand(aliases = "edit", description = "Edit a message in a list.", usage = "<List> <Index> <Message>", min = 4)
	public void onEditCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// edit <List> <Index> <Message>
		if(!(sender.hasPermission("automessage.commands.edit"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}
		
		if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
			MessageList list = AutoMessage.getMessageListManager().getBestList(args[1]);

			if(StringUtil.isInteger(args[2])) {
				if(list.editMessage(Integer.valueOf(args[2]), StringUtil.concat(args, 3, args.length))) {
					sender.sendMessage(ChatColor.GREEN + "Message edited!");

					AutoMessage.getMessageListManager().setList(AutoMessage.getMessageListManager().getBestKey(args[1]), list);
					AutoMessage.getMessageListManager().schedule();
				} else {
					sender.sendMessage(ChatColor.RED + "The specified index does not exist!");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "The specified index does not exist!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}
	
	@BaseCommand(aliases = "remove", description = "Remove a message from the list.", usage = "<List> [Index]", min = 2, max = 3)
	public void onRemoveCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// remove <List> [Index]
		if(!(sender.hasPermission("automessage.commands.remove"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}
		
		if(args.length == 2) {
			if(AutoMessage.getMessageListManager().getExactList(args[1]) != null) {
				AutoMessage.getMessageListManager().setList(args[1], null);
				sender.sendMessage(ChatColor.GREEN + "List removed sucessfully!");
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		} else {
			if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
				MessageList list = AutoMessage.getMessageListManager().getBestList(args[1]);

				if(StringUtil.isInteger(args[2])) {
					if(list.removeMessage(Integer.valueOf(args[2]))) {
						sender.sendMessage(ChatColor.GREEN + "Message removed!");
						
						AutoMessage.getMessageListManager().setList(AutoMessage.getMessageListManager().getBestKey(args[1]), list);
						AutoMessage.getMessageListManager().schedule();
					} else {
						sender.sendMessage(ChatColor.RED + "The specified index does not exist!");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "The specified index does not exist!");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		}
	}
	
	@BaseCommand(aliases = "prefix", description = "Set a lists prefix.", usage = "<List> [Prefix]", min = 2)
	public void onPrefixCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// prefix <List> <Prefix>
		if(!(sender.hasPermission("automessage.commands.prefix"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}
		
		if(args.length == 2) {
			if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
				MessageList list = AutoMessage.getMessageListManager().getBestList(args[1]);
				list.setPrefix("");
				sender.sendMessage(ChatColor.GREEN + "Prefix updated!");

				AutoMessage.getMessageListManager().setList(AutoMessage.getMessageListManager().getBestKey(args[1]), list);				
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		} else {
			if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
				MessageList list = AutoMessage.getMessageListManager().getBestList(args[1]);
				list.setPrefix(StringUtil.concat(args, 2, args.length) + " ");
				sender.sendMessage(ChatColor.GREEN + "Prefix updated!");

				AutoMessage.getMessageListManager().setList(AutoMessage.getMessageListManager().getBestKey(args[1]), list);				
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		}
	}
	
	@BaseCommand(aliases = "enabled", description = "Set a lists prefix.", usage = "<List>", min = 2, max = 2)
	public void onEnableCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// enabled <List>
		if(!(sender.hasPermission("automessage.commands.enabled"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}
		
		if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
			MessageList list = AutoMessage.getMessageListManager().getBestList(args[1]);
			list.setEnabled(!(list.isEnabled()));
			sender.sendMessage(ChatColor.GREEN + "Enabled: " + ChatColor.YELLOW + list.isEnabled() + ChatColor.GREEN + "!");

			AutoMessage.getMessageListManager().setList(AutoMessage.getMessageListManager().getBestKey(args[1]), list);				
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}
	
	@BaseCommand(aliases = "interval", description = "Set a lists interval.", usage = "<List> <Interval>", min = 3, max = 3)
	public void onIntervalCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// interval <List> <Interval>
		if(!(sender.hasPermission("automessage.commands.interval"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}
		
		if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
			MessageList list = AutoMessage.getMessageListManager().getBestList(args[1]);
			
			if(StringUtil.isInteger(args[2])) {
				list.setInterval(Integer.valueOf(args[2]));
				sender.sendMessage(ChatColor.GREEN + "Interval: " + ChatColor.YELLOW + Integer.valueOf(args[2]) + ChatColor.GREEN + "!");
	
				AutoMessage.getMessageListManager().setList(AutoMessage.getMessageListManager().getBestKey(args[1]), list);		
			} else {
				sender.sendMessage(ChatColor.RED + "The interval must be an Integer!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}
	
	@BaseCommand(aliases = "random", description = "Set a lists broadcasting method.", usage = "<List>", min = 2, max = 2)
	public void onRandomCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// random <List>
		if(!(sender.hasPermission("automessage.commands.random"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}
		
		if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
			MessageList list = AutoMessage.getMessageListManager().getBestList(args[1]);
			list.setRandom(!(list.isRandom()));
			sender.sendMessage(ChatColor.GREEN + "Random: " + ChatColor.YELLOW + list.isRandom() + ChatColor.GREEN + "!");

			AutoMessage.getMessageListManager().setList(AutoMessage.getMessageListManager().getBestKey(args[1]), list);				
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

}
