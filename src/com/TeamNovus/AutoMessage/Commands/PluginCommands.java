package com.TeamNovus.AutoMessage.Commands;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Models.MessageList;
import com.TeamNovus.AutoMessage.Util.StringUtil;

public class PluginCommands {

	@BaseCommand(aliases = "add", description = "Add  message to a list.", usage = "<List> [Index] [Message]", min = 2)
	public void onAddCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// add <List> [Index] [Message]
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
				list.setPrefix(StringUtil.concat(args, 2, args.length));
				sender.sendMessage(ChatColor.GREEN + "Prefix updated!");

				AutoMessage.getMessageListManager().setList(AutoMessage.getMessageListManager().getBestKey(args[1]), list);				
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		}
	}
	
}
