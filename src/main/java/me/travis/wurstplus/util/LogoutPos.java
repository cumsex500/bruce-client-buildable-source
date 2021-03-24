// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.util;

import net.minecraft.util.math.Vec3d;
import java.util.UUID;

public class LogoutPos
{
    final UUID id;
    final String name;
    final Vec3d maxs;
    final Vec3d mins;
    
    public LogoutPos(final UUID uuid, final String name, final Vec3d maxs, final Vec3d mins) {
        this.id = uuid;
        this.name = name;
        this.maxs = maxs;
        this.mins = mins;
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Vec3d getMaxs() {
        return this.maxs;
    }
    
    public Vec3d getMins() {
        return this.mins;
    }
    
    public Vec3d getTopVec() {
        return new Vec3d((this.getMins().x + this.getMaxs().x) / 2.0, this.getMaxs().y, (this.getMins().z + this.getMaxs().z) / 2.0);
    }
    
    @Override
    public boolean equals(final Object other) {
        return this == other || (other instanceof LogoutPos && this.getId().equals(((LogoutPos)other).getId()));
    }
    
    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
