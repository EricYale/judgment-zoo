package com.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Logger;

public class UDPServer extends Thread {
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    private Logger logger;
    private MinecraftHandler handler;
    private JavaPlugin plugin;

    public UDPServer(Logger logger, MinecraftHandler handler, JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = logger;
        this.handler = handler;
        try {
            socket = new DatagramSocket(4445);
            logger.info("UDP server up!");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        running = true;
        logger.info("Scanning for new packets");

        int[] buffer = new int[3];
        int bufferCount = 0;
        int bufferIndex = 0;

        while (running) {
            byte[] receiveBuf = new byte[256];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);

            try {
                socket.receive(receivePacket);
            } catch (IOException e) {
                logger.warning("Socket receive error: " + e.getMessage());
                continue;
            }

            String received = new String(receivePacket.getData(), 0, receivePacket.getLength());

            int asInt = Integer.parseInt(received);
            Bukkit.getScheduler().runTask(plugin, () -> handler.handleDistanceReading(asInt));
        }
        socket.close();
    }
}
