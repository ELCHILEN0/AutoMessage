package com.TeamNovus.AutoMessage;

import org.bukkit.command.CommandSender;

public enum Permission {
	COMMAND_UPDATE		("automessage.commands.update"),
	COMMAND_RELOAD		("automessage.commands.reload"),
	COMMAND_ADD			("automessage.commands.add"),
	COMMAND_EDIT		("automessage.commands.edit"),
	COMMAND_REMOVE		("automessage.commands.remove"),
	COMMAND_ENABLE		("automessage.commands.enable"),
	COMMAND_INTERVAL	("automessage.commands.interval"),
	COMMAND_EXPIRY		("automessage.commands.expiry"),
	COMMAND_RANDOM		("automessage.commands.random"),
	COMMAND_PREFIX		("automessage.commands.prefix"),
	COMMAND_SUFFIX		("automessage.commands.suffix"),
	COMMAND_BROADCAST	("automessage.commands.broadcast"),
	COMMAND_LIST		("automessage.commands.list"),
	NONE("");
	
	private String node;
	private boolean multi;
	
	private Permission(String node) {
		this(node, false);
	}
	
	private Permission(String node, boolean multi) {
		this.node = node;
		this.multi = multi;
	}
	
	public String getNode() {
		return node;
	}
	
	public boolean isMulti() {
		return multi;
	}
	
	public static boolean has(Permission permission, String appending, CommandSender target) {
		if(appending != null && permission.isMulti()) {
			return target.hasPermission(permission.getNode() + "." + appending) || target.hasPermission(permission.getNode() + ".*");
		}
		
		return target.hasPermission(permission.getNode());
	}
	
	public static Boolean has(Permission permission, CommandSender target) {
		return has(permission, null, target);
	}
}
