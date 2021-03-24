// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.DispenserPVP;

import net.minecraft.util.math.Vec3d;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import me.travis.wurstplus.util.Friends;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import me.travis.wurstplus.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import me.travis.wurstplus.util.LagCompensator;
import me.travis.wurstplus.setting.Settings;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "Bruce Aura", category = Category.DispenserPVP, description = "Hits entities around you")
public class BruceAura extends Module
{
    private Setting<Boolean> attackPlayers;
    private Setting<Boolean> attackMobs;
    private Setting<Boolean> attackAnimals;
    private Setting<Double> hitRange;
    private Setting<Boolean> ignoreWalls;
    private Setting<WaitMode> waitMode;
    private Setting<Double> waitTick;
    private Setting<Boolean> autoWait;
    private Setting<SwitchMode> switchMode;
    private Setting<HitMode> hitMode;
    private int waitCounter;
    
    public BruceAura() {
        this.attackPlayers = this.register(Settings.b("Players", true));
        this.attackMobs = this.register(Settings.b("Mobs", false));
        this.attackAnimals = this.register(Settings.b("Animals", false));
        this.hitRange = this.register(Settings.d("Hit Range", 5.5));
        this.ignoreWalls = this.register(Settings.b("Ignore Walls", true));
        this.waitMode = this.register(Settings.e("Mode", WaitMode.TICK));
        this.waitTick = this.register((Setting<Double>)Settings.doubleBuilder("Tick Delay").withMinimum(0.1).withValue(2.0).withMaximum(20.0).build());
        this.autoWait = this.register(Settings.b("Auto Tick Delay", true));
        this.switchMode = this.register(Settings.e("Autoswitch", SwitchMode.Only32k));
        this.hitMode = this.register(Settings.e("Tool", HitMode.SWORD));
    }
    
    public void onEnable() {
        if (BruceAura.mc.player == null) {
            return;
        }
    }
    
    @Override
    public void onUpdate() {
        double autoWaitTick = 0.0;
        if (BruceAura.mc.player.isDead || BruceAura.mc.player == null) {
            return;
        }
        if (this.autoWait.getValue()) {
            autoWaitTick = 20.0 - Math.round(LagCompensator.INSTANCE.getTickRate() * 10.0f) / 10.0;
        }
        final boolean shield = BruceAura.mc.player.getHeldItemOffhand().getItem().equals(Items.SHIELD) && BruceAura.mc.player.getActiveHand() == EnumHand.OFF_HAND;
        if (BruceAura.mc.player.isHandActive() && !shield) {
            return;
        }
        if (this.waitMode.getValue().equals(WaitMode.CPS)) {
            if (BruceAura.mc.player.getCooledAttackStrength(this.getLagComp()) < 1.0f) {
                return;
            }
            if (BruceAura.mc.player.ticksExisted % 2 != 0) {
                return;
            }
        }
        if (this.autoWait.getValue()) {
            if (this.waitMode.getValue().equals(WaitMode.TICK) && autoWaitTick > 0.0) {
                if (this.waitCounter < autoWaitTick) {
                    ++this.waitCounter;
                    return;
                }
                this.waitCounter = 0;
            }
        }
        else if (this.waitMode.getValue().equals(WaitMode.TICK) && this.waitTick.getValue() > 0.0) {
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
            if (target == BruceAura.mc.player) {
                continue;
            }
            if (BruceAura.mc.player.getDistance(target) > this.hitRange.getValue()) {
                continue;
            }
            if (((EntityLivingBase)target).getHealth() <= 0.0f) {
                continue;
            }
            if (this.waitMode.getValue().equals(WaitMode.CPS) && ((EntityLivingBase)target).hurtTime != 0) {
                continue;
            }
            if (!this.ignoreWalls.getValue() && !BruceAura.mc.player.canEntityBeSeen(target) && !this.canEntityFeetBeSeen(target)) {
                continue;
            }
            if (this.attackPlayers.getValue() && target instanceof EntityPlayer && !Friends.isFriend(target.getName())) {
                this.attack(target);
                return;
            }
            Label_0598: {
                if (EntityUtil.isPassive(target)) {
                    if (this.attackAnimals.getValue()) {
                        break Label_0598;
                    }
                    continue;
                }
                else {
                    if (EntityUtil.isMobAggressive(target) && this.attackMobs.getValue()) {
                        break Label_0598;
                    }
                    continue;
                }
                continue;
            }
            this.attack(target);
        }
    }
    
    private boolean checkSharpness(final ItemStack stack) {
        if (stack.getTagCompound() == null) {
            return false;
        }
        if (stack.getItem().equals(Items.DIAMOND_AXE) && this.hitMode.getValue().equals(HitMode.SWORD)) {
            return false;
        }
        if (stack.getItem().equals(Items.DIAMOND_SWORD) && this.hitMode.getValue().equals(HitMode.AXE)) {
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
                if (this.switchMode.getValue().equals(SwitchMode.Only32k)) {
                    if (lvl >= 42) {
                        return true;
                    }
                    break;
                }
                else if (this.switchMode.getValue().equals(SwitchMode.ALL)) {
                    if (lvl >= 4) {
                        return true;
                    }
                    break;
                }
                else {
                    if (this.switchMode.getValue().equals(SwitchMode.NONE)) {
                        return true;
                    }
                    break;
                }
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    private void attack(final Entity e) {
        boolean holding32k = false;
        if (this.checkSharpness(BruceAura.mc.player.getHeldItemMainhand())) {
            holding32k = true;
        }
        if ((this.switchMode.getValue().equals(SwitchMode.Only32k) || this.switchMode.getValue().equals(SwitchMode.ALL)) && !holding32k) {
            int newSlot = -1;
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = BruceAura.mc.player.inventory.getStackInSlot(i);
                if (stack != ItemStack.EMPTY) {
                    if (this.checkSharpness(stack)) {
                        newSlot = i;
                        break;
                    }
                }
            }
            if (newSlot != -1) {
                BruceAura.mc.player.inventory.currentItem = newSlot;
                holding32k = true;
            }
        }
        if (this.switchMode.getValue().equals(SwitchMode.Only32k) && !holding32k) {
            return;
        }
        BruceAura.mc.playerController.attackEntity((EntityPlayer)BruceAura.mc.player, e);
        BruceAura.mc.player.swingArm(EnumHand.MAIN_HAND);
    }
    
    private float getLagComp() {
        if (this.waitMode.getValue().equals(WaitMode.CPS)) {
            return -(20.0f - LagCompensator.INSTANCE.getTickRate());
        }
        return 0.0f;
    }
    
    private boolean canEntityFeetBeSeen(final Entity entityIn) {
        return BruceAura.mc.world.rayTraceBlocks(new Vec3d(BruceAura.mc.player.posX, BruceAura.mc.player.posY + BruceAura.mc.player.getEyeHeight(), BruceAura.mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null;
    }
    
    private enum SwitchMode
    {
        NONE, 
        ALL, 
        Only32k;
    }
    
    private enum HitMode
    {
        SWORD, 
        AXE;
    }
    
    private enum WaitMode
    {
        CPS, 
        TICK;
    }
}
