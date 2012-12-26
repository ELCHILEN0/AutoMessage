package com.TeamNovus.AutoMessage.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageList {
	private Boolean enabled = true;
	private Integer interval = 45;
	private Long expiry = -1L;
	private Boolean random = false;
	private String prefix = "[&bAutoMessage&r] ";
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
	
	public Long getExpiry() {
		return expiry;
	}
	
	public void setExpiry(Long expiry) {
		this.expiry = expiry;
	}
	
	public Boolean isExpired() {
		return System.currentTimeMillis() >= expiry && expiry != -1;
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
	
	public String getMessage(Integer index) {
		try {
			return this.messages.get(index.intValue());
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void addMessage(Integer index, String message) {
		try {
			this.messages.add(index.intValue(), message);
		} catch (IndexOutOfBoundsException e) {
			this.messages.add(message);
		}
	}
	
	public boolean editMessage(Integer index, String message) {
		try {
			return this.messages.set(index.intValue(), message) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public boolean removeMessage(Integer index) {
		try {
			return this.messages.remove(index.intValue()) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public String getCurrentMessage() {
		if(currentIndex >= messages.size()) {
			currentIndex = 0;
		}
		
		if(random) {
			currentIndex = new Random().nextInt(messages.size());
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
