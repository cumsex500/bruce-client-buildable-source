// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.render;

import me.travis.wurstplus.module.Module;

@Info(name = "NoHurtCam", category = Category.RENDER)
public class NoHurtCam extends Module
{
    private static NoHurtCam INSTANCE;
    
    public NoHurtCam() {
        NoHurtCam.INSTANCE = this;
    }
    
    public static boolean shouldDisable() {
        return NoHurtCam.INSTANCE != null && NoHurtCam.INSTANCE.isEnabled();
    }
}
