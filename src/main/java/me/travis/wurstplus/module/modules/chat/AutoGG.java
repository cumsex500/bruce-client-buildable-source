// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.chat;

import java.util.Objects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
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

@Info(name = "AutoGG", category = Category.CHAT)
public class AutoGG extends Module
{
    private ConcurrentHashMap<String, Integer> targetedPlayers;
    private Setting<Boolean> toxicMode;
    private Setting<Boolean> clientName;
    private Setting<Integer> timeoutTicks;
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    @EventHandler
    public Listener<LivingDeathEvent> livingDeathEventListener;
    
    public AutoGG() {
        this.targetedPlayers = null;
        this.toxicMode = this.register(Settings.b("ToxicMode", false));
        this.clientName = this.register(Settings.b("ClientName", true));
        this.timeoutTicks = this.register(Settings.i("TimeoutTicks", 20));
        CPacketUseEntity cPacketUseEntity;
        Entity targetEntity;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (AutoGG.mc.player == null) {
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
                        targetEntity = cPacketUseEntity.getEntityFromWorld((World)AutoGG.mc.world);
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
        EntityLivingBase entity;
        EntityPlayer player;
        String name;
        this.livingDeathEventListener = new Listener<LivingDeathEvent>(event -> {
            if (AutoGG.mc.player != null) {
                if (this.targetedPlayers == null) {
                    this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
                }
                entity = event.getEntityLiving();
                if (entity != null) {
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
        if (this.isDisabled() || AutoGG.mc.player == null) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        for (final Entity entity : AutoGG.mc.world.getLoadedEntityList()) {
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
        if (this.toxicMode.getValue()) {
            message.append("oo aa me 32k you... mad?");
        }
        else {
            message.append("this is sad... i just clowned you ");
        }
        message.append(name);
        message.append("!");
        if (this.clientName.getValue()) {
            message.append(" ");
            message.append("Bruce Client");
            message.append(" ");
        }
        String messageSanitized = message.toString().replaceAll("§", "");
        if (messageSanitized.length() > 255) {
            messageSanitized = messageSanitized.substring(0, 255);
        }
        AutoGG.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(messageSanitized));
    }
    
    public void addTargetedPlayer(final String name) {
        if (Objects.equals(name, AutoGG.mc.player.getName())) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        this.targetedPlayers.put(name, this.timeoutTicks.getValue());
    }
}
