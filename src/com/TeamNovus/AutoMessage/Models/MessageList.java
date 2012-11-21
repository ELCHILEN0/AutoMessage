package com.TeamNovus.AutoMessage.Models;

import java.util.ArrayList;
import java.util.List;

public class MessageList {
	private Boolean enabled = true;
	private Integer interval = 45;
	private Boolean random = false;
	private String prefix = "[&bAutoMessage&r]";
	private List<String> messages = new ArrayList<String>();
	
	private Integer currentIndex = 0;
	
	public Boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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
	
	public void addMessage(String message) {
		this.messages.add(message);
	}
	
	public void addMessage(Integer index, String message) {
		try {
			this.messages.add(index, message);
		} catch (IndexOutOfBoundsException e) {
			this.messages.add(message);
		}
	}
	
	public boolean editMessage(Integer index, String message) {
		try {
			this.messages.set(index, message);
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public boolean removeMessage(Integer index) {
		try {
			this.messages.remove(index);
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public String getCurrentMessage() {
		if(currentIndex >= messages.size()) {
			currentIndex = 0;
		}
		
		try {
			return messages.get(currentIndex);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void setCurrentIndex(Integer currentIndex) {
		this.currentIndex = currentIndex;
	}
	
	public Integer getCurrentIndex() {
		return currentIndex;
	}
}
