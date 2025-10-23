package com.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class JudgmentZoo extends JavaPlugin {
    private UDPServer udp;
    private MinecraftHandler handler;
    @Override
    public void onEnable() {
        handler = new MinecraftHandler(this);
        udp = new UDPServer(getLogger(), handler, this);
        udp.start();
    }

    @Override
    public void onDisable() {

    }
}
