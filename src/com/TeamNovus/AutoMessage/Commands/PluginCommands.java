package com.TeamNovus.AutoMessage.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Models.MessageList;
import com.TeamNovus.AutoMessage.Tasks.BroadcastTask;
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

	@BaseCommand(aliases = "add", description = "Add a list or message to a list.", usage = "<List> [Index] [Message]", min = 2)
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
					AutoMessage.getPlugin().saveConfiguration();
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

	@BaseCommand(aliases = "remove", description = "Remove a list or message from a list.", usage = "<List> [Index]", min = 2, max = 3)
	public void onRemoveCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// remove <List> [Index]
		if(!(sender.hasPermission("automessage.commands.remove"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}

		if(args.length == 2) {
			if(AutoMessage.getMessageListManager().getExactList(args[1]) != null) {
				AutoMessage.getMessageListManager().setList(args[1], null);
				AutoMessage.getPlugin().saveConfiguration();
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
						AutoMessage.getPlugin().saveConfiguration();
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

	@BaseCommand(aliases = "enabled", description = "Toggle broadcasting for a list.", usage = "<List>", min = 2, max = 2)
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
			AutoMessage.getPlugin().saveConfiguration();
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

	@BaseCommand(aliases = "interval", description = "Set a lists broadcast interval.", usage = "<List> <Interval>", min = 3, max = 3)
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
				AutoMessage.getPlugin().saveConfiguration();
			} else {
				sender.sendMessage(ChatColor.RED + "The interval must be an Integer!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}
	
	@BaseCommand(aliases = "expiry", description = "Set a lists expiry time.", usage = "<List> <Expiry>", min = 3, max = 3)
	public void onExpiryCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// expiry <List> <Interval>
		if(!(sender.hasPermission("automessage.commands.expiry"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}

		if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
			MessageList list = AutoMessage.getMessageListManager().getBestList(args[1]);

			try {
				if(StringUtil.isInteger(args[2])) {
					if(Integer.valueOf(args[2]).longValue() >= 0) {
						list.setExpiry(System.currentTimeMillis() + Integer.valueOf(args[2]).longValue());
					} else {
						list.setExpiry(Integer.valueOf(-1).longValue());
					}
				} else {
					list.setExpiry(System.currentTimeMillis() + StringUtil.parseTime(args[2]));
				}
				
				if(list.getExpiry() != -1) {
					sender.sendMessage(ChatColor.GREEN + "Expires in " + ChatColor.YELLOW + StringUtil.millisToLongDHMS(list.getExpiry() - System.currentTimeMillis()) + ChatColor.GREEN + "!");
				} else {
					sender.sendMessage(ChatColor.GREEN + "Expiry disabled!");
				}
				AutoMessage.getMessageListManager().setList(AutoMessage.getMessageListManager().getBestKey(args[1]), list);	
				AutoMessage.getPlugin().saveConfiguration();
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Illegal Format. To disable use -1.");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

	@BaseCommand(aliases = "random", description = "Set a lists broadcast method.", usage = "<List>", min = 2, max = 2)
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
			AutoMessage.getPlugin().saveConfiguration();
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

	@BaseCommand(aliases = "prefix", description = "Set a lists prefix for broadcasts.", usage = "<List> [Prefix]", min = 2)
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
				AutoMessage.getPlugin().saveConfiguration();
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		} else {
			if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
				MessageList list = AutoMessage.getMessageListManager().getBestList(args[1]);
				list.setPrefix(StringUtil.concat(args, 2, args.length) + " ");
				sender.sendMessage(ChatColor.GREEN + "Prefix updated!");

				AutoMessage.getMessageListManager().setList(AutoMessage.getMessageListManager().getBestKey(args[1]), list);
				AutoMessage.getPlugin().saveConfiguration();
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		}
	}

	@BaseCommand(aliases = "broadcast", description = "Broadcast a message from a list.", usage = "<List> <Index>", min = 3, max = 3)
	public void onBroadcast(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// remove <List> [Index]
		if(!(sender.hasPermission("automessage.commands.broadcast"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}

		if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
			MessageList list = AutoMessage.getMessageListManager().getBestList(args[1]);

			if(StringUtil.isInteger(args[2])) {
				if(list.getMessage(Integer.valueOf(args[2])) != null) {

					String[] lines = list.getMessage(Integer.valueOf(args[2])).split("\\\\n");
					for(int i = 0; i < lines.length; i++) {
						if(lines[i].startsWith("/")) {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), lines[i].replaceFirst("/", ""));
						} else {
							if(i == 0) {
								lines[i] = list.getPrefix() + lines[i];
							}
																					
							for(Player p : Bukkit.getServer().getOnlinePlayers()) {
								p.sendMessage(BroadcastTask.parsePlayer(p, lines[i]));
							}
							
							Bukkit.getConsoleSender().sendMessage(BroadcastTask.parseConsole(lines[i]));
						}
					}
					
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
	
	@BaseCommand(aliases = "list", description = "List all lists or messages in a list.", usage = "[List]", min = 1, max = 2)
	public void onListCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// list [List]
		if(!(sender.hasPermission("automessage.commands.list"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return;
		}

		if(args.length == 1) {
			if(AutoMessage.getMessageListManager().getMessageLists().keySet().size() != 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Availiable Lists:");
				for(String key : AutoMessage.getMessageListManager().getMessageLists().keySet()) {
					sender.sendMessage(ChatColor.GOLD + key);
				}
			} else {
				sender.sendMessage(ChatColor.RED + "No lists availiable!");
			}
		} else {
			if(AutoMessage.getMessageListManager().getBestList(args[1]) != null) {
				sender.sendMessage(ChatColor.DARK_RED + AutoMessage.getMessageListManager().getBestKey(args[1]));
				List<String> messages = AutoMessage.getMessageListManager().getBestList(args[1]).getMessages();
				for (int i = 0; i < messages.size(); i++) {
					sender.sendMessage(ChatColor.YELLOW + "" + i + ": " + ChatColor.RESET + ChatColor.translateAlternateColorCodes("&".charAt(0), AutoMessage.getMessageListManager().getBestList(args[1]).getPrefix() + messages.get(i)));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		}
	}
}
