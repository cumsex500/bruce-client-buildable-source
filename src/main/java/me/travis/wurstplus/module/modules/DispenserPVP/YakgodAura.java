// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.DispenserPVP;

import net.minecraft.util.math.Vec3d;
import me.travis.wurstplus.util.LagCompensator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import me.travis.wurstplus.module.modules.misc.AutoTool;
import me.travis.wurstplus.module.ModuleManager;
import me.travis.wurstplus.util.Friends;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import me.travis.wurstplus.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import me.travis.wurstplus.setting.Settings;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "Yakgod Aura", category = Category.DispenserPVP)
public class YakgodAura extends Module
{
    private Setting<Boolean> attackPlayers;
    private Setting<Boolean> attackMobs;
    private Setting<Boolean> attackAnimals;
    private Setting<Double> hitRange;
    private Setting<Boolean> ignoreWalls;
    private Setting<WaitMode> waitMode;
    private Setting<Integer> waitTick;
    private Setting<Boolean> switchTo32k;
    private Setting<Boolean> onlyUse32k;
    private int waitCounter;
    
    public YakgodAura() {
        this.attackPlayers = this.register(Settings.b("Players", true));
        this.attackMobs = this.register(Settings.b("Mobs", false));
        this.attackAnimals = this.register(Settings.b("Animals", false));
        this.hitRange = this.register(Settings.d("Hit Range", 5.5));
        this.ignoreWalls = this.register(Settings.b("Ignore Walls", true));
        this.waitMode = this.register(Settings.e("Mode", WaitMode.DYNAMIC));
        this.waitTick = this.register(Settings.integerBuilder("Tick Delay").withMinimum(0).withValue(3).withVisibility(o -> this.waitMode.getValue().equals(WaitMode.STATIC)).build());
        this.switchTo32k = this.register(Settings.b("32k Switch", true));
        this.onlyUse32k = this.register(Settings.b("32k Only", false));
    }
    
    @Override
    public void onUpdate() {
        if (YakgodAura.mc.player.isDead) {
            return;
        }
        final boolean shield = YakgodAura.mc.player.getHeldItemOffhand().getItem().equals(Items.SHIELD) && YakgodAura.mc.player.getActiveHand() == EnumHand.OFF_HAND;
        if (YakgodAura.mc.player.isHandActive() && !shield) {
            return;
        }
        if (this.waitMode.getValue().equals(WaitMode.DYNAMIC)) {
            if (YakgodAura.mc.player.getCooledAttackStrength(this.getLagComp()) < 1.0f) {
                return;
            }
            if (YakgodAura.mc.player.ticksExisted % 2 != 0) {
                return;
            }
        }
        if (this.waitMode.getValue().equals(WaitMode.STATIC) && this.waitTick.getValue() > 0) {
            if (this.waitCounter < this.waitTick.getValue()) {
                ++this.waitCounter;
                return;
            }
            this.waitCounter = 0;
        }
        for (final Entity target : Minecraft.getMinecraft().world.loadedEntityList) {
            if (!EntityUtil.isLiving(target)) {
                continue;
            }
            if (target == YakgodAura.mc.player) {
                continue;
            }
            if (YakgodAura.mc.player.getDistance(target) > this.hitRange.getValue()) {
                continue;
            }
            if (((EntityLivingBase)target).getHealth() <= 0.0f) {
                continue;
            }
            if (this.waitMode.getValue().equals(WaitMode.DYNAMIC) && ((EntityLivingBase)target).hurtTime != 0) {
                continue;
            }
            if (!this.ignoreWalls.getValue() && !YakgodAura.mc.player.canEntityBeSeen(target) && !this.canEntityFeetBeSeen(target)) {
                continue;
            }
            if (this.attackPlayers.getValue() && target instanceof EntityPlayer && !Friends.isFriend(target.getName())) {
                this.attack(target);
                return;
            }
            Label_0459: {
                if (EntityUtil.isPassive(target)) {
                    if (this.attackAnimals.getValue()) {
                        break Label_0459;
                    }
                    continue;
                }
                else {
                    if (EntityUtil.isMobAggressive(target) && this.attackMobs.getValue()) {
                        break Label_0459;
                    }
                    continue;
                }
                continue;
            }
            if (!this.switchTo32k.getValue() && ModuleManager.isModuleEnabled("AutoTool")) {
                AutoTool.equipBestWeapon();
            }
            this.attack(target);
        }
    }
    
    private boolean checkSharpness(final ItemStack stack) {
        if (stack.getTagCompound() == null) {
            return false;
        }
        final NBTTagList enchants = (NBTTagList)stack.getTagCompound().getTag("ench");
        if (enchants == null) {
            return false;
        }
        int i = 0;
        while (i < enchants.tagCount()) {
            final NBTTagCompound enchant = enchants.getCompoundTagAt(i);
            if (enchant.getInteger("id") == 16) {
                final int lvl = enchant.getInteger("lvl");
                if (lvl >= 42) {
                    return true;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    private void attack(final Entity e) {
        boolean holding32k = false;
        if (this.checkSharpness(YakgodAura.mc.player.getHeldItemMainhand())) {
            holding32k = true;
        }
        if (this.switchTo32k.getValue() && !holding32k) {
            int newSlot = -1;
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = YakgodAura.mc.player.inventory.getStackInSlot(i);
                if (stack != ItemStack.EMPTY) {
                    if (this.checkSharpness(stack)) {
                        newSlot = i;
                        break;
                    }
                }
            }
            if (newSlot != -1) {
                YakgodAura.mc.player.inventory.currentItem = newSlot;
                holding32k = true;
            }
        }
        if (this.onlyUse32k.getValue() && !holding32k) {
            return;
        }
        YakgodAura.mc.playerController.attackEntity((EntityPlayer)YakgodAura.mc.player, e);
        YakgodAura.mc.player.swingArm(EnumHand.MAIN_HAND);
    }
    
    private float getLagComp() {
        if (this.waitMode.getValue().equals(WaitMode.DYNAMIC)) {
            return -(20.0f - LagCompensator.INSTANCE.getTickRate());
        }
        return 0.0f;
    }
    
    private boolean canEntityFeetBeSeen(final Entity entityIn) {
        return YakgodAura.mc.world.rayTraceBlocks(new Vec3d(YakgodAura.mc.player.posX, YakgodAura.mc.player.posY + YakgodAura.mc.player.getEyeHeight(), YakgodAura.mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null;
    }
    
    private enum WaitMode
    {
        DYNAMIC, 
        STATIC;
    }
}
