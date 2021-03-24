// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.event;

import me.travis.wurstplus.util.Wrapper;
import me.zero.alpine.type.Cancellable;

public class wurstplusEvent extends Cancellable
{
    private Era era;
    private final float partialTicks;
    
    public wurstplusEvent() {
        this.era = Era.PRE;
        this.partialTicks = Wrapper.getMinecraft().getRenderPartialTicks();
    }
    
    public Era getEra() {
        return this.era;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public enum Era
    {
        PRE, 
        PERI, 
        POST;
    }
}
