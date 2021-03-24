// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.EnumFacing;
import me.travis.wurstplus.util.BlockInteractionHelper;
import java.util.Iterator;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import me.travis.wurstplus.command.Command;
import me.travis.wurstplus.module.ModuleManager;
import me.travis.wurstplus.util.Wrapper;
import me.travis.wurstplus.setting.Settings;
import me.travis.wurstplus.setting.Setting;
import net.minecraft.util.math.Vec3d;
import me.travis.wurstplus.module.Module;

@Info(name = "Surround", category = Category.COMBAT)
public class Surround extends Module
{
    private final Vec3d[] surroundTargets;
    private Setting<Boolean> triggerable;
    private Setting<Integer> timeoutTicks;
    private Setting<Integer> blocksPerTick;
    private Setting<Boolean> rotate;
    private Setting<Boolean> hybrid;
    private Setting<Boolean> announceUsage;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private int offsetStep;
    private int totalTickRuns;
    private boolean isSneaking;
    private boolean flag;
    private int yHeight;
    
    public Surround() {
        this.surroundTargets = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, -1.0, 0.0) };
        this.triggerable = this.register(Settings.b("Triggerable", true));
        this.timeoutTicks = this.register((Setting<Integer>)Settings.integerBuilder("TimeoutTicks").withMinimum(1).withValue(20).withMaximum(100).build());
        this.blocksPerTick = this.register((Setting<Integer>)Settings.integerBuilder("Blocks per Tick").withMinimum(1).withValue(4).withMaximum(9).build());
        this.rotate = this.register(Settings.b("Rotate", true));
        this.hybrid = this.register(Settings.b("Hybrid", true));
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.offsetStep = 0;
        this.totalTickRuns = 0;
        this.isSneaking = false;
        this.flag = false;
        this.announceUsage = this.register(Settings.b("Announce Usage", true));
    }
    
    @Override
    protected void onEnable() {
        this.flag = false;
        if (Surround.mc.player == null) {
            this.disable();
            return;
        }
        this.playerHotbarSlot = Wrapper.getPlayer().inventory.currentItem;
        this.lastHotbarSlot = -1;
        this.yHeight = (int)Math.round(Surround.mc.player.posY);
        if (ModuleManager.getModuleByName("Unco Aura").isEnabled()) {
            this.flag = true;
            ModuleManager.getModuleByName("Unco Aura").disable();
        }
        if (this.announceUsage.getValue()) {
            Command.sendChatMessage("we surrounding");
        }
    }
    
    @Override
    protected void onDisable() {
        if (Surround.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            Wrapper.getPlayer().inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        if (this.flag) {
            ModuleManager.getModuleByName("Unco Aura").enable();
            this.flag = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        if (this.announceUsage.getValue()) {
            Command.sendChatMessage("we aint surrounding no more");
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.isDisabled() && this.flag) {
            ModuleManager.getModuleByName("Unco Aura").enable();
            this.flag = false;
        }
        if (Surround.mc.player == null) {
            return;
        }
        if (this.hybrid.getValue() && (int)Math.round(Surround.mc.player.posY) != this.yHeight) {
            this.disable();
        }
        if (this.triggerable.getValue() && this.totalTickRuns >= this.timeoutTicks.getValue()) {
            this.totalTickRuns = 0;
            this.disable();
            return;
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= this.surroundTargets.length) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos(this.surroundTargets[this.offsetStep]);
            final BlockPos targetPos = new BlockPos(Surround.mc.player.getPositionVector()).add(offsetPos.x, offsetPos.y, offsetPos.z);
            boolean shouldTryToPlace = true;
            if (!Wrapper.getWorld().getBlockState(targetPos).getMaterial().isReplaceable()) {
                shouldTryToPlace = false;
            }
            for (final Entity entity : Surround.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(targetPos))) {
                if (!(entity instanceof EntityItem)) {
                    if (entity instanceof EntityXPOrb) {
                        continue;
                    }
                    shouldTryToPlace = false;
                    break;
                }
            }
            if (shouldTryToPlace && this.placeBlock(targetPos)) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0 && this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            Wrapper.getPlayer().inventory.currentItem = this.playerHotbarSlot;
            this.lastHotbarSlot = this.playerHotbarSlot;
        }
        ++this.totalTickRuns;
    }
    
    private boolean placeBlock(final BlockPos pos) {
        if (!Surround.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            return false;
        }
        if (!BlockInteractionHelper.checkForNeighbours(pos)) {
            return false;
        }
        final EnumFacing[] values = EnumFacing.values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (!BlockInteractionHelper.canBeClicked(neighbor)) {
                ++i;
            }
            else {
                final int obiSlot = this.findObiInHotbar();
                if (obiSlot == -1) {
                    this.disable();
                    return false;
                }
                if (this.lastHotbarSlot != obiSlot) {
                    Wrapper.getPlayer().inventory.currentItem = obiSlot;
                    this.lastHotbarSlot = obiSlot;
                }
                final Block neighborPos;
                if (BlockInteractionHelper.blackList.contains(neighborPos = Surround.mc.world.getBlockState(neighbor).getBlock())) {
                    Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    this.isSneaking = true;
                }
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (this.rotate.getValue()) {
                    BlockInteractionHelper.faceVectorPacketInstant(hitVec);
                }
                Surround.mc.playerController.processRightClickBlock(Surround.mc.player, Surround.mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                Surround.mc.player.swingArm(EnumHand.MAIN_HAND);
                return true;
            }
        }
        return false;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
            final Block block;
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock && (block = ((ItemBlock)stack.getItem()).getBlock()) instanceof BlockObsidian) {
                slot = i;
                break;
            }
        }
        return slot;
    }
}
