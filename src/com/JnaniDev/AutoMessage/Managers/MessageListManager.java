package com.JnaniDev.AutoMessage.Managers;

import java.util.HashMap;

import com.JnaniDev.AutoMessage.Models.MessageList;

public class MessageListManager {
	private HashMap<String, MessageList> lists = new HashMap<String, MessageList>();
	
	public HashMap<String, MessageList> getLists() {
		return lists;
	}
	
	public void setLists(HashMap<String, MessageList> lists) {
		this.lists = lists;
	}
	
	public void putList(String name, MessageList list) {
		lists.put(name, list);
	}
	
	public void deleteList(String name) {
		lists.remove(name);
	}

	public MessageList getList(String name) {
		return lists.get(name);
	}
}
