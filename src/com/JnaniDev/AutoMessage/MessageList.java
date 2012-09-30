package com.JnaniDev.AutoMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

public class MessageList {
	private int messageIndex;
	private int interval;
	private boolean random;
	private String prefix;
	private List<String> messages;
	
	// Initializers
	public MessageList(Object object) {
		if((object instanceof ConfigurationSection)) {
			ConfigurationSection section = (ConfigurationSection) object;
			this.interval = section.getInt("interval");
			this.random = section.getBoolean("random");
			this.prefix = section.getString("prefix");
			this.messages = section.getStringList("messages");
			this.messageIndex = 0;
		} else {
			new MessageList();
		}
	}
	
	public MessageList() {
		this.interval = 45;
		this.random = false;
		this.prefix = "&r[&bAutoMsg&r] ";
		this.messages = new ArrayList<String>();
		this.messageIndex = 0;
	}
	
	// Hash conversions
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("interval", interval);
		map.put("random", random);
		map.put("prefix", prefix);
		map.put("messages", messages);
		return map;
	}
	
	// Interval
	public boolean getRandom() {
		return random;
	}
	
	public void setRandom(boolean random) {
		this.random = random;
	}
	
	// Interval
	public int getInterval() {
		return interval;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	// Prefix operations
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	// Message Index
	public void setMessageIndex(int index) {
		if(index < messages.size())
			this.messageIndex = index;
		else
			this.messageIndex = 0;
	}
	
	public int getMessageIndex() {
		return messageIndex;
	}
	
	// Message Getters
	public List<String> getMessages() {
		return messages;
	}
	
	public String[] getMessage(int index) {
		return messages.get(index).split("\\$n");
	}
	
	public String[] getCurrentMessage() {
		return getMessage(messageIndex);
	}
	
	public int getTotalMessages() {
		return messages.size();
	}
	
	// Message Setters
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	public void addMessage(String message) {
		this.messages.add(message);
	}
	
	public void addMessage(int index, String message) {
		this.messages.add(index, message);
	}
	
	public void editMessage(int index, String message) {
		this.messages.set(index, message);
	}
	
	public void removeMessage(int index) {
		this.messages.remove(index);
	}
}
