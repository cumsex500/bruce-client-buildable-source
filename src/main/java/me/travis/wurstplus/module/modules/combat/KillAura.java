// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.combat;

import net.minecraft.util.EnumHand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import me.travis.wurstplus.util.Friends;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import me.travis.wurstplus.setting.Settings;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "Travis Aura", category = Category.COMBAT)
public class KillAura extends Module
{
    private Setting<Boolean> attackPlayers;
    private Setting<Double> hitRange;
    private Setting<Integer> delay;
    private Setting<Boolean> switchTo32k;
    private Setting<Boolean> onlyUse32k;
    private int hasWaited;
    
    public KillAura() {
        this.attackPlayers = this.register(Settings.b("Players", true));
        this.hitRange = this.register(Settings.d("Hit Range", 5.5));
        this.delay = this.register((Setting<Integer>)Settings.integerBuilder("Delay").withMinimum(0).withValue(6).withMaximum(10).build());
        this.switchTo32k = this.register(Settings.b("32k Switch", true));
        this.onlyUse32k = this.register(Settings.b("32k Only", false));
        this.hasWaited = 0;
    }
    
    @Override
    public void onUpdate() {
        if (!this.isEnabled() || KillAura.mc.player.isDead || KillAura.mc.world == null) {
            return;
        }
        if (this.hasWaited < this.delay.getValue()) {
            ++this.hasWaited;
            return;
        }
        this.hasWaited = 0;
        for (final Entity entity : KillAura.mc.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                if (entity == KillAura.mc.player) {
                    continue;
                }
                if (KillAura.mc.player.getDistance(entity) > this.hitRange.getValue() || ((EntityLivingBase)entity).getHealth() <= 0.0f || (!(entity instanceof EntityPlayer) && this.attackPlayers.getValue()) || (entity instanceof EntityPlayer && Friends.isFriend(entity.getName()))) {
                    continue;
                }
                if (!this.checkSharpness(KillAura.mc.player.getHeldItemMainhand()) && this.onlyUse32k.getValue()) {
                    continue;
                }
                this.attack(entity);
            }
        }
    }
    
    private boolean checkSharpness(final ItemStack item) {
        if (item.getTagCompound() == null) {
            return false;
        }
        final NBTTagList enchants = (NBTTagList)item.getTagCompound().getTag("ench");
        if (enchants == null) {
            return false;
        }
        int i = 0;
        while (i < enchants.tagCount()) {
            final NBTTagCompound enchant = enchants.getCompoundTagAt(i);
            if (enchant.getInteger("id") != 16) {
                ++i;
            }
            else {
                final int lvl = enchant.getInteger("lvl");
                if (lvl < 42) {
                    break;
                }
                return true;
            }
        }
        return false;
    }
    
    public void attack(final Entity e) {
        boolean holding32k = false;
        if (this.checkSharpness(KillAura.mc.player.getHeldItemMainhand())) {
            holding32k = true;
        }
        if (this.switchTo32k.getValue() && !holding32k) {
            int newSlot = -1;
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = KillAura.mc.player.inventory.getStackInSlot(i);
                if (stack != ItemStack.EMPTY && this.checkSharpness(stack)) {
                    newSlot = i;
                    break;
                }
            }
            if (newSlot != -1) {
                KillAura.mc.player.inventory.currentItem = newSlot;
                holding32k = true;
            }
        }
        if (this.onlyUse32k.getValue() && !holding32k) {
            return;
        }
        KillAura.mc.playerController.attackEntity((EntityPlayer)KillAura.mc.player, e);
        KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
    }
}
