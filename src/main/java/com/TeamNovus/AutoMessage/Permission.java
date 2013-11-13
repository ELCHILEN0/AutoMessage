package com.TeamNovus.AutoMessage;

import org.bukkit.command.CommandSender;

public enum Permission {
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
	
	private Permission(String node, boolean multi) {
		this.node = node;
		this.multi = multi;
	}
	
	public boolean isMulti() {
		return multi;
	}

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
	}
}
