// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.combat;

import java.util.List;
import me.travis.wurstplus.util.EntityUtil;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.travis.wurstplus.util.Friends;
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
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import me.travis.wurstplus.command.Command;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import me.travis.wurstplus.util.Wrapper;
import me.travis.wurstplus.setting.Settings;
import net.minecraft.entity.player.EntityPlayer;
import me.travis.wurstplus.setting.Setting;
import net.minecraft.util.math.Vec3d;
import me.travis.wurstplus.module.Module;

@Info(name = "AutoTrap", category = Category.COMBAT)
public class AutoTrap extends Module
{
    private final Vec3d[] offsetsDefault;
    private final Vec3d[] offsetsExtra;
    private Setting<Double> range;
    private Setting<Integer> blockPerTick;
    private Setting<Boolean> rotate;
    private Setting<Boolean> extrablock;
    private Setting<Boolean> chad;
    private Setting<Boolean> announceUsage;
    private EntityPlayer closestTarget;
    private String lastTickTargetName;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int offsetStep;
    private boolean firstRun;
    private double yHeight;
    private double xPos;
    private double zPos;
    
    public AutoTrap() {
        this.offsetsDefault = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0) };
        this.offsetsExtra = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0), new Vec3d(0.0, 4.0, 0.0) };
        this.range = this.register(Settings.d("Range", 5.5));
        this.blockPerTick = this.register(Settings.i("Blocks per Tick", 4));
        this.rotate = this.register(Settings.b("Rotate", true));
        this.extrablock = this.register(Settings.b("Extra Block", true));
        this.chad = this.register(Settings.b("Chad Mode", false));
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.isSneaking = false;
        this.offsetStep = 0;
        this.announceUsage = this.register(Settings.b("Announce Usage", true));
    }
    
    @Override
    protected void onEnable() {
        if (AutoTrap.mc.player == null) {
            this.disable();
            return;
        }
        this.yHeight = (int)Math.ceil(AutoTrap.mc.player.posY);
        this.xPos = (int)Math.ceil(AutoTrap.mc.player.posX);
        this.zPos = (int)Math.ceil(AutoTrap.mc.player.posZ);
        this.firstRun = true;
        this.playerHotbarSlot = Wrapper.getPlayer().inventory.currentItem;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    protected void onDisable() {
        if (AutoTrap.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            Wrapper.getPlayer().inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        if (this.announceUsage.getValue()) {
            Command.sendChatMessage("we aint §l§4trapping§r no more");
        }
    }
    
    @Override
    public void onUpdate() {
        if (AutoTrap.mc.player == null) {
            return;
        }
        if (this.chad.getValue() && (Math.ceil(AutoTrap.mc.player.posY) != this.yHeight || Math.ceil(AutoTrap.mc.player.posX) != this.xPos || Math.ceil(AutoTrap.mc.player.posY) != this.zPos)) {
            Command.sendChatMessage("c: " + Math.ceil(AutoTrap.mc.player.posX) + " " + Math.ceil(AutoTrap.mc.player.posY) + " " + Math.ceil(AutoTrap.mc.player.posZ));
            Command.sendChatMessage("d: " + this.xPos + " " + this.yHeight + " " + this.zPos);
            this.disable();
        }
        this.findClosestTarget();
        if (this.closestTarget == null) {
            if (this.firstRun) {
                this.firstRun = false;
                if (this.announceUsage.getValue()) {
                    Command.sendChatMessage("we §l§2trapping§r");
                }
            }
            return;
        }
        if (this.firstRun) {
            this.firstRun = false;
            this.lastTickTargetName = this.closestTarget.getName();
        }
        else if (!this.lastTickTargetName.equals(this.closestTarget.getName())) {
            this.lastTickTargetName = this.closestTarget.getName();
            this.offsetStep = 0;
        }
        final ArrayList placeTargets = new ArrayList();
        if (this.extrablock.getValue()) {
            Collections.addAll(placeTargets, this.offsetsExtra);
        }
        else {
            Collections.addAll(placeTargets, this.offsetsDefault);
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blockPerTick.getValue()) {
            if (this.offsetStep >= placeTargets.size()) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos((Vec3d)placeTargets.get(this.offsetStep));
            final BlockPos targetPos = new BlockPos(this.closestTarget.getPositionVector()).down().add(offsetPos.x, offsetPos.y, offsetPos.z);
            boolean shouldTryToPlace = true;
            if (!Wrapper.getWorld().getBlockState(targetPos).getMaterial().isReplaceable()) {
                shouldTryToPlace = false;
            }
            for (final Entity entity : AutoTrap.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(targetPos))) {
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
        if (blocksPlaced > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                Wrapper.getPlayer().inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
    }
    
    private boolean placeBlock(final BlockPos pos) {
        if (!AutoTrap.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            return false;
        }
        if (!BlockInteractionHelper.checkForNeighbours(pos)) {
            return false;
        }
        final Vec3d eyesPos = new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight(), Wrapper.getPlayer().posZ);
        final EnumFacing[] values = EnumFacing.values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            final Vec3d hitVec;
            if (AutoTrap.mc.world.getBlockState(neighbor).getBlock().canCollideCheck(AutoTrap.mc.world.getBlockState(neighbor), false) && eyesPos.distanceTo(hitVec = new Vec3d((Vec3i)neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5))) <= this.range.getValue()) {
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
                if (BlockInteractionHelper.blackList.contains(neighborPos = AutoTrap.mc.world.getBlockState(neighbor).getBlock())) {
                    AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    this.isSneaking = true;
                }
                if (this.rotate.getValue()) {
                    BlockInteractionHelper.faceVectorPacketInstant(hitVec);
                }
                AutoTrap.mc.playerController.processRightClickBlock(AutoTrap.mc.player, AutoTrap.mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                AutoTrap.mc.player.swingArm(EnumHand.MAIN_HAND);
                return true;
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 1; i < 10; ++i) {
            final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
            final Block block;
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock && (block = ((ItemBlock)stack.getItem()).getBlock()) instanceof BlockObsidian) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    private void findClosestTarget() {
        final List<EntityPlayer> entities = new ArrayList<EntityPlayer>();
        entities.addAll((Collection<? extends EntityPlayer>)AutoTrap.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        this.closestTarget = null;
        for (final EntityPlayer target : entities) {
            if (target.getName() != AutoTrap.mc.player.getName() && !Friends.isFriend(target.getName()) && EntityUtil.isLiving((Entity)target)) {
                if (target.getHealth() <= 0.0f) {
                    continue;
                }
                if (this.closestTarget == null) {
                    this.closestTarget = target;
                }
                else {
                    if (Wrapper.getPlayer().getDistance((Entity)target) >= Wrapper.getPlayer().getDistance((Entity)this.closestTarget)) {
                        continue;
                    }
                    this.closestTarget = target;
                }
            }
        }
    }
}
