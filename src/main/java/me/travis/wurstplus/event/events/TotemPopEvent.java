// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.event.events;

import net.minecraft.entity.Entity;
import me.travis.wurstplus.event.wurstplusEvent;

public class TotemPopEvent extends wurstplusEvent
{
    private Entity entity;
    
    public TotemPopEvent(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
