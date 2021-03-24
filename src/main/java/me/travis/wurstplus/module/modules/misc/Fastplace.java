// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.misc;

import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import me.travis.wurstplus.setting.Settings;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "Tickplace", category = Category.MISC)
public class Fastplace extends Module
{
    private Setting<Boolean> xp;
    
    public Fastplace() {
        this.xp = this.register(Settings.b("EXP & Crystal only", false));
    }
    
    @Override
    public void onUpdate() {
        if (this.isDisabled() || Fastplace.mc.player == null) {
            return;
        }
        if (this.xp.getValue()) {
            if (Fastplace.mc.player.inventory.getCurrentItem().getItem() instanceof ItemExpBottle || Fastplace.mc.player.inventory.getCurrentItem().getItem() instanceof ItemEndCrystal) {
                Fastplace.mc.rightClickDelayTimer = 0;
            }
        }
        else {
            Fastplace.mc.rightClickDelayTimer = 0;
        }
    }
}
