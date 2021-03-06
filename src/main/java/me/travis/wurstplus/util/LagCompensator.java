// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.util;

import net.minecraft.util.math.MathHelper;
import java.util.Arrays;
import me.travis.wurstplus.wurstplusMod;
import java.util.function.Predicate;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import me.zero.alpine.listener.EventHandler;
import me.travis.wurstplus.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import java.util.EventListener;

public class LagCompensator implements EventListener
{
    public static LagCompensator INSTANCE;
    private final float[] tickRates;
    private int nextIndex;
    private long timeLastTimeUpdate;
    @EventHandler
    Listener<PacketEvent.Receive> packetEventListener;
    
    public LagCompensator() {
        this.tickRates = new float[20];
        this.nextIndex = 0;
        this.packetEventListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketTimeUpdate) {
                LagCompensator.INSTANCE.onTimeUpdate();
            }
            return;
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        wurstplusMod.EVENT_BUS.subscribe(this);
        this.reset();
    }
    
    public void reset() {
        this.nextIndex = 0;
        this.timeLastTimeUpdate = -1L;
        Arrays.fill(this.tickRates, 0.0f);
    }
    
    public float getTickRate() {
        float numTicks = 0.0f;
        float sumTickRates = 0.0f;
        for (final float tickRate : this.tickRates) {
            if (tickRate > 0.0f) {
                sumTickRates += tickRate;
                ++numTicks;
            }
        }
        return MathHelper.clamp(sumTickRates / numTicks, 0.0f, 20.0f);
    }
    
    public void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1L) {
            final float timeElapsed = (System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0f;
            this.tickRates[this.nextIndex % this.tickRates.length] = MathHelper.clamp(20.0f / timeElapsed, 0.0f, 20.0f);
            ++this.nextIndex;
        }
        this.timeLastTimeUpdate = System.currentTimeMillis();
    }
}
