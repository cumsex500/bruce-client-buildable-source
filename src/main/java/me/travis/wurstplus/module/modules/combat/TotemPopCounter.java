// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.combat;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import me.travis.wurstplus.wurstplusMod;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import java.util.function.Predicate;
import me.travis.wurstplus.command.Command;
import me.travis.wurstplus.setting.Settings;
import me.travis.wurstplus.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import me.travis.wurstplus.event.events.TotemPopEvent;
import me.zero.alpine.listener.Listener;
import me.travis.wurstplus.setting.Setting;
import java.util.HashMap;
import me.travis.wurstplus.module.Module;

@Info(name = "TotemPopCounter", description = "Counts the times your enemy pops", category = Category.COMBAT)
public class TotemPopCounter extends Module
{
    private HashMap<String, Integer> popList;
    private Setting<colour> mode;
    @EventHandler
    public Listener<TotemPopEvent> totemPopEvent;
    @EventHandler
    public Listener<PacketEvent.Receive> totemPopListener;
    
    public TotemPopCounter() {
        this.popList = new HashMap<String, Integer>();
        this.mode = this.register(Settings.e("Colour", colour.DARK_PURPLE));
        int popCounter;
        int newPopCounter;
        this.totemPopEvent = new Listener<TotemPopEvent>(event -> {
            if (this.popList == null) {
                this.popList = new HashMap<String, Integer>();
            }
            if (this.popList.get(event.getEntity().getName()) == null) {
                this.popList.put(event.getEntity().getName(), 1);
                Command.sendChatMessage(this.colourchoice() + event.getEntity().getName() + " popped " + 1 + " totem");
            }
            else if (this.popList.get(event.getEntity().getName()) != null) {
                popCounter = this.popList.get(event.getEntity().getName());
                newPopCounter = ++popCounter;
                this.popList.put(event.getEntity().getName(), newPopCounter);
                Command.sendChatMessage(this.colourchoice() + event.getEntity().getName() + " popped " + newPopCounter + " totems kek");
            }
            return;
        }, (Predicate<TotemPopEvent>[])new Predicate[0]);
        SPacketEntityStatus packet;
        Entity entity;
        this.totemPopListener = new Listener<PacketEvent.Receive>(event -> {
            if (TotemPopCounter.mc.world != null && TotemPopCounter.mc.player != null) {
                if (event.getPacket() instanceof SPacketEntityStatus) {
                    packet = (SPacketEntityStatus)event.getPacket();
                    if (packet.getOpCode() == 35) {
                        entity = packet.getEntity((World)TotemPopCounter.mc.world);
                        wurstplusMod.EVENT_BUS.post(new TotemPopEvent(entity));
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        for (final EntityPlayer player : TotemPopCounter.mc.world.playerEntities) {
            if (player.getHealth() <= 0.0f && this.popList.containsKey(player.getName())) {
                Command.sendChatMessage(this.colourchoice() + player.getName() + " died after popping " + this.popList.get(player.getName()) + " totems Ezzzzz");
                this.popList.remove(player.getName(), this.popList.get(player.getName()));
            }
        }
    }
    
    private String colourchoice() {
        switch (this.mode.getValue()) {
            case BLACK: {
                return "&0";
            }
            case RED: {
                return "&c";
            }
            case AQUA: {
                return "&b";
            }
            case BLUE: {
                return "&9";
            }
            case GOLD: {
                return "&6";
            }
            case GRAY: {
                return "&7";
            }
            case WHITE: {
                return "&f";
            }
            case GREEN: {
                return "&a";
            }
            case YELLOW: {
                return "&e";
            }
            case DARK_RED: {
                return "&4";
            }
            case DARK_AQUA: {
                return "&3";
            }
            case DARK_BLUE: {
                return "&1";
            }
            case DARK_GRAY: {
                return "&8";
            }
            case DARK_GREEN: {
                return "&2";
            }
            case DARK_PURPLE: {
                return "&5";
            }
            case LIGHT_PURPLE: {
                return "&d";
            }
            default: {
                return "";
            }
        }
    }
    
    private enum colour
    {
        BLACK, 
        DARK_BLUE, 
        DARK_GREEN, 
        DARK_AQUA, 
        DARK_RED, 
        DARK_PURPLE, 
        GOLD, 
        GRAY, 
        DARK_GRAY, 
        BLUE, 
        GREEN, 
        AQUA, 
        RED, 
        LIGHT_PURPLE, 
        YELLOW, 
        WHITE;
    }
}
