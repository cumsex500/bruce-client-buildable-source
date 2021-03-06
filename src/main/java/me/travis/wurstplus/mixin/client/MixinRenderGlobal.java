// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.client.renderer.ChunkRenderContainer;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderGlobal.class })
public class MixinRenderGlobal
{
    @Shadow
    Minecraft mc;
    @Shadow
    public ChunkRenderContainer renderContainer;
    
    @Inject(method = { "renderBlockLayer(Lnet/minecraft/util/BlockRenderLayer;)V" }, at = { @At("HEAD") }, cancellable = true)
    public void renderBlockLayer(final BlockRenderLayer blockLayerIn, final CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}
