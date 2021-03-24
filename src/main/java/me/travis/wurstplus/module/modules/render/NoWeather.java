// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.render;

import me.travis.wurstplus.module.Module;

@Info(name = "NoWeather", description = "Removes Weather", category = Category.RENDER)
public class NoWeather extends Module
{
    @Override
    public void onUpdate() {
        if (this.isDisabled()) {
            return;
        }
        if (NoWeather.mc.world.isRaining()) {
            NoWeather.mc.world.setRainStrength(0.0f);
        }
        if (NoWeather.mc.world.isThundering()) {
            NoWeather.mc.world.setThunderStrength(0.0f);
        }
    }
}
