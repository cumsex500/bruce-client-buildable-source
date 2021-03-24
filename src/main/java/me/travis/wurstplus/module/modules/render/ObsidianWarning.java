// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.render;

import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemTool;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.travis.wurstplus.util.Friends;
import java.util.Collection;
import java.util.ArrayList;
import me.travis.wurstplus.command.Command;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.function.Predicate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import java.util.Objects;
import me.travis.wurstplus.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.travis.wurstplus.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "ObsidianWarning", category = Category.RENDER)
public class ObsidianWarning extends Module
{
    private Setting<Integer> distanceToDetect;
    private Setting<Boolean> announce;
    private Setting<Integer> chatDelay;
    private int delay;
    @EventHandler
    public Listener<PacketEvent.Receive> packetReceiveListener;
    
    public ObsidianWarning() {
        this.distanceToDetect = this.register((Setting<Integer>)Settings.integerBuilder("Max Break Distance").withMinimum(1).withValue(2).withMaximum(5).build());
        this.announce = this.register(Settings.b("Announce in chat", false));
        this.chatDelay = this.register(Settings.integerBuilder("Chat Delay").withMinimum(14).withValue(18).withMaximum(25).withVisibility(o -> this.announce.getValue()).build());
        final EntityPlayerSP player;
        final WorldClient world;
        SPacketBlockBreakAnim packet;
        BlockPos pos;
        this.packetReceiveListener = new Listener<PacketEvent.Receive>(event -> {
            player = ObsidianWarning.mc.player;
            world = ObsidianWarning.mc.world;
            if (!Objects.isNull(player) && !Objects.isNull(world)) {
                if (event.getPacket() instanceof SPacketBlockBreakAnim) {
                    packet = (SPacketBlockBreakAnim)event.getPacket();
                    pos = packet.getPosition();
                    if (this.pastDistance((EntityPlayer)player, pos, this.distanceToDetect.getValue())) {
                        this.sendChat();
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    private boolean pastDistance(final EntityPlayer player, final BlockPos pos, final double dist) {
        return player.getDistanceSqToCenter(pos) <= Math.pow(dist, 2.0);
    }
    
    public void sendChat() {
        if (this.delay > this.chatDelay.getValue() && this.announce.getValue()) {
            this.delay = 0;
            ObsidianWarning.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("Hey " + this.getPlayer() + " please stop breaking that block x"));
        }
        Command.sendChatMessage("§4Someone is mining ur box mate, id watch out");
        ++this.delay;
    }
    
    public String getPlayer() {
        final List<EntityPlayer> entities = new ArrayList<EntityPlayer>();
        entities.addAll((Collection<? extends EntityPlayer>)ObsidianWarning.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        for (final EntityPlayer e : entities) {
            if (!e.isDead) {
                if (e.getHealth() <= 0.0f) {
                    continue;
                }
                if (e.getName() == ObsidianWarning.mc.player.getName()) {
                    continue;
                }
                if (e.getHeldItemMainhand().getItem() instanceof ItemTool) {
                    return e.getName();
                }
                continue;
            }
        }
        return "";
    }
}
