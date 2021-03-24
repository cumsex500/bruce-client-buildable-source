// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.misc;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import me.travis.wurstplus.util.wurstplusTessellator;
import me.travis.wurstplus.event.events.RenderEvent;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.travis.wurstplus.util.Friends;
import java.util.Collection;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import me.travis.wurstplus.setting.Settings;
import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "32k Warner", category = Category.MISC)
public class SuperWeaponFinder extends Module
{
    private Setting<Boolean> renderOwn;
    private Setting<Boolean> rainbow;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Float> satuation;
    private Setting<Float> brightness;
    private Setting<Integer> speed;
    private Setting<Integer> alpha;
    private BlockPos renderBlock;
    private Color rgbc;
    private float hue;
    
    public SuperWeaponFinder() {
        this.renderOwn = this.register(Settings.b("Show Own", false));
        this.rainbow = this.register(Settings.b("RainbowMode", false));
        this.red = this.register(Settings.integerBuilder("Red").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.green = this.register(Settings.integerBuilder("Green").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.blue = this.register(Settings.integerBuilder("Blue").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.satuation = this.register(Settings.floatBuilder("Saturation").withRange(0.0f, 1.0f).withValue(0.6f).withVisibility(o -> this.rainbow.getValue()).build());
        this.brightness = this.register(Settings.floatBuilder("Brightness").withRange(0.0f, 1.0f).withValue(0.6f).withVisibility(o -> this.rainbow.getValue()).build());
        this.speed = this.register(Settings.integerBuilder("Speed").withRange(0, 10).withValue(2).withVisibility(o -> this.rainbow.getValue()).build());
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Transparency").withRange(0, 255).withValue(70).build());
    }
    
    @Override
    public void onUpdate() {
        if (SuperWeaponFinder.mc.player.isDead || SuperWeaponFinder.mc.player == null || this.isDisabled()) {
            return;
        }
        EntityPlayer target = null;
        final List<EntityPlayer> entities = new ArrayList<EntityPlayer>();
        entities.addAll((Collection<? extends EntityPlayer>)SuperWeaponFinder.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        for (final EntityPlayer e : entities) {
            if (!e.isDead) {
                if (e.getHealth() <= 0.0f) {
                    continue;
                }
                if (e.getName() == SuperWeaponFinder.mc.player.getName() && !this.renderOwn.getValue()) {
                    continue;
                }
                if (!this.checkSharpness(e.getHeldItemMainhand())) {
                    continue;
                }
                this.renderBlock = e.getPosition();
                target = e;
            }
        }
        if (target == null) {
            this.renderBlock = null;
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.renderBlock != null) {
            if (this.rainbow.getValue()) {
                this.rgbc = Color.getHSBColor(this.hue, this.satuation.getValue(), this.brightness.getValue());
                wurstplusTessellator.drawRange(this.renderBlock, (float)this.rgbc.getRed(), (float)this.rgbc.getGreen(), (float)this.rgbc.getBlue(), this.alpha.getValue());
                if (this.hue + this.speed.getValue() / 200.0f > 1.0f) {
                    this.hue = 0.0f;
                }
                else {
                    this.hue += this.speed.getValue() / 200.0f;
                }
            }
            else {
                wurstplusTessellator.drawRange(this.renderBlock, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
            }
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
    
    @Override
    protected void onEnable() {
        this.hue = 0.0f;
    }
    
    @Override
    protected void onDisable() {
        this.renderBlock = null;
    }
}
