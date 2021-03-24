// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.render;

import me.travis.wurstplus.util.Invdraw;
import me.travis.wurstplus.setting.Settings;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "Show INV", category = Category.RENDER)
public class InvPreview extends Module
{
    private Setting<Integer> xSetting;
    private Setting<Integer> ySetting;
    
    public InvPreview() {
        this.xSetting = this.register(Settings.i("X Coord", 784));
        this.ySetting = this.register(Settings.i("Y Coord", 46));
    }
    
    @Override
    public void onRender() {
        if (!this.isEnabled()) {
            return;
        }
        final Invdraw i = new Invdraw();
        i.drawInventory(this.xSetting.getValue(), this.ySetting.getValue(), InvPreview.mc.player);
    }
}
