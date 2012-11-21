package com.JnaniDev.AutoMessage.Models;

import java.util.ArrayList;
import java.util.List;

public class MessageList {
	private Integer currentMessage = 0;
	private Integer interval = 45;
	private Boolean random = false;
	private String prefix = "&r[&bAutoMessage&r] ";
	private List<String> messages = new ArrayList<String>();
	
	public Integer getCurrentMessage() {
		return currentMessage;
	}
	
	public void setCurrentMessage(Integer currentMessage) {
		this.currentMessage = currentMessage;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Boolean isRandom() {
		return random;
	}

	public void setRandom(Boolean random) {
		this.random = random;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	public String getMessage() {
		try {
			if(currentMessage >= messages.size())
				currentMessage = 0;
			return messages.get(currentMessage);
		} catch (Exception e) {
			return null;
		}
	}
	
	public void addMessage(String message) {
		this.messages.add(message);
	}
	
	public void removeMessage(Integer index) {
		this.messages.remove(index);
	}
	
	public void editMessage(Integer index, String message) {
		this.messages.set(index, message);
	}
}
