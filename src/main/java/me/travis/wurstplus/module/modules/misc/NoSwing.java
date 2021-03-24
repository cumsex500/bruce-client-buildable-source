// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.misc;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketAnimation;
import me.zero.alpine.listener.EventHandler;
import me.travis.wurstplus.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.travis.wurstplus.module.Module;

@Info(name = "NoSwing", category = Category.EXPLOITS)
public class NoSwing extends Module
{
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    
    public NoSwing() {
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketAnimation) {
                event.cancel();
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
