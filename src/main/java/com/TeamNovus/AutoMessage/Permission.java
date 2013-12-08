package com.TeamNovus.AutoMessage;

import org.bukkit.command.CommandSender;

public enum Permission {
<<<<<<< HEAD
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
	
=======
	COMMAND_ADD      ("lists.add",       true),
	COMMAND_EDIT     ("lists.edit",      true),
	COMMAND_REMOVE   ("lists.remove",    true),
	COMMAND_ENABLE   ("lists.enable",    true),
	COMMAND_INTERVAL ("lists.interval",  true),
	COMMAND_EXPIRY   ("lists.expiry",    true),
	COMMAND_RANDOM   ("lists.random",    true),
	COMMAND_PREFIX   ("lists.prefix",    true),
	COMMAND_SUFFIX   ("lists.suffix",    true),
	COMMAND_BROADCAST("lists.broadcast", true),
	COMMAND_LIST     ("lists.list",      true),
	COMMAND_UPDATE   ("update", false),
	COMMAND_RELOAD   ("reload", false),
	NONE("", false);
	
	private final String node;
	private final boolean multi;
	
>>>>>>> 2df7346d94aa70cd6fcf22505ef6a5c45e9d793e
	private Permission(String node, boolean multi) {
		this.node = node;
		this.multi = multi;
	}
	
	public boolean isMulti() {
		return multi;
	}
<<<<<<< HEAD
	
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
=======

	public static Boolean has(Permission permission, CommandSender target) {
		if(permission.multi)
			return target.hasPermission("automessage." + permission.node + ".*");
		return target.hasPermission("automessage." + permission.node);
	}

	public static Boolean has(Permission permission, CommandSender target, String list) {
		if(permission.multi)
		{
			if(target.hasPermission("automessage." + permission.node + ".*"))
				return true;
			if(list == null || "".equals(list))
				return false;
			return target.hasPermission("automessage." + permission.node + list);
		}
		return target.hasPermission("automessage." + permission.node);
>>>>>>> 2df7346d94aa70cd6fcf22505ef6a5c45e9d793e
	}
}
