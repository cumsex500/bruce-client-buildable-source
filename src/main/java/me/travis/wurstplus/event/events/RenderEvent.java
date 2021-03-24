// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.event.events;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.Tessellator;
import me.travis.wurstplus.event.wurstplusEvent;

public class RenderEvent extends wurstplusEvent
{
    private final Tessellator tessellator;
    private final Vec3d renderPos;
    
    public RenderEvent(final Tessellator tessellator, final Vec3d renderPos) {
        this.tessellator = tessellator;
        this.renderPos = renderPos;
    }
    
    public Tessellator getTessellator() {
        return this.tessellator;
    }
    
    public BufferBuilder getBuffer() {
        return this.tessellator.getBuffer();
    }
    
    public Vec3d getRenderPos() {
        return this.renderPos;
    }
    
    public void setTranslation(final Vec3d translation) {
        this.getBuffer().setTranslation(-translation.x, -translation.y, -translation.z);
    }
    
    public void resetTranslation() {
        this.setTranslation(this.renderPos);
    }
}
