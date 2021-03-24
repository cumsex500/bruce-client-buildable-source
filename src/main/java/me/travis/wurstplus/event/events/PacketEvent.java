// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.event.events;

import net.minecraft.network.Packet;
import me.travis.wurstplus.event.wurstplusEvent;

public class PacketEvent extends wurstplusEvent
{
    private final Packet packet;
    
    public PacketEvent(final Packet packet) {
        this.packet = packet;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public static class Send extends PacketEvent
    {
        public Send(final Packet packet) {
            super(packet);
        }
    }
    
    public static class Receive extends PacketEvent
    {
        public Receive(final Packet packet) {
            super(packet);
        }
    }
}
