package com.TeamNovus.AutoMessage.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Permission;
import com.TeamNovus.AutoMessage.Commands.Common.BaseCommand;
import com.TeamNovus.AutoMessage.Models.Message;
import com.TeamNovus.AutoMessage.Models.MessageList;
import com.TeamNovus.AutoMessage.Models.MessageLists;
import com.TeamNovus.AutoMessage.Util.StringUtil;
import com.TeamNovus.AutoMessage.Util.Updater;
import com.TeamNovus.AutoMessage.Util.Updater.UpdateResult;
import com.TeamNovus.AutoMessage.Util.Updater.UpdateType;

public class PluginCommands {

	@BaseCommand(aliases = "reload", desc = "Reload the configuration from the disk.", usage = "", permission = Permission.COMMAND_RELOAD)
	public void onReloadCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		AutoMessage.getPlugin().loadConfig();

		sender.sendMessage(ChatColor.GREEN + "Configuration reloaded from disk!");
	}
	
	@BaseCommand(aliases = "update", desc = "Update to the latest version.", usage = "", permission = Permission.COMMAND_UPDATE)
	public void onUpdateCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Updater updater = new Updater(AutoMessage.getPlugin(), "automessage", AutoMessage.getPlugin().getFile(), UpdateType.NO_DOWNLOAD, false);
		
		if(updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
			sender.sendMessage(ChatColor.GREEN + "There is an update available! Downloading update...");
			
			UpdateResult result = new Updater(AutoMessage.getPlugin(), "automessage", AutoMessage.getPlugin().getFile(), UpdateType.NO_VERSION_CHECK, true).getResult();
			if(result == UpdateResult.SUCCESS) {
				sender.sendMessage(ChatColor.RESET + updater.getLatestVersionString() + ChatColor.GREEN + " has been downloaded sucessfully!");
			} else  {
				sender.sendMessage(ChatColor.RED + "There was an error downloading " + ChatColor.RESET + updater.getLatestVersionString() + ChatColor.RED + "!");
			}
		} else {
			PluginDescriptionFile desc = AutoMessage.getPlugin().getDescription();
			
			sender.sendMessage(ChatColor.RESET + desc.getName() + " v" + desc.getVersion() + ChatColor.GREEN + " is up  to date!");
		}
	}

	@BaseCommand(aliases = "add", desc = "Add a list or message to a list.", usage = "<List> [Index] [Message]", min = 1, permission = Permission.COMMAND_ADD)
	public void onAddCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length == 1) {
			if(MessageLists.getExactList(args[0]) == null) {
				MessageLists.setList(args[0], new MessageList());
				
				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "List added sucessfully!");				
			} else {
				sender.sendMessage(ChatColor.RED + "A list already exists by this name!");
			}
		} else {
			MessageList list = MessageLists.getBestList(args[0]);

			if(list != null) {
				if(args.length >= 2) {
					if(args.length >= 3 && StringUtil.isInteger(args[1]) ) {
						Message message = new Message(StringUtil.concat(args, 2, args.length));
						
						list.addMessage(Integer.valueOf(args[1]), message);
					} else {
						Message message = new Message(StringUtil.concat(args, 1, args.length));

						list.addMessage(message);
					}

					AutoMessage.getPlugin().saveConfiguration();

					sender.sendMessage(ChatColor.GREEN + "Message added!");
				} else {
					sender.sendMessage(ChatColor.RED + "Please specify more arguments!");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		}
	}

	@BaseCommand(aliases = "edit", desc = "Edit a message in a list.", usage = "<List> <Index> <Message>", min = 3, permission = Permission.COMMAND_EDIT)
	public void onEditCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			if(StringUtil.isInteger(args[1])) {
				Message message = new Message(StringUtil.concat(args, 2, args.length));
				
				if(list.editMessage(Integer.valueOf(args[1]), message)) {
					AutoMessage.getPlugin().saveConfiguration();
					
					sender.sendMessage(ChatColor.GREEN + "Message edited!");
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

	@BaseCommand(aliases = "remove", desc = "Remove a list or message from a list.", usage = "<List> [Index]", min = 1, max = 3, permission = Permission.COMMAND_REMOVE)
	public void onRemoveCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length == 1) {
			if(MessageLists.getExactList(args[0]) != null) {
				MessageLists.setList(args[0], null);
				
				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "List removed sucessfully!");				
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		} else {
			MessageList list = MessageLists.getBestList(args[0]);

			if(list != null) {
				if(StringUtil.isInteger(args[1])) {
					if(list.removeMessage(Integer.valueOf(args[1]))) {
						MessageLists.schedule();
						AutoMessage.getPlugin().saveConfiguration();
						
						sender.sendMessage(ChatColor.GREEN + "Message removed!");
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

	@BaseCommand(aliases = "enable", desc = "Toggle broadcasting for a list.", usage = "<List>", min = 1, max = 1, permission = Permission.COMMAND_ENABLE)
	public void onEnableCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			list.setEnabled(!(list.isEnabled()));
			
			AutoMessage.getPlugin().saveConfiguration();

			sender.sendMessage(ChatColor.GREEN + "Enabled: " + ChatColor.YELLOW + list.isEnabled() + ChatColor.GREEN + "!");
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

	@BaseCommand(aliases = "interval", desc = "Set a lists broadcast interval.", usage = "<List> <Interval>", min = 2, max = 2, permission = Permission.COMMAND_INTERVAL)
	public void onIntervalCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			if(StringUtil.isInteger(args[1])) {
				list.setInterval(Integer.valueOf(args[1]));
				
				MessageLists.schedule();
				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Interval: " + ChatColor.YELLOW + Integer.valueOf(args[1]) + ChatColor.GREEN + "!");
			} else {
				sender.sendMessage(ChatColor.RED + "The interval must be an Integer!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

	@BaseCommand(aliases = "expiry", desc = "Set a lists expiry time.", usage = "<List> <Expiry>", min = 2, max = 2, permission = Permission.COMMAND_EXPIRY)
	public void onExpiryCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			try {
				if(StringUtil.isInteger(args[1])) {
					if(Integer.valueOf(args[1]).longValue() >= 0) {
						list.setExpiry(System.currentTimeMillis() + Integer.valueOf(args[1]).longValue());
					} else {
						list.setExpiry(Integer.valueOf(-1).longValue());
					}
				} else {
					list.setExpiry(System.currentTimeMillis() + StringUtil.parseTime(args[1]));
				}

				AutoMessage.getPlugin().saveConfiguration();
				
				if(list.getExpiry() != -1) {
					sender.sendMessage(ChatColor.GREEN + "Expires in " + ChatColor.YELLOW + StringUtil.millisToLongDHMS(list.getExpiry() - System.currentTimeMillis()) + ChatColor.GREEN + "!");
				} else {
					sender.sendMessage(ChatColor.GREEN + "Expiry disabled!");
				}
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Illegal Format. To disable use -1.");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

	@BaseCommand(aliases = "random", desc = "Set a lists broadcast method.", usage = "<List>", min = 1, max = 1, permission = Permission.COMMAND_RANDOM)
	public void onRandomCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			list.setRandom(!(list.isRandom()));
			
			AutoMessage.getPlugin().saveConfiguration();
			
			sender.sendMessage(ChatColor.GREEN + "Random: " + ChatColor.YELLOW + list.isRandom() + ChatColor.GREEN + "!");
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

	@BaseCommand(aliases = "prefix", desc = "Set a lists prefix for broadcasts.", usage = "<List> [Prefix]", min = 1, permission = Permission.COMMAND_PREFIX)
	public void onPrefixCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			if(args.length == 1) {
				list.setPrefix("");
				
				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Prefix updated!");
			} else {
				list.setPrefix(StringUtil.concat(args, 1, args.length) + " ");

				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Prefix updated!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

	@BaseCommand(aliases = "suffix", desc = "Set a lists suffix for broadcasts.", usage = "<List> [Suffix]", min = 1, permission = Permission.COMMAND_SUFFIX)
	public void onSuffixCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			if(args.length == 1) {
				list.setSuffix("");

				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Suffix updated!");
			} else {
				list.setSuffix(" " + StringUtil.concat(args, 1, args.length));

				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Suffix updated!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

	@BaseCommand(aliases = "broadcast", desc = "Broadcast a message from a list.", usage = "<List> <Index>", min = 2, max = 2, permission = Permission.COMMAND_BROADCAST)
	public void onBroadcast(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			if(StringUtil.isInteger(args[1])) {
				int index = Integer.valueOf(args[1]);

				if(list.getMessage(index) != null) {
					list.broadcast(index);
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

	@BaseCommand(aliases = "list", desc = "List all lists or messages in a list.", usage = "[List]", max = 1, permission = Permission.COMMAND_LIST)
	public void onListCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length == 0) {
			if(!MessageLists.getMessageLists().keySet().isEmpty()) {
				sender.sendMessage(ChatColor.DARK_RED + "Availiable Lists:");
				
				for(String key : MessageLists.getMessageLists().keySet()) {
					sender.sendMessage(ChatColor.GOLD + key);
				}
			} else {
				sender.sendMessage(ChatColor.RED + "No lists availiable!");
			}
		} else {
			MessageList list = MessageLists.getBestList(args[0]);

			if(list != null) {
				sender.sendMessage(ChatColor.DARK_RED + MessageLists.getBestKey(args[0]));
				
				List<Message> messages = list.getMessages();
				for (int i = 0; i < messages.size(); i++) {
					sender.sendMessage(ChatColor.YELLOW + "" + i + ": " + ChatColor.RESET + ChatColor.translateAlternateColorCodes("&".charAt(0), list.getPrefix() + messages.get(i).getFormat() + list.getSuffix()));
					
					for(int j = 0; j < messages.get(i).getArguments().size(); j++) {
						sender.sendMessage(" Argument #" + j + ":" + messages.get(i).getArguments().get(j));
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		}
	}
}
