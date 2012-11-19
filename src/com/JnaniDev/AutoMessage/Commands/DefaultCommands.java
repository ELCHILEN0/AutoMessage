package com.JnaniDev.AutoMessage.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.TeamNovus.SupernaturalRaces.Util.Util;

public class DefaultCommands {

	@BaseCommand(aliases = { "tutorial", "tut" }, description = "Learn about the plugin through an interactive tutorial!", usage = "[Chapter]", min = 0, max = 2)
	public void onTutorialCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		int page = (args.length > 1) ? (Util.isInt(args[1]) ? Integer.valueOf(args[1]) : 0) : 0;
		if(page == 0) {
			sender.sendMessage(ChatColor.BLUE + "Table of Contents:");
			sender.sendMessage(ChatColor.GOLD + "   Chapter 1 " + ChatColor.BLUE + "-" + ChatColor.GOLD + " Overview");
			sender.sendMessage(ChatColor.GOLD + "   Chapter 2 " + ChatColor.BLUE + "-" + ChatColor.GOLD + " Gameplay");
			sender.sendMessage(ChatColor.GOLD + "   Chapter 3 " + ChatColor.BLUE + "-" + ChatColor.GOLD + " Commands");
			sender.sendMessage(ChatColor.GOLD + "   Chapter 4 " + ChatColor.BLUE + "-" + ChatColor.GOLD + " Races");
			sender.sendMessage(ChatColor.GOLD + "   Chapter 5 " + ChatColor.BLUE + "-" + ChatColor.GOLD + " Spells");
			sender.sendMessage(ChatColor.GREEN + "To continue on to gameplay type: " + ChatColor.AQUA + "/sn tutorial 1");
		} else if(page == 1) {
			sender.sendMessage(ChatColor.GREEN + "Overview:");
			sender.sendMessage(ChatColor.GOLD + "SupernaturalRaces was is found ONLY on NovusCraft!");
			sender.sendMessage(ChatColor.GOLD + "We feature many different races and spells that bring aspects never before seen in Minecraft to our server!");
			sender.sendMessage(ChatColor.GOLD + "We suggest that you read every part of this tutorial to fully understand the plugin.");
			sender.sendMessage(ChatColor.GREEN + "To continue on to gameplay type: " + ChatColor.AQUA + "/sn tutorial 2");
		} else if(page == 2) {
			sender.sendMessage(ChatColor.GREEN + "Gameplay:");
			sender.sendMessage(ChatColor.GOLD + "SupernaturalRaces allows you to become a different race with unique abilites, weaknesses and spells!");
			sender.sendMessage(ChatColor.GOLD + "Each race has a certain amount of power.  Your power regains every 10 seconds depending on your race!");
			sender.sendMessage(ChatColor.GOLD + "Some races gain power quicker at different times of the day or in different worlds!");
			sender.sendMessage(ChatColor.GOLD + "This power can then be used to cast spells and perform other actions!");
			sender.sendMessage(ChatColor.GREEN + "To continue on to commands type: " + ChatColor.AQUA + "/sn tutorial 3");
		} else if(page == 3) {
			sender.sendMessage(ChatColor.GREEN + "Commands:");
			sender.sendMessage(ChatColor.GOLD + "There are a few basic commands that you should know.");
			sender.sendMessage(ChatColor.AQUA + "/sn convert <Race> " + ChatColor.GOLD + "- Convert to a race.");
			sender.sendMessage(ChatColor.AQUA + "/sn races " + ChatColor.GOLD + "- List availiable races.");
			sender.sendMessage(ChatColor.AQUA + "/sn race <Race> " + ChatColor.GOLD + "- View generated info about a race.");
			sender.sendMessage(ChatColor.AQUA + "/sn spell <Spell> " + ChatColor.GOLD + "- View generated info about a spell.");
			sender.sendMessage(ChatColor.AQUA + "/sn power " + ChatColor.GOLD + "- View your power.");
			sender.sendMessage(ChatColor.GREEN + "To continue on to commands type: " + ChatColor.AQUA + "/sn tutorial 4");
		} else if(page == 4) {
			sender.sendMessage(ChatColor.GREEN + "Races:");
			sender.sendMessage(ChatColor.GOLD + "Each race has a set Max Power.");
			sender.sendMessage(ChatColor.GOLD + "You power is regained every 10 seconds depending on the setting.");
			sender.sendMessage(ChatColor.GOLD + "Some races may gain power slowly others quickly.");
			sender.sendMessage(ChatColor.GOLD + "Races also consist of EventModifiers.  These allow us to customize damages, abilties etc.");
			sender.sendMessage(ChatColor.GOLD + "Finally races consist of Spells.");
			sender.sendMessage(ChatColor.GREEN + "To continue on to spells type: " + ChatColor.AQUA + "/sn tutorial 5");
		} else if(page == 5) {
			sender.sendMessage(ChatColor.GREEN + "Spells:");
			sender.sendMessage(ChatColor.GOLD + "Each race has a unique set of spells!");
			sender.sendMessage(ChatColor.GOLD + "Some spells are healing, others damage and others are buff spells!");
			sender.sendMessage(ChatColor.GOLD + "To view all the spells available for your race type " + ChatColor.AQUA + "/sn race <YourRace>" + ChatColor.GOLD + "!");
			sender.sendMessage(ChatColor.GOLD + "To view a spells info simply type " + ChatColor.AQUA + "/sn spell <Spell>" + ChatColor.GOLD + "!");
			sender.sendMessage(ChatColor.GOLD + "To switch through your spells right click with a BLAZE_ROD.");
			sender.sendMessage(ChatColor.GOLD + "To cast a spell left click with a BLAZE_ROD!");
			sender.sendMessage(ChatColor.GOLD + "Once cast the spell will consume the consumable reagents!");
			sender.sendMessage(ChatColor.GREEN + "You have completed the tutorial!  Thanks for reading!");
		} else {
			sender.sendMessage(ChatColor.RED + "The specified page was not found!");
		}
	}

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
