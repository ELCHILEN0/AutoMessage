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
	
	public MessageList getExactList(String name) {
		for(String key : messageLists.keySet()) {
			if(key.equals(name)) {
				return messageLists.get(key);
			}
		}
		return null;
	}
	
	public MessageList getBestList(String name) {
		for(String key : messageLists.keySet()) {
			if(key.startsWith(name)) {
				return messageLists.get(key);
			}
		}
		return null;
	}
	
	public String getBestKey(String name) {
		for(String key : messageLists.keySet()) {
			if(key.startsWith(name)) {
				return key;
			}
		}
		return null;
	}
	
	public void setList(String key, MessageList value) {
		if(value == null) {
			messageLists.remove(key);
		} else {
			messageLists.put(key, value);
		}
		schedule();
	}
	
	public void clear() {
		messageLists.clear();
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
