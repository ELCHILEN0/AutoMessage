package com.TeamNovus.AutoMessage;

import org.bukkit.command.CommandSender;

public enum Permission {
	COMMAND_UPDATE("commands.update"),
	COMMAND_RELOAD("commands.reload"),
	COMMAND_ADD("commands.add"),
	COMMAND_EDIT("commands.edit"),
	COMMAND_REMOVE("commands.remove"),
	COMMAND_ENABLE("commands.enable"),
	COMMAND_INTERVAL("commands.interval"),
	COMMAND_EXPIRY("commands.expiry"),
	COMMAND_RANDOM("commands.random"),
	COMMAND_PREFIX("commands.prefix"),
	COMMAND_SUFFIX("commands.suffix"),
	COMMAND_BROADCAST("commands.broadcast"),
	COMMAND_LIST("commands.list"),
	NONE("");
	
	private String node;
	
	private Permission(String node) {
		this.node = node;
	}
	
	public String getNode() {
		return node;
	}
	
	private static String getPermission(Permission permission) {
		return "supernaturals." + permission.getNode();
	}
	
	public static Boolean has(Permission permission, CommandSender target) {
		return target.hasPermission(getPermission(permission));
	}
}
