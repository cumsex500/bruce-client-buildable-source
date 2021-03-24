// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus;

import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraft.client.multiplayer.ServerData;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;

public class DiscordPresence
{
    public static Minecraft mc;
    private static final String APP_ID = "739167888832856126";
    private static DiscordRichPresence presence;
    private static boolean hasStarted;
    public static String details;
    public static String state;
    public static int players;
    public static int maxPlayers;
    public static ServerData svr;
    public static String[] popInfo;
    public static int players2;
    public static int maxPlayers2;
    
    public static boolean start() {
        FMLLog.log.info("Starting Discord RPC");
        if (DiscordPresence.hasStarted) {
            return false;
        }
        DiscordPresence.hasStarted = true;
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2));
        DiscordRPC.discordInitialize("739167888832856126", handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        DiscordPresence.presence.details = "Bruce";
        DiscordPresence.presence.state = "Bruce ontop wtf?";
        DiscordPresence.presence.largeImageKey = "monkey";
        DiscordRPC.discordUpdatePresence(DiscordPresence.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    DiscordRPC.discordRunCallbacks();
                    DiscordPresence.details = "";
                    DiscordPresence.state = "";
                    DiscordPresence.players = 0;
                    DiscordPresence.maxPlayers = 0;
                    if (DiscordPresence.mc.isIntegratedServerRunning()) {
                        DiscordPresence.details = "Singleplayer";
                    }
                    else if (DiscordPresence.mc.getCurrentServerData() != null) {
                        DiscordPresence.svr = DiscordPresence.mc.getCurrentServerData();
                        if (!DiscordPresence.svr.serverIP.equals("")) {
                            DiscordPresence.details = "With the boys";
                            DiscordPresence.state = DiscordPresence.svr.serverIP;
                            if (DiscordPresence.svr.populationInfo != null) {
                                DiscordPresence.popInfo = DiscordPresence.svr.populationInfo.split("/");
                                if (DiscordPresence.popInfo.length > 2) {
                                    DiscordPresence.players2 = Integer.valueOf(DiscordPresence.popInfo[0]);
                                    DiscordPresence.maxPlayers2 = Integer.valueOf(DiscordPresence.popInfo[1]);
                                }
                            }
                            if (DiscordPresence.state.contains("2b2t.org")) {
                                try {
                                    if (wurstplusMod.lastChat.startsWith("Position in queue: ")) {
                                        DiscordPresence.state = DiscordPresence.state + " " + Integer.parseInt(wurstplusMod.lastChat.substring(19)) + " in queue";
                                    }
                                }
                                catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    else {
                        DiscordPresence.details = "Vibing";
                        DiscordPresence.state = "Listening to Bruce Willis";
                    }
                    if (!DiscordPresence.details.equals(DiscordPresence.presence.details) || !DiscordPresence.state.equals(DiscordPresence.presence.state)) {
                        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }
                    DiscordPresence.presence.details = DiscordPresence.details;
                    DiscordPresence.presence.state = DiscordPresence.state;
                    DiscordRPC.discordUpdatePresence(DiscordPresence.presence);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(5000L);
                }
                catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
            return;
        }, "Discord-RPC-Callback-Handler").start();
        FMLLog.log.info("Discord RPC initialised succesfully");
        return true;
    }
    
    private static void lambdastart() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                DiscordRPC.discordRunCallbacks();
                String details = "";
                String state = "";
                int players = 0;
                int maxPlayers = 0;
                if (DiscordPresence.mc.isIntegratedServerRunning()) {
                    details = "Lonely monkey";
                }
                else if (DiscordPresence.mc.getCurrentServerData() != null) {
                    final ServerData svr = DiscordPresence.mc.getCurrentServerData();
                    if (!svr.serverIP.equals("")) {
                        details = "With The Boys";
                        state = svr.serverIP;
                        if (svr.populationInfo != null) {
                            final String[] popInfo = svr.populationInfo.split("/");
                            if (popInfo.length > 2) {
                                players = Integer.valueOf(popInfo[0]);
                                maxPlayers = Integer.valueOf(popInfo[1]);
                            }
                        }
                        if (state.contains("2b2t.org")) {
                            try {
                                if (wurstplusMod.lastChat.startsWith("Queue simulator: ")) {
                                    state = state + " " + Integer.parseInt(wurstplusMod.lastChat.substring(19)) + " people infront";
                                }
                            }
                            catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else {
                    details = "Vibing";
                    state = "Listening to Bruce WIllis";
                }
                if (!details.equals(DiscordPresence.presence.details) || !state.equals(DiscordPresence.presence.state)) {
                    DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
                }
                DiscordPresence.presence.details = details;
                DiscordPresence.presence.state = state;
                DiscordRPC.discordUpdatePresence(DiscordPresence.presence);
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                Thread.sleep(5000L);
            }
            catch (InterruptedException e3) {
                e3.printStackTrace();
            }
        }
    }
    
    private static void lambdastart(final int var1, final String var2) {
        System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2);
    }
    
    public static void shutdown() {
        DiscordRPC.discordShutdown();
    }
    
    static {
        DiscordPresence.mc = Minecraft.getMinecraft();
        DiscordPresence.presence = new DiscordRichPresence();
        DiscordPresence.hasStarted = false;
    }
}
