package com.TeamNovus.AutoMessage.Tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Models.MessageList;
import com.TeamNovus.AutoMessage.Models.MessageLists;

public class BroadcastTask implements Runnable {
	private String name;

	public BroadcastTask(String name) {
		this.name = name;
	}

	@Override
	public void run() {		
		if(MessageLists.getExactList(name) != null && AutoMessage.getPlugin().getConfig().getBoolean("settings.enabled")) {
			MessageList list = MessageLists.getExactList(name);

			if(list.isEnabled() && list.hasMessages() && !(list.isExpired())) {
				if(Bukkit.getServer().getOnlinePlayers().length >= AutoMessage.getPlugin().getConfig().getInt("settings.min-players")) {
					int index = list.isRandom() ? new Random().nextInt(list.getMessages().size()) : list.getCurrentIndex();

					for(Player p : Bukkit.getServer().getOnlinePlayers()) {					    
						if(p.hasPermission("automessage.receive." + name) && !list.getIgnoreworlds().contains(p.getWorld().getName())) {
							list.broadcastTo(index, p);
						}
					}

					if(AutoMessage.getPlugin().getConfig().getBoolean("settings.log-to-console")) {
						list.broadcastTo(index, Bukkit.getConsoleSender());
					}
					
					list.setCurrentIndex(index + 1);
				}
			}
		}
	}
}
