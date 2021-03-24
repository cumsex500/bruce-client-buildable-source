// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.util;

public final class Timer
{
    private long lastCheck;
    
    public Timer() {
        this.lastCheck = getSystemTime();
    }
    
    public boolean hasReach(final int targetTime) {
        return this.getTimePassed() >= targetTime;
    }
    
    public long getTimePassed() {
        return getSystemTime() - this.lastCheck;
    }
    
    public void reset() {
        this.lastCheck = getSystemTime();
    }
    
    public static long getSystemTime() {
        return System.nanoTime() / 1000000L;
    }
}
