package com.TeamNovus.AutoMessage.Managers;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Models.MessageList;
import com.TeamNovus.AutoMessage.Tasks.BroadcastTask;

public class MessageListManager {
	private HashMap<String, MessageList> messageLists = new HashMap<String, MessageList>();
	
	public HashMap<String, MessageList> getMessageLists() {
		return messageLists;
	}
	
	public void setMessageLists(HashMap<String, MessageList> messageLists) {
		this.messageLists = messageLists;
	}
	
	public void schedule() {
		Bukkit.getServer().getScheduler().cancelTasks(AutoMessage.getPlugin());
		
		for(Entry<String, MessageList> entry : messageLists.entrySet()) {
			MessageList list = messageLists.get(entry.getKey());
						
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AutoMessage.getPlugin(), 
																		new BroadcastTask(entry), 
																		20 * list.getInterval(), 
																		20 * list.getInterval());
		}
	}

}
