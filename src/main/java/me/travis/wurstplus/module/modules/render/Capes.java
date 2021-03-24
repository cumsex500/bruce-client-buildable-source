// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.render;

import me.travis.wurstplus.module.Module;

@Info(name = "Capes", category = Category.RENDER)
public class Capes extends Module
{
    private static Capes INSTANCE;
    
    public Capes() {
        Capes.INSTANCE = this;
    }
    
    public static boolean isActive() {
        return Capes.INSTANCE.isEnabled();
    }
}
