// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.renderer.GlStateManager;
import me.travis.wurstplus.module.ModuleManager;
import me.travis.wurstplus.util.Wrapper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBoat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ModelBoat.class })
public class MixinModelBoat
{
    @Inject(method = { "render" }, at = { @At("HEAD") })
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo info) {
        if (Wrapper.getPlayer().getRidingEntity() == entityIn && ModuleManager.isModuleEnabled("EntitySpeed")) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
            GlStateManager.enableBlend();
        }
    }
}
