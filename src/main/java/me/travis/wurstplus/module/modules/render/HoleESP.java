// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.render;

import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import java.awt.Color;
import me.travis.wurstplus.util.wurstplusTessellator;
import me.travis.wurstplus.event.events.RenderEvent;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.Vec3i;
import net.minecraft.init.Blocks;
import me.travis.wurstplus.util.BlockInteractionHelper;
import me.travis.wurstplus.module.modules.combat.CrystalAura;
import me.travis.wurstplus.setting.Settings;
import java.util.concurrent.ConcurrentHashMap;
import me.travis.wurstplus.setting.Setting;
import net.minecraft.util.math.BlockPos;
import me.travis.wurstplus.module.Module;

@Info(name = "HoleESP", category = Category.RENDER, description = "Show safe holes")
public class HoleESP extends Module
{
    private final BlockPos[] surroundOffset;
    private Setting<HoleType> holeType;
    private Setting<Boolean> hideOwn;
    private Setting<Double> renderDistance;
    private Setting<RenderMode> renderMode;
    private Setting<Float> width;
    private Setting<Integer> renderAlpha;
    private ConcurrentHashMap<BlockPos, Boolean> safeHoles;
    
    public HoleESP() {
        this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
        this.holeType = this.register(Settings.e("HoleType", HoleType.BOTH));
        this.hideOwn = this.register(Settings.b("HideOwn", false));
        this.renderDistance = this.register((Setting<Double>)Settings.doubleBuilder("RenderDistance").withMinimum(1.0).withValue(8.0).withMaximum(32.0).build());
        this.renderMode = this.register(Settings.e("RenderMode", RenderMode.DOWN));
        this.width = this.register(Settings.floatBuilder("Line Width").withMinimum(1.0f).withValue(3.0f).withMaximum(10.0f).withVisibility(o -> this.renderMode.getValue().equals(RenderMode.OUTLINE)).build());
        this.renderAlpha = this.register((Setting<Integer>)Settings.integerBuilder("RenderAlpha").withMinimum(0).withValue(42).withMaximum(255).build());
    }
    
    @Override
    public void onUpdate() {
        if (this.safeHoles == null) {
            this.safeHoles = new ConcurrentHashMap<BlockPos, Boolean>();
        }
        else {
            this.safeHoles.clear();
        }
        final int range = (int)Math.ceil(this.renderDistance.getValue());
        final List<BlockPos> blockPosList = BlockInteractionHelper.getSphere(CrystalAura.getPlayerPos(), (float)range, range, false, true, 0);
        for (final BlockPos pos : blockPosList) {
            if (HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                if (this.hideOwn.getValue() && pos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ))) {
                    continue;
                }
                boolean isSafe = true;
                boolean isBedrock = true;
                for (final BlockPos offset : this.surroundOffset) {
                    final Block block = HoleESP.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                    if (block != Blocks.BEDROCK) {
                        isBedrock = false;
                    }
                    if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                        isSafe = false;
                        break;
                    }
                }
                if (!isSafe) {
                    continue;
                }
                this.safeHoles.put(pos, isBedrock);
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (HoleESP.mc.player == null || this.safeHoles == null) {
            return;
        }
        if (this.safeHoles.isEmpty()) {
            return;
        }
        wurstplusTessellator.prepare(7);
        this.safeHoles.forEach((blockPos, isBedrock) -> {
            if (isBedrock) {
                if (this.holeType.getValue().equals(HoleType.BOTH) || this.holeType.getValue().equals(HoleType.BROCK)) {
                    this.drawBlock(blockPos, 208, 118, 15);
                }
            }
            else if (this.holeType.getValue().equals(HoleType.BOTH) || this.holeType.getValue().equals(HoleType.OBI)) {
                this.drawBlock(blockPos, 103, 156, 255);
            }
            return;
        });
        wurstplusTessellator.release();
    }
    
    private void drawBlock(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.renderAlpha.getValue());
        if (this.renderMode.getValue().equals(RenderMode.BLOCK)) {
            wurstplusTessellator.drawBox(blockPos, color.getRGB(), 63);
        }
        else if (this.renderMode.getValue().equals(RenderMode.OUTLINE)) {
            final IBlockState iBlockState2 = CrystalAura.mc.world.getBlockState(blockPos);
            final Vec3d interp2 = interpolateEntity((Entity)CrystalAura.mc.player, HoleESP.mc.getRenderPartialTicks());
            wurstplusTessellator.drawBoundingBoxBottom(iBlockState2.getSelectedBoundingBox((World)CrystalAura.mc.world, blockPos).offset(-interp2.x, -interp2.y, -interp2.z), this.width.getValue(), r, g, b, this.renderAlpha.getValue());
        }
        else {
            wurstplusTessellator.drawBox(blockPos, color.getRGB(), 1);
        }
    }
    
    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
    
    private enum HoleType
    {
        BROCK, 
        OBI, 
        BOTH;
    }
    
    public enum RenderMode
    {
        DOWN, 
        OUTLINE, 
        BLOCK;
    }
}
