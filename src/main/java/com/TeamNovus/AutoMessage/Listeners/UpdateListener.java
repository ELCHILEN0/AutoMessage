package com.TeamNovus.AutoMessage.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Permission;

public class UpdateListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(AutoMessage.isUpdateAvailiable() && Permission.has(Permission.COMMAND_UPDATE, event.getPlayer())) {
			event.getPlayer().sendMessage(ChatColor.GREEN + "An update is availiable: " + ChatColor.RESET + AutoMessage.getLatestVersionString());
			event.getPlayer().sendMessage(ChatColor.GREEN + "Type " + ChatColor.RESET + "/am update"  + ChatColor.GREEN + " if you would like to update!");
		}
	}

}
