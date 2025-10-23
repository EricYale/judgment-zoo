package com.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fox;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Arrow;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class MinecraftHandler {
    World world;
    Location cageCenter;
    Location chickenSpawnpoint;
    private JavaPlugin plugin;
    private long lastActionTime = 0; // cooldown tracker

    private List<Integer> distanceBuffer = new ArrayList<>();
    private boolean waitingForSamples = false;

    public MinecraftHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        for(World w : Bukkit.getWorlds()) {
            if(w.getEnvironment() == World.Environment.NORMAL) {
                world = w;
                break;
            }
        }

        cageCenter = new Location(world, -4, 68, 81);
        chickenSpawnpoint = new Location(world, -4, 80, 81);

        // Register join listener
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                Player player = event.getPlayer();
                player.setGameMode(GameMode.SPECTATOR);
                Location tpLoc = new Location(world, -9.2, 70.5, 82.4, -90f, 17f); // yaw 90 = east, pitch 17
                player.teleport(tpLoc);
            }
        }, plugin);
    }

    public void handleDistanceReading(int distance) {
        if(world.getPlayers().isEmpty()) return;
        plugin.getLogger().info("Distance reading: " + distance);

        summonAnimalIfNotExist();

        long now = System.currentTimeMillis();
        boolean cooldownActive = (now - lastActionTime < 7000);

        if (!waitingForSamples) {
            if (distance < 6) {
                distanceBuffer.clear();
                distanceBuffer.add(distance);
                waitingForSamples = true;
            }
        } else {
            distanceBuffer.add(distance);
            if (distanceBuffer.size() == 4) {
                int minDistance = distanceBuffer.stream().min(Integer::compareTo).orElse(Integer.MAX_VALUE);
                if (cooldownActive) {
                    // Reset buffer and state, but skip action due to cooldown
                    distanceBuffer.clear();
                    waitingForSamples = false;
                    return;
                }
                if (minDistance < 4) {
                    harmFox();
                } else {
                    feedFox();
                }
                lastActionTime = now;
                distanceBuffer.clear();
                waitingForSamples = false;
            }
        }
    }

    public void feedFox() {
        if (world == null || chickenSpawnpoint == null) return;
        world.spawn(chickenSpawnpoint, org.bukkit.entity.Chicken.class);
    }

    public void harmFox() {
        if (world == null || plugin == null) return;

        Collection<Entity> nearbyEntities = world.getNearbyEntities(cageCenter, 20, 20, 20);
        Fox targetFox = null;
        for (Entity entity : nearbyEntities) {
            if (entity.getType() == EntityType.FOX && entity instanceof Fox) {
                targetFox = (Fox) entity;
                break;
            }
        }
        if (targetFox == null) {
            plugin.getLogger().warning("Warning: no fox found to harm!");
            return;
        }

        // Spawn 10 arrows above the fox, 50ms (1 tick) apart
        final Location baseLoc = targetFox.getLocation();

        for (int i = 0; i < 10; i++) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Location spawnLoc = baseLoc.clone().add(0, 13, 0);
                Arrow arrow = world.spawn(spawnLoc, Arrow.class);
            }, i);
        }
    }

    public void summonAnimalIfNotExist() {
        Collection<Entity> nearbyEntities = world.getNearbyEntities(cageCenter, 20, 20, 20);

        for (Entity entity : nearbyEntities) {
            if (entity.getType() == EntityType.FOX) return;
        }

        world.spawn(cageCenter, Fox.class);
    }
}
