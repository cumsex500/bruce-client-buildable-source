// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.combat;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import java.util.OptionalInt;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiContainer;
import me.travis.wurstplus.setting.Settings;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "AutoTotem", category = Category.COMBAT)
public class AutoTotem extends Module
{
    private Setting<Boolean> soft;
    private Setting<Integer> healthSwitch;
    
    public AutoTotem() {
        this.soft = this.register(Settings.b("Soft", true));
        this.healthSwitch = this.register(Settings.integerBuilder("Health Switch").withRange(1, 20).withValue(4).withVisibility(o -> this.soft.getValue()).build());
    }
    
    @Override
    public void onUpdate() {
        if (AutoTotem.mc.player == null) {
            return;
        }
        if (AutoTotem.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.soft.getValue()) {
            if (AutoTotem.mc.player.getHealth() + AutoTotem.mc.player.getAbsorptionAmount() <= this.healthSwitch.getValue() && this.getOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                return;
            }
            if (!this.getOffhand().isEmpty() && AutoTotem.mc.player.getHealth() + AutoTotem.mc.player.getAbsorptionAmount() >= this.healthSwitch.getValue()) {
                return;
            }
            this.findItem(Items.TOTEM_OF_UNDYING).ifPresent(slot -> {
                this.invPickup(slot);
                this.invPickup(45);
                this.invPickup(slot);
            });
        }
        else {
            if (this.getOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                return;
            }
            this.findItem(Items.TOTEM_OF_UNDYING).ifPresent(slot -> {
                this.invPickup(slot);
                this.invPickup(45);
                this.invPickup(slot);
            });
        }
    }
    
    private void invPickup(final int slot) {
        AutoTotem.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
    }
    
    private OptionalInt findItem(final Item ofType) {
        for (int i = 44; i >= 9; --i) {
            if (AutoTotem.mc.player.inventoryContainer.getSlot(i).getStack().getItem() == ofType) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }
    
    private ItemStack getOffhand() {
        return AutoTotem.mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
    }
}
