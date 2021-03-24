// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.misc;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.client.Minecraft;
import java.util.function.Predicate;
import net.minecraft.network.play.server.SPacketChunkData;
import me.travis.wurstplus.event.events.EventStageable;
import me.travis.wurstplus.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.travis.wurstplus.event.events.EventReceivePacket;
import me.zero.alpine.listener.Listener;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "AntiChunkBan", category = Category.MISC)
public class AntiChunkBan extends Module
{
    private static long startTime;
    private double delayTime;
    private Setting<ModeThing> modeThing;
    private Setting<Boolean> disable;
    @EventHandler
    private Listener<EventReceivePacket> packetEventSendListener;
    
    public AntiChunkBan() {
        this.delayTime = 10.0;
        this.modeThing = this.register(Settings.e("Mode", ModeThing.PACKET));
        this.disable = this.register(Settings.b("Disable for Kill mode", false));
        this.packetEventSendListener = new Listener<EventReceivePacket>(event -> {
            if (this.modeThing.getValue().equals(ModeThing.PACKET) && event.getStage() == EventStageable.EventStage.PRE && event.getPacket() instanceof SPacketChunkData) {
                event.setCanceled(true);
            }
        }, (Predicate<EventReceivePacket>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (AntiChunkBan.mc.player == null) {
            return;
        }
        if (this.modeThing.getValue().equals(ModeThing.KILL) && Minecraft.getMinecraft().getCurrentServerData() != null) {
            if (AntiChunkBan.startTime == 0L) {
                AntiChunkBan.startTime = System.currentTimeMillis();
            }
            if (AntiChunkBan.startTime + this.delayTime <= System.currentTimeMillis()) {
                if (Minecraft.getMinecraft().getCurrentServerData() != null) {
                    Minecraft.getMinecraft().playerController.connection.sendPacket((Packet)new CPacketChatMessage("/kill"));
                }
                if (AntiChunkBan.mc.player.getHealth() <= 0.0f) {
                    AntiChunkBan.mc.player.respawnPlayer();
                    AntiChunkBan.mc.displayGuiScreen((GuiScreen)null);
                    if (this.disable.getValue()) {
                        this.disable();
                    }
                }
                AntiChunkBan.startTime = System.currentTimeMillis();
            }
        }
    }
    
    static {
        AntiChunkBan.startTime = 0L;
    }
    
    private enum ModeThing
    {
        PACKET, 
        KILL;
    }
}
