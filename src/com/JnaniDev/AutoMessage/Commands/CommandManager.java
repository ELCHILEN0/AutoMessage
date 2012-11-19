package com.JnaniDev.AutoMessage.Commands;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CommandManager {
	private LinkedHashMap<BaseCommand, Method> commands = new LinkedHashMap<BaseCommand, Method>();
	
	/**
	 * Register all the commands annotated with @BaseCommand.
	 * Commands are ordered by their order of registration.
	 * When a class is registered the commands are registered
	 * from top to bottom.
	 */
	public void registerClass(Class<?> commandClass) {
		// Loop through all the classes methods
        Method[] methods = commandClass.getMethods();
        for (Method method : methods) {
        	// Check if BaseCommand is part of the method
            if (method.isAnnotationPresent(BaseCommand.class)) {
                // Get the method and annotation and place it in the map
        		commands.put(method.getAnnotation(BaseCommand.class), method);
            }
        }
	}
	
	/**
	 * Unregister all commands.
	 */
	public void unregisterCommands() {
		commands.clear();
	}
	
	/**
	 * Return a list of the BaseCommands that are registered.
	 * If no commands are present it will return an empty list.
	 */
	public ArrayList<BaseCommand> getCommands() {
		ArrayList<BaseCommand> cmds = new ArrayList<BaseCommand>();
		for(BaseCommand command : commands.keySet()) {
			cmds.add(command);
		}
		return cmds;
	}
	
	/**
	 * Fetch a BaseCommand object by searching for an alias.
	 * It will return the first command where the alias
	 * matches the specified commandLabel.
	 */
	public BaseCommand getCommand(String commandLabel) {
		for(BaseCommand command : commands.keySet()) {
			for(String alias : command.aliases()) {
				if(alias.equalsIgnoreCase(commandLabel)) {
					return command;
				}
			}
		}
		return null;
	}
	
	/**
	 * Invoke a command of by using the BaseCommand object.
	 * The CommandSender, CommandLabel, Arguments and a JavaPlugin 
	 * object should all be used when the command is sent.
	 */
	public void dispatchCommand(BaseCommand command, Object... object) {
		try {
			// Invoke the command by initializing a new class and sending the arguments to the method
			commands.get(command).invoke(commands.get(command).getDeclaringClass().newInstance(), object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
