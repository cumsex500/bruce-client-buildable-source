// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.misc;

import me.travis.wurstplus.command.Command;
import net.minecraft.util.math.MathHelper;
import me.travis.wurstplus.setting.Settings;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "PosLock", category = Category.MISC)
public class Poslock extends Module
{
    private Setting<Direction> direction;
    
    public Poslock() {
        this.direction = this.register(Settings.e("Direction", Direction.North));
    }
    
    @Override
    public void onUpdate() {
        if (this.isDisabled()) {
            return;
        }
        if (this.direction.getValue() == Direction.North) {
            Poslock.mc.player.rotationYaw = (float)MathHelper.clamp(180, -180, 180);
        }
        else if (this.direction.getValue() == Direction.South) {
            Poslock.mc.player.rotationYaw = (float)MathHelper.clamp(0, -180, 180);
        }
        else if (this.direction.getValue() == Direction.West) {
            Poslock.mc.player.rotationYaw = (float)MathHelper.clamp(90, -180, 180);
        }
        else if (this.direction.getValue() == Direction.East) {
            Poslock.mc.player.rotationYaw = -90.0f;
        }
        else {
            Command.sendChatMessage("what");
        }
    }
    
    public enum Direction
    {
        North, 
        South, 
        West, 
        East;
    }
}
