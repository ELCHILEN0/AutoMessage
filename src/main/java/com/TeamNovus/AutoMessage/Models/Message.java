package com.TeamNovus.AutoMessage.Models;

import java.util.LinkedList;

import net.minecraft.server.v1_7_R1.ChatSerializer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Message {
	private String raw;

	public Message(String raw) {
		this.raw = raw;
	}
	
	public String getMessage() {
		return raw;
	}
	
	public Message setMessage(String raw) {
		this.raw = raw;
		
		return this;
	}
	
	public boolean isJsonMessage(int index) {
	    try {
	    	ChatSerializer.a(getMessages().get(index));
//	    	new JSONParser().parse(getMessages().get(index));

	    	return true;
	    } catch(Exception e) { 
	        return false;
	    }

	}
	
	public LinkedList<String> getMessages() {
		LinkedList<String> messages = new LinkedList<String>();
		
		for (String line : raw.split("\\\\n")) {
			if(!(line.startsWith("/"))) {
				messages.add(line);
			}
		}
		
		return messages;
	}
	
	public LinkedList<String> getCommands() {
		LinkedList<String> commands = new LinkedList<String>();
		
		for (String line : raw.split("\\\\n")) {
			if(line.startsWith("/")) {
				commands.add(line);
			}
		}
		
		return commands;
	}
	
}
