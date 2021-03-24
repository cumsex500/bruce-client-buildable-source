// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.combat;

import java.util.concurrent.TimeUnit;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import me.travis.wurstplus.util.Friends;
import net.minecraft.entity.Entity;
import me.travis.wurstplus.setting.Settings;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import me.travis.wurstplus.setting.Setting;
import net.minecraft.util.math.BlockPos;
import me.travis.wurstplus.module.Module;

@Info(name = "AutoWeb", category = Category.COMBAT)
public class AutoWeb extends Module
{
    BlockPos head;
    BlockPos feet;
    private Setting<Integer> delay;
    int d;
    public static EntityPlayer target;
    public static List<EntityPlayer> targets;
    public static float yaw;
    public static float pitch;
    
    public AutoWeb() {
        this.delay = this.register((Setting<Integer>)Settings.integerBuilder("Delay").withRange(0, 10).withValue(3).build());
    }
    
    public boolean isInBlockRange(final Entity target) {
        return target.getDistance((Entity)AutoWeb.mc.player) <= 4.0f;
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return AutoWeb.mc.world.getBlockState(pos).getBlock().canCollideCheck(AutoWeb.mc.world.getBlockState(pos), false);
    }
    
    public boolean isValid(final EntityPlayer entity) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer animal = entity;
            if (this.isInBlockRange((Entity)animal) && animal.getHealth() > 0.0f && !animal.isDead && !animal.getName().startsWith("Body #") && !Friends.isFriend(animal.getName())) {
                return true;
            }
        }
        return false;
    }
    
    public void loadTargets() {
        for (final EntityPlayer player : AutoWeb.mc.world.playerEntities) {
            if (!(player instanceof EntityPlayerSP)) {
                final EntityPlayer p = player;
                if (this.isValid(p)) {
                    AutoWeb.targets.add(p);
                }
                else {
                    if (!AutoWeb.targets.contains(p)) {
                        continue;
                    }
                    AutoWeb.targets.remove(p);
                }
            }
        }
    }
    
    private boolean isStackObby(final ItemStack stack) {
        return stack != null && stack.getItem() == Item.getItemById(30);
    }
    
    private boolean doesHotbarHaveWeb() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack stack = AutoWeb.mc.player.inventoryContainer.getSlot(i).getStack();
            if (stack != null && this.isStackObby(stack)) {
                return true;
            }
        }
        return false;
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return AutoWeb.mc.world.getBlockState(pos);
    }
    
    public static boolean placeBlockLegit(final BlockPos pos) {
        final Vec3d eyesPos = new Vec3d(AutoWeb.mc.player.posX, AutoWeb.mc.player.posY + AutoWeb.mc.player.getEyeHeight(), AutoWeb.mc.player.posZ);
        final Vec3d posVec = new Vec3d((Vec3i)pos).add(0.5, 0.5, 0.5);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            if (canBeClicked(neighbor)) {
                final Vec3d hitVec = posVec.add(new Vec3d(side.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 36.0) {
                    AutoWeb.mc.playerController.processRightClickBlock(AutoWeb.mc.player, AutoWeb.mc.world, neighbor, side.getOpposite(), hitVec, EnumHand.MAIN_HAND);
                    AutoWeb.mc.player.swingArm(EnumHand.MAIN_HAND);
                    try {
                        TimeUnit.MILLISECONDS.sleep(10L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void onUpdate() {
        if (AutoWeb.mc.player.isHandActive()) {
            return;
        }
        if (!this.isValid(AutoWeb.target) || AutoWeb.target == null) {
            this.updateTarget();
        }
        for (final EntityPlayer player : AutoWeb.mc.world.playerEntities) {
            if (!(player instanceof EntityPlayerSP)) {
                final EntityPlayer e = player;
                if (this.isValid(e) && e.getDistance((Entity)AutoWeb.mc.player) < AutoWeb.target.getDistance((Entity)AutoWeb.mc.player)) {
                    AutoWeb.target = e;
                    return;
                }
                continue;
            }
        }
        if (this.isValid(AutoWeb.target) && AutoWeb.mc.player.getDistance((Entity)AutoWeb.target) < 4.0f) {
            this.trap(AutoWeb.target);
        }
        else {
            this.d = 0;
        }
    }
    
    public void onEnable() {
        if (AutoWeb.mc.player == null) {
            this.disable();
        }
    }
    
    private void trap(final EntityPlayer player) {
        if (player.moveForward == 0.0 && player.moveStrafing == 0.0 && player.moveForward == 0.0) {
            ++this.d;
        }
        if (player.moveForward != 0.0 || player.moveStrafing != 0.0 || player.moveForward != 0.0) {
            this.d = 0;
        }
        if (!this.doesHotbarHaveWeb()) {
            this.d = 0;
        }
        if (this.d == this.delay.getValue() && this.doesHotbarHaveWeb()) {
            this.head = new BlockPos(player.posX, player.posY + 1.0, player.posZ);
            this.feet = new BlockPos(player.posX, player.posY, player.posZ);
            for (int i = 36; i < 45; ++i) {
                final ItemStack stack = AutoWeb.mc.player.inventoryContainer.getSlot(i).getStack();
                if (stack != null && this.isStackObby(stack)) {
                    final int oldSlot = AutoWeb.mc.player.inventory.currentItem;
                    if (AutoWeb.mc.world.getBlockState(this.head).getMaterial().isReplaceable() || AutoWeb.mc.world.getBlockState(this.feet).getMaterial().isReplaceable()) {
                        AutoWeb.mc.player.inventory.currentItem = i - 36;
                        if (player.moveForward == 0.0 || player.moveStrafing == 0.0) {
                            if (AutoWeb.mc.world.getBlockState(this.head).getMaterial().isReplaceable()) {
                                placeBlockLegit(this.head);
                            }
                        }
                        else {
                            if (AutoWeb.mc.world.getBlockState(this.head).getMaterial().isReplaceable()) {
                                placeBlockLegit(this.head);
                            }
                            if (AutoWeb.mc.world.getBlockState(this.feet).getMaterial().isReplaceable()) {
                                placeBlockLegit(this.feet);
                            }
                        }
                        AutoWeb.mc.player.inventory.currentItem = oldSlot;
                        this.d = 0;
                        break;
                    }
                    this.d = 0;
                }
                this.d = 0;
            }
        }
    }
    
    public void onDisable() {
        this.d = 0;
        AutoWeb.yaw = AutoWeb.mc.player.rotationYaw;
        AutoWeb.pitch = AutoWeb.mc.player.rotationPitch;
        AutoWeb.target = null;
    }
    
    public void updateTarget() {
        for (final EntityPlayer player : AutoWeb.mc.world.playerEntities) {
            if (!(player instanceof EntityPlayerSP)) {
                final EntityPlayer entity = player;
                if (entity instanceof EntityPlayerSP) {
                    continue;
                }
                if (!this.isValid(entity)) {
                    continue;
                }
                AutoWeb.target = entity;
            }
        }
    }
    
    public EnumFacing getEnumFacing(final float posX, final float posY, final float posZ) {
        return EnumFacing.getFacingFromVector(posX, posY, posZ);
    }
    
    public BlockPos getBlockPos(final double x, final double y, final double z) {
        return new BlockPos(x, y, z);
    }
}
