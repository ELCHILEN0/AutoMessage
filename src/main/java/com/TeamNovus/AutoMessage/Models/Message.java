package com.TeamNovus.AutoMessage.Models;

import java.util.LinkedList;
import java.util.List;

public class Message {
	private String format;
	private LinkedList<String> arguments = new LinkedList<String>();

	public Message(String format, String... arguments) {
		this.format = format;
		for(String arg : arguments) {
			this.arguments.add(arg);
		}
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

}
