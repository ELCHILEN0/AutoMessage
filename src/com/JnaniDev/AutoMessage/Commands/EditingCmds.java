package com.JnaniDev.AutoMessage.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.JnaniDev.AutoMessage.AutoMessage;
import com.JnaniDev.AutoMessage.MessageList;

public class EditingCmds implements CommandExecutor {
	private AutoMessage plugin;

	public EditingCmds(AutoMessage plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String command = ChatColor.GRAY + "- " + ChatColor.GOLD + "/" + commandLabel + " %s ";
		
		if((args.length > 1) && (args[0].equalsIgnoreCase("add"))) {
			if(args.length == 2) {
				// Add a MessageList
				plugin.messageLists.put(args[1], new MessageList());
				plugin.saveConfiguration();
				sendMessage(sender, ChatColor.GREEN + "Added list!");
			} else if(args.length > 2) {
				// Add a Message to a MessageList
				if(plugin.messageLists.get(args[1]) == null) {
					sendMessage(sender, ChatColor.RED + "The specified list was not found!");
					return true;
				}
				
				if(isInteger(args[2])) {
					if(plugin.messageLists.size() > Integer.valueOf(args[2])) {
						plugin.messageLists.get(args[1]).addMessage(Integer.valueOf(args[2]), compactString(args, 3));
					} else {
						plugin.messageLists.get(args[1]).addMessage(compactString(args, 3));
					}

				} else {
					plugin.messageLists.get(args[1]).addMessage(compactString(args, 2));
				}
				plugin.saveConfiguration();
				plugin.schedule();
				sendMessage(sender, ChatColor.GREEN + "Added message!");
			} else {
				sendMessage(sender, String.format(command, "add <MessageList> [Index] [Message]"));
			}
			return true;
		}
		
		if((args.length > 1) && (args[0].equalsIgnoreCase("edit"))) {
			if(args.length >= 4) {
				// Edit a message at an index in a MessageList
				if(plugin.messageLists.get(args[1]) == null) {
					sendMessage(sender, ChatColor.RED + "The specified list was not found!");
					return true;
				}
				
				if(isInteger(args[2]) && plugin.messageLists.size() > Integer.valueOf(args[2])) {
					plugin.messageLists.get(args[1]).editMessage(Integer.valueOf(args[2]), compactString(args, 3));
				} else {
					sendMessage(sender, ChatColor.RED + "The specified index was not found!");
					return true;
				}
				plugin.saveConfiguration();
				plugin.schedule();
				sendMessage(sender, ChatColor.GREEN + "Edited message!");
			} else {
				sendMessage(sender, String.format(command, "edit <MessageList> <Index> [Message]"));
			}
			return true;
		}
		
		if((args.length > 1) && (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete"))) {
			if(args.length > 2) {
				// Remove a message from a list
				if(plugin.messageLists.get(args[1]) == null) {
					sendMessage(sender, ChatColor.RED + "The specified list was not found!");
					return true;
				}
				
				if(isInteger(args[2]) && plugin.messageLists.get(args[1]).getTotalMessages() > Integer.valueOf(args[2])) {
					plugin.messageLists.get(args[1]).removeMessage(Integer.valueOf(args[2]));
				} else {
					sendMessage(sender, ChatColor.RED + "The specified index was not found!");
					return true;
				}
				plugin.saveConfiguration();
				plugin.schedule();
				sendMessage(sender, ChatColor.GREEN + "Removed message!");
			} else {
				sendMessage(sender, String.format(command, "remove <MessageList> <Index>"));
			}
			return true;
		}
		
		// /am list [List]
		if((args.length > 0) && (args[0].equalsIgnoreCase("list"))) {
			if(args.length > 1) {
				// List the messages and indices in a list
				if(plugin.messageLists.get(args[1]) == null) {
					sendMessage(sender, ChatColor.RED + "The specified list was not found!");
					return true;
				}
				
				sendMessage(sender, ChatColor.GREEN + "Messages in List " + args[1] + ":");
				int i = 0;
				for(String message : plugin.messageLists.get(args[1]).getMessages()) {
					sendMessage(sender, ChatColor.YELLOW + String.valueOf(i) + ": " + ChatColor.RESET + plugin.messageLists.get(args[1]).getPrefix() + message);
					i++;
				}
			} else {
				// Display Lists
				sendMessage(sender, ChatColor.GREEN + "Availiable Lists:");
				for(String key : plugin.messageLists.keySet()) {
					sendMessage(sender, ChatColor.YELLOW + "  " + key);
				}
			}
			return true;
		}
		
		if((args.length > 1) && (args[0].equalsIgnoreCase("broadcast"))) {
			if(args.length > 2) {
				// Broadcast a message from a list
				if(plugin.messageLists.get(args[1]) == null) {
					sendMessage(sender, ChatColor.RED + "The specified list was not found!");
					return true;
				}
								
				if(isInteger(args[2]) && plugin.messageLists.size() > Integer.valueOf(args[2])) {
					for(String s : plugin.messageLists.get(args[1]).getMessage(Integer.valueOf(args[2]))) {
						if(s.equals(plugin.messageLists.get(args[1]).getMessage(Integer.valueOf(args[2]))[0]))
							s = plugin.messageLists.get(args[1]).getPrefix() + s;
						sendMessage(sender, s);
					}
				} else {
					sendMessage(sender, ChatColor.RED + "The specified index was not found!");
					return true;
				}
			} else {
				sendMessage(sender, String.format(command, "broadcast <MessageList> <Index>"));
			}
			return true;
		}
		
		if((args.length > 1) && (args[0].equalsIgnoreCase("interval"))) {
			if(args.length > 2) {
				// Set a message list interval
				if(plugin.messageLists.get(args[1]) == null) {
					sendMessage(sender, ChatColor.RED + "The specified list was not found!");
					return true;
				}
								
				if(isInteger(args[2])) {
					plugin.messageLists.get(args[1]).setInterval(Integer.valueOf(args[2]));	
				} else {
					sendMessage(sender, ChatColor.RED + "The specified index was not found!");
					return true;
				}
				
				plugin.saveConfiguration();
				plugin.schedule();
				sendMessage(sender, ChatColor.GREEN + "Interval udpated!");
			} else {
				sendMessage(sender, String.format(command, "interval <MessageList> <Interval>"));
			}
			return true;
		}
		
		return false;
	}
	
	public String compactString(String args[], Integer minArg) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<args.length; i++) {
			if(i >= minArg) {
				sb.append(args[i] + " ");
			}
		}
		
		return sb.toString().trim();
	}
	
	public boolean isInteger(String s) {
		try {
			Integer.valueOf(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public void sendMessage(CommandSender sender, String s) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes("&".charAt(0), s));
	}
}
