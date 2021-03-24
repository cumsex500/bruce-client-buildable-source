// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.chat;

import java.util.Objects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.travis.wurstplus.module.ModuleManager;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import java.util.function.Predicate;
import me.travis.wurstplus.util.EntityUtil;
import net.minecraft.world.World;
import net.minecraft.network.play.client.CPacketUseEntity;
import me.travis.wurstplus.setting.Settings;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import me.zero.alpine.listener.EventHandler;
import me.travis.wurstplus.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.travis.wurstplus.setting.Setting;
import java.util.concurrent.ConcurrentHashMap;
import me.travis.wurstplus.module.Module;

@Info(name = "AutoEZ", category = Category.CHAT)
public class AutoEZ extends Module
{
    private ConcurrentHashMap<String, Integer> targetedPlayers;
    private Setting<Boolean> greenMode;
    private Setting<Boolean> toxicMode;
    private Setting<Boolean> ezMode;
    private Setting<Integer> timeoutTicks;
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    @EventHandler
    public Listener<LivingDeathEvent> livingDeathEventListener;
    
    public AutoEZ() {
        this.targetedPlayers = null;
        this.greenMode = this.register(Settings.b("Green Text", false));
        this.toxicMode = this.register(Settings.b("ToxicMode", false));
        this.ezMode = this.register(Settings.b("EzMode", false));
        this.timeoutTicks = this.register(Settings.i("TimeoutTicks", 20));
        CPacketUseEntity cPacketUseEntity;
        Entity targetEntity;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (AutoEZ.mc.player == null) {
                return;
            }
            else {
                if (this.targetedPlayers == null) {
                    this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
                }
                if (!(event.getPacket() instanceof CPacketUseEntity)) {
                    return;
                }
                else {
                    cPacketUseEntity = (CPacketUseEntity)event.getPacket();
                    if (!cPacketUseEntity.getAction().equals((Object)CPacketUseEntity.Action.ATTACK)) {
                        return;
                    }
                    else {
                        targetEntity = cPacketUseEntity.getEntityFromWorld((World)AutoEZ.mc.world);
                        if (!EntityUtil.isPlayer(targetEntity)) {
                            return;
                        }
                        else {
                            this.addTargetedPlayer(targetEntity.getName());
                            return;
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        final EntityLivingBase entityLivingBase;
        EntityLivingBase entity;
        EntityPlayer player;
        String name;
        this.livingDeathEventListener = new Listener<LivingDeathEvent>(event -> {
            if (AutoEZ.mc.player != null) {
                if (this.targetedPlayers == null) {
                    this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
                }
                event.getEntityLiving();
                if ((entity = entityLivingBase) != null) {
                    if (!(!EntityUtil.isPlayer((Entity)entity))) {
                        player = (EntityPlayer)entity;
                        if (player.getHealth() <= 0.0f) {
                            name = player.getName();
                            if (this.shouldAnnounce(name)) {
                                this.doAnnounce(name);
                            }
                        }
                    }
                }
            }
        }, (Predicate<LivingDeathEvent>[])new Predicate[0]);
    }
    
    public void onEnable() {
        this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
    }
    
    public void onDisable() {
        this.targetedPlayers = null;
    }
    
    @Override
    public void onUpdate() {
        if (this.isDisabled() || AutoEZ.mc.player == null) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        for (final Entity entity : AutoEZ.mc.world.getLoadedEntityList()) {
            if (!EntityUtil.isPlayer(entity)) {
                continue;
            }
            final EntityPlayer player = (EntityPlayer)entity;
            if (player.getHealth() > 0.0f) {
                continue;
            }
            final String name2 = player.getName();
            if (this.shouldAnnounce(name2)) {
                this.doAnnounce(name2);
                break;
            }
        }
        this.targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.targetedPlayers.remove(name);
            }
            else {
                this.targetedPlayers.put(name, timeout - 1);
            }
        });
    }
    
    private boolean shouldAnnounce(final String name) {
        return this.targetedPlayers.containsKey(name);
    }
    
    private void doAnnounce(final String name) {
        this.targetedPlayers.remove(name);
        final StringBuilder message = new StringBuilder();
        if (this.greenMode.getValue()) {
            message.append("> ");
        }
        if (ModuleManager.getModuleByName("BetterAuto32k").isEnabled()) {
            message.append("You have been added to the montage EZZ ");
        }
        if (ModuleManager.getModuleByName("Auto32kVersionOne").isEnabled()) {
            message.append("popbob ezz ");
        }
        else if (this.ezMode.getValue()) {
            message.append("oo aa me 32k you.. mad? ");
        }
        else if (this.toxicMode.getValue()) {
            message.append("top 10 monkey moments!");
        }
        else {
            message.append("Bruce Client on top");
        }
        message.append("");
        String messageSanitized;
        if ((messageSanitized = message.toString().replaceAll("§", "")).length() > 255) {
            messageSanitized = messageSanitized.substring(0, 255);
        }
        AutoEZ.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(messageSanitized));
    }
    
    public void addTargetedPlayer(final String name) {
        if (Objects.equals(name, AutoEZ.mc.player.getName())) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        this.targetedPlayers.put(name, this.timeoutTicks.getValue());
    }
}
