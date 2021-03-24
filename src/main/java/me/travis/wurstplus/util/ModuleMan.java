// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.util;

import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import me.travis.wurstplus.module.ModuleManager;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class ModuleMan
{
    public Integer totems;
    private String holeType;
    private BlockPos pos;
    
    public ModuleMan() {
        this.holeType = "§4 0";
        this.getPlayerPos();
    }
    
    public Boolean getPlayerPos() {
        try {
            this.pos = new BlockPos(Math.floor(Minecraft.getMinecraft().player.posX), Math.floor(Minecraft.getMinecraft().player.posY), Math.floor(Minecraft.getMinecraft().player.posZ));
            return false;
        }
        catch (Exception e) {
            return true;
        }
    }
    
    public String getHoleType() {
        if (this.getPlayerPos()) {
            return "§4 0";
        }
        this.getPlayerPos();
        if (Minecraft.getMinecraft().world.getBlockState(this.pos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK && Minecraft.getMinecraft().world.getBlockState(this.pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK && Minecraft.getMinecraft().world.getBlockState(this.pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK && Minecraft.getMinecraft().world.getBlockState(this.pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK && Minecraft.getMinecraft().world.getBlockState(this.pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK) {
            return this.holeType = "§a Safe";
        }
        if ((Minecraft.getMinecraft().world.getBlockState(this.pos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK | Minecraft.getMinecraft().world.getBlockState(this.pos.add(0, -1, 0)).getBlock() == Blocks.OBSIDIAN) && (Minecraft.getMinecraft().world.getBlockState(this.pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK | Minecraft.getMinecraft().world.getBlockState(this.pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (Minecraft.getMinecraft().world.getBlockState(this.pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK | Minecraft.getMinecraft().world.getBlockState(this.pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN) && (Minecraft.getMinecraft().world.getBlockState(this.pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK | Minecraft.getMinecraft().world.getBlockState(this.pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (Minecraft.getMinecraft().world.getBlockState(this.pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK | Minecraft.getMinecraft().world.getBlockState(this.pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN)) {
            return this.holeType = "§3 Unsafe";
        }
        return this.holeType = "§4 None";
    }
    
    public String isAura() {
        try {
            if (ModuleManager.getModuleByName("Travis Aura").isEnabled()) {
                return "§a 1";
            }
            return "§4 0";
        }
        catch (Exception e) {
            return "lack of games: " + e;
        }
    }
    
    public String isTrap() {
        try {
            if (ModuleManager.getModuleByName("AutoTrap").isEnabled()) {
                return "§a 1";
            }
            return "§4 0";
        }
        catch (Exception e) {
            return "lack of games: " + e;
        }
    }
    
    public String isSurround() {
        try {
            if (ModuleManager.getModuleByName("Surround").isEnabled()) {
                return "§a 1";
            }
            return "§4 0";
        }
        catch (Exception e) {
            return "lack of games: " + e;
        }
    }
    
    public String isFill() {
        try {
            if (ModuleManager.getModuleByName("HoleFill").isEnabled()) {
                return "§a 1";
            }
            return "§4 0";
        }
        catch (Exception e) {
            return "lack of games: " + e;
        }
    }
    
    public int getTotemsInt() {
        return this.offhand() + Minecraft.getMinecraft().player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::func_190916_E).sum();
    }
    
    public String getTotems() {
        try {
            this.totems = this.offhand() + Minecraft.getMinecraft().player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::func_190916_E).sum();
            if (this.totems > 1) {
                return "§a " + this.totems;
            }
            return "§4 " + this.totems;
        }
        catch (Exception e) {
            return "0";
        }
    }
    
    public Integer offhand() {
        if (Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            return 1;
        }
        return 0;
    }
}
