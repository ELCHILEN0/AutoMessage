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
import com.TeamNovus.AutoMessage.Util.Utils;
import com.TeamNovus.AutoMessage.Util.Updater;
import com.TeamNovus.AutoMessage.Util.Updater.UpdateResult;
import com.TeamNovus.AutoMessage.Util.Updater.UpdateType;

public class PluginCommands {

	@BaseCommand(aliases = "reload", desc = "Reload the configuration from the disk.", usage = "", permission = Permission.COMMAND_RELOAD)
	public void onReloadCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		AutoMessage.plugin.loadConfig();

		sender.sendMessage(ChatColor.GREEN + "Configuration reloaded from disk!");
	}

	@BaseCommand(aliases = "update", desc = "Update to the latest version.", usage = "", permission = Permission.COMMAND_UPDATE)
	public void onUpdateCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Updater updater = new Updater(AutoMessage.plugin, 37718, AutoMessage.plugin.getFile(), UpdateType.NO_DOWNLOAD, false);

		if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
			sender.sendMessage(ChatColor.GREEN + "There is an update available! Downloading update...");

			UpdateResult result = new Updater(AutoMessage.plugin, 37718, AutoMessage.plugin.getFile(), UpdateType.DEFAULT, true).getResult();
			if (result == UpdateResult.SUCCESS) {
				sender.sendMessage(ChatColor.RESET + updater.getLatestName() + ChatColor.GREEN + " has been downloaded sucessfully!");
			} else {
				sender.sendMessage(ChatColor.RED + "There was an error downloading " + ChatColor.RESET + updater.getLatestName() + ChatColor.RED + "!");
			}
		} else {
			PluginDescriptionFile desc = AutoMessage.plugin.getDescription();

			sender.sendMessage(ChatColor.RESET + desc.getName() + " v" + desc.getVersion() + ChatColor.GREEN + " is up  to date!");
		}
	}

	@BaseCommand(aliases = "add", desc = "Add a list or message to a list.", usage = "<List> [Index] [Message]", min = 1, permission = Permission.COMMAND_ADD)
	public void onAddCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 1) {
			if (MessageLists.getExactList(args[0]) == null) {
				MessageLists.setList(args[0], new MessageList());

				AutoMessage.plugin.saveConfiguration();

				sender.sendMessage(ChatColor.GREEN + "List added sucessfully!");
			} else {
				sender.sendMessage(ChatColor.RED + "A list already exists by this name!");
			}
		} else {
			MessageList list = MessageLists.getBestList(args[0]);

			if (list != null) {
				if (args.length >= 2) {
					if (args.length >= 3 && Utils.isInteger(args[1])) {
						Message message = new Message(Utils.concat(args, 2, args.length));

						list.addMessage(Integer.valueOf(args[1]), message);
					} else {
						Message message = new Message(Utils.concat(args, 1, args.length));

						list.addMessage(message);
					}

					AutoMessage.plugin.saveConfiguration();

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

		if (list != null) {
			if (Utils.isInteger(args[1])) {
				Message message = new Message(Utils.concat(args, 2, args.length));

				if (list.editMessage(Integer.valueOf(args[1]), message)) {
					AutoMessage.plugin.saveConfiguration();

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
		if (args.length == 1) {
			if (MessageLists.getExactList(args[0]) != null) {
				MessageLists.setList(args[0], null);

				AutoMessage.plugin.saveConfiguration();

				sender.sendMessage(ChatColor.GREEN + "List removed sucessfully!");
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		} else {
			MessageList list = MessageLists.getBestList(args[0]);

			if (list != null) {
				if (Utils.isInteger(args[1])) {
					if (list.removeMessage(Integer.valueOf(args[1]))) {
						MessageLists.schedule();
						AutoMessage.plugin.saveConfiguration();

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

	@BaseCommand(aliases = "enabled", desc = "Toggle broadcasting for a list.", usage = "<List>", min = 1, max = 1, permission = Permission.COMMAND_ENABLE)
	public void onEnableCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if (list != null) {
			list.setEnabled(!(list.isEnabled()));

			AutoMessage.plugin.saveConfiguration();

			sender.sendMessage(ChatColor.GREEN + "Enabled: " + ChatColor.YELLOW + list.isEnabled() + ChatColor.GREEN + "!");
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

	@BaseCommand(aliases = "interval", desc = "Set a lists broadcast interval.", usage = "<List> <Interval>", min = 2, max = 2, permission = Permission.COMMAND_INTERVAL)
	public void onIntervalCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if (list != null) {
			if (Utils.isInteger(args[1])) {
				list.setInterval(Integer.valueOf(args[1]));

				MessageLists.schedule();
				AutoMessage.plugin.saveConfiguration();

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

		if (list != null) {
			try {
				if (Utils.isInteger(args[1])) {
					if (Integer.valueOf(args[1]).longValue() >= 0) {
						list.setExpiry(System.currentTimeMillis() + Integer.valueOf(args[1]).longValue());
					} else {
						list.setExpiry(Integer.valueOf(-1).longValue());
					}
				} else {
					list.setExpiry(System.currentTimeMillis() + Utils.parseTime(args[1]));
				}

				AutoMessage.plugin.saveConfiguration();

				if (list.getExpiry() != -1) {
					sender.sendMessage(ChatColor.GREEN + "Expires in " + ChatColor.YELLOW + Utils.millisToLongDHMS(list.getExpiry() - System.currentTimeMillis()) + ChatColor.GREEN + "!");
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

		if (list != null) {
			list.setRandom(!(list.isRandom()));

			AutoMessage.plugin.saveConfiguration();

			sender.sendMessage(ChatColor.GREEN + "Random: " + ChatColor.YELLOW + list.isRandom() + ChatColor.GREEN + "!");
		} else {
			sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
		}
	}

	@BaseCommand(aliases = "broadcast", desc = "Broadcast a message from a list.", usage = "<List> <Index>", min = 2, max = 2, permission = Permission.COMMAND_BROADCAST)
	public void onBroadcast(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if (list != null) {
			if (Utils.isInteger(args[1])) {
				int index = Integer.valueOf(args[1]);

				if (list.getMessage(index) != null) {
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
		if (args.length == 0) {
			if (MessageLists.getMessageLists().keySet().size() != 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Availiable Lists:");

				for (String key : MessageLists.getMessageLists().keySet()) {
					sender.sendMessage(ChatColor.GOLD + key);
				}
			} else {
				sender.sendMessage(ChatColor.RED + "No lists available!");
			}
		} else {
			MessageList list = MessageLists.getBestList(args[0]);

			if (list != null) {
				sender.sendMessage(ChatColor.DARK_RED + MessageLists.getBestKey(args[0]));

				List<Message> messages = list.getMessages();
				for (int i = 0; i < messages.size(); i++) {
					sender.sendMessage(ChatColor.YELLOW + "" + i + ": " + ChatColor.RESET + ChatColor.translateAlternateColorCodes("&".charAt(0), messages.get(i).getMessage()));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "The specified list does not exist!");
			}
		}
	}

}
