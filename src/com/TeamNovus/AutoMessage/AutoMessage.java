package com.TeamNovus.AutoMessage;

import org.bukkit.plugin.java.JavaPlugin;

public class AutoMessage extends JavaPlugin {
	private static AutoMessage plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
	}

	@Override
	public void onDisable() {
		plugin = null;
	}

	public static AutoMessage getPlugin() {
		return plugin;
	}
	
}
