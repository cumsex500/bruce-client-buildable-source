// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.render;

import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import me.travis.wurstplus.util.wurstplusTessellator;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import me.travis.wurstplus.event.events.RenderEvent;
import me.travis.wurstplus.setting.Settings;
import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "Block Highlight", category = Category.RENDER)
public class Blockhighlight extends Module
{
    private Setting<Float> width;
    private Setting<Boolean> rainbow;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Float> satuation;
    private Setting<Float> brightness;
    private Setting<Integer> speed;
    private Setting<Integer> alpha;
    private Setting<RenderMode> renderMode;
    private BlockPos renderBlock;
    private float hue;
    private Color rgbc;
    
    public Blockhighlight() {
        this.width = this.register((Setting<Float>)Settings.floatBuilder("Width").withMinimum(0.0f).withValue(2.5f).build());
        this.rainbow = this.register(Settings.b("RainbowMode", false));
        this.red = this.register(Settings.integerBuilder("Red").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.green = this.register(Settings.integerBuilder("Green").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.blue = this.register(Settings.integerBuilder("Blue").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.satuation = this.register(Settings.floatBuilder("Saturation").withRange(0.0f, 1.0f).withValue(0.6f).withVisibility(o -> this.rainbow.getValue()).build());
        this.brightness = this.register(Settings.floatBuilder("Brightness").withRange(0.0f, 1.0f).withValue(0.6f).withVisibility(o -> this.rainbow.getValue()).build());
        this.speed = this.register(Settings.integerBuilder("Speed").withRange(0, 10).withValue(2).withVisibility(o -> this.rainbow.getValue()).build());
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Transparency").withRange(0, 255).withValue(70).build());
        this.renderMode = this.register(Settings.e("Render Mode", RenderMode.SOLID));
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.renderBlock != null && !(Blockhighlight.mc.world.getBlockState(this.renderBlock).getBlock() instanceof BlockAir) && !(Blockhighlight.mc.world.getBlockState(this.renderBlock).getBlock() instanceof BlockLiquid)) {
            if (this.rainbow.getValue()) {
                this.rgbc = Color.getHSBColor(this.hue, this.satuation.getValue(), this.brightness.getValue());
                this.drawBlock(this.renderBlock, this.rgbc.getRed(), this.rgbc.getGreen(), this.rgbc.getBlue());
                if (this.hue + this.speed.getValue() / 200.0f > 1.0f) {
                    this.hue = 0.0f;
                }
                else {
                    this.hue += this.speed.getValue() / 200.0f;
                }
            }
            else {
                this.drawBlock(this.renderBlock, this.red.getValue(), this.green.getValue(), this.blue.getValue());
            }
        }
    }
    
    private void drawBlock(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.alpha.getValue());
        wurstplusTessellator.prepare(7);
        if (this.renderMode.getValue().equals(RenderMode.UP)) {
            wurstplusTessellator.drawBox(blockPos, color.getRGB(), 2);
        }
        else if (this.renderMode.getValue().equals(RenderMode.SOLID)) {
            wurstplusTessellator.drawBox(blockPos, color.getRGB(), 63);
        }
        else if (this.renderMode.getValue().equals(RenderMode.OUTLINE)) {
            final IBlockState iBlockState2 = Blockhighlight.mc.world.getBlockState(this.renderBlock);
            final Vec3d interp2 = interpolateEntity((Entity)Blockhighlight.mc.player, Blockhighlight.mc.getRenderPartialTicks());
            wurstplusTessellator.drawBoundingBox(iBlockState2.getSelectedBoundingBox((World)Blockhighlight.mc.world, this.renderBlock).offset(-interp2.x, -interp2.y, -interp2.z), this.width.getValue(), r, g, b, this.alpha.getValue());
        }
        else {
            final IBlockState iBlockState3 = Blockhighlight.mc.world.getBlockState(this.renderBlock);
            final Vec3d interp3 = interpolateEntity((Entity)Blockhighlight.mc.player, Blockhighlight.mc.getRenderPartialTicks());
            wurstplusTessellator.drawFullBox(iBlockState3.getSelectedBoundingBox((World)Blockhighlight.mc.world, this.renderBlock).offset(-interp3.x, -interp3.y, -interp3.z), this.renderBlock, this.width.getValue(), r, g, b, this.alpha.getValue());
        }
        wurstplusTessellator.release();
    }
    
    @Override
    public void onUpdate() {
        if (Blockhighlight.mc.player == null || this.isDisabled()) {
            return;
        }
        try {
            this.renderBlock = new BlockPos((double)Blockhighlight.mc.objectMouseOver.getBlockPos().getX(), (double)Blockhighlight.mc.objectMouseOver.getBlockPos().getY(), (double)Blockhighlight.mc.objectMouseOver.getBlockPos().getZ());
        }
        catch (Exception e) {}
    }
    
    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
    
    public void onEnable() {
        this.hue = 0.0f;
    }
    
    public void onDisable() {
        this.renderBlock = null;
    }
    
    private enum RenderMode
    {
        SOLID, 
        OUTLINE, 
        UP, 
        FULL;
    }
}
