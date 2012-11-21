package com.TeamNovus.AutoMessage.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Models.MessageList;
import com.TeamNovus.AutoMessage.Tasks.BroadcastTask;

public class MessageListManager {
	private HashMap<String, MessageList> messageLists = new HashMap<String, MessageList>();
	private List<Integer> scheduled = new ArrayList<Integer>();
	
	public HashMap<String, MessageList> getMessageLists() {
		return messageLists;
	}
	
	public void setMessageLists(HashMap<String, MessageList> messageLists) {
		this.messageLists = messageLists;
	}
	
	public void schedule() {
		unschedule();
		
		for(Entry<String, MessageList> entry : messageLists.entrySet()) {
			MessageList list = messageLists.get(entry.getKey());
						
			int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AutoMessage.getPlugin(), 
																				 new BroadcastTask(entry), 
																				 20 * list.getInterval(), 
																				 20 * list.getInterval());
			scheduled.add(id);
		}
	}
	
	public void unschedule() {
		for(Integer id : scheduled) {
			Bukkit.getServer().getScheduler().cancelTask(id);
		}
	}

}
