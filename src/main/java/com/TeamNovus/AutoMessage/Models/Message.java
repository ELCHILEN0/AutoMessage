package com.TeamNovus.AutoMessage.Models;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Message {
	private String format;
	private LinkedList<String> arguments = new LinkedList<String>();

	public Message(String format, String... arguments) {
		this.format = format;
		for(String arg : arguments) {
			this.arguments.add(arg);
		}
	}
	
	public String getFormattedMessage() {
		return String.format(format, arguments.toArray());
	}
	
	public String getFormat() {
		return format;
	}
	
	public List<String> getArguments() {
		return arguments;
	}
	
	public void addArgument(String argument, int index) {
		try {
			arguments.add(index, argument);
		} catch (IndexOutOfBoundsException e) {
			arguments.add(argument);
		}
	}
	
	public void addArgument(String argument) {
		arguments.add(argument);
	}
	
	public LinkedList<String> getCommands() {
		LinkedList<String> commands = new LinkedList<String>();
		
		for (String line : StringUtils.split(getFormattedMessage(), "\\\n")) {
			if(line.startsWith("/")) {
				commands.add(line.replaceFirst("/", ""));
			}
		}
		
		return commands;
	}

	public LinkedList<String> getMessages() {
		LinkedList<String> messages = new LinkedList<String>();

		for (String line : StringUtils.split(getFormattedMessage(), "\\\n")) {
			if(!(line.startsWith("/"))) {
				messages.add(line);
			}
		}
		
		return messages;
	}
	
}
