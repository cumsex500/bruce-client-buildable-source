// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.DispenserPVP;

import java.util.HashMap;
import net.minecraft.item.ItemStack;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import me.travis.wurstplus.setting.Settings;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Module.Info(name = "Better Auto Totem", category = Module.Category.DispenserPVP)
public class BetterAutoTotem extends Module
{
    private int numOfTotems;
    private int preferredTotemSlot;
    private Setting<Boolean> soft;
    private Setting<Boolean> pauseInContainers;
    private Setting<Boolean> pauseInInventory;
    
    public BetterAutoTotem() {
        this.soft = this.register(Settings.b("Soft", false));
        this.pauseInContainers = this.register(Settings.b("PauseInContainers", true));
        this.pauseInInventory = this.register(Settings.b("PauseInInventory", true));
    }
    
    @Override
    public void onUpdate() {
        if (BetterAutoTotem.mc.player == null) {
            return;
        }
        if (!this.findTotems()) {
            return;
        }
        if (this.pauseInContainers.getValue() && BetterAutoTotem.mc.currentScreen instanceof GuiContainer && !(BetterAutoTotem.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.pauseInInventory.getValue() && BetterAutoTotem.mc.currentScreen instanceof GuiInventory && BetterAutoTotem.mc.currentScreen instanceof GuiInventory) {
            return;
        }
        if (this.soft.getValue()) {
            if (!BetterAutoTotem.mc.player.getHeldItemOffhand().getItem().equals(Items.AIR)) {
                return;
            }
            BetterAutoTotem.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)BetterAutoTotem.mc.player);
            BetterAutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)BetterAutoTotem.mc.player);
            BetterAutoTotem.mc.playerController.updateController();
        }
        else {
            if (BetterAutoTotem.mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {
                return;
            }
            boolean offhandEmptyPreSwitch = false;
            if (BetterAutoTotem.mc.player.getHeldItemOffhand().getItem().equals(Items.AIR)) {
                offhandEmptyPreSwitch = true;
            }
            BetterAutoTotem.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)BetterAutoTotem.mc.player);
            BetterAutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)BetterAutoTotem.mc.player);
            if (!offhandEmptyPreSwitch) {
                BetterAutoTotem.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)BetterAutoTotem.mc.player);
            }
            BetterAutoTotem.mc.playerController.updateController();
        }
    }
    
    private boolean findTotems() {
        this.numOfTotems = 0;
        final AtomicInteger preferredTotemSlotStackSize = new AtomicInteger();
        preferredTotemSlotStackSize.set(Integer.MIN_VALUE);
        AtomicInteger numOfTotemsInStack = new AtomicInteger();
        final AtomicInteger atomicInteger = new AtomicInteger();
        getInventoryAndHotbarSlots().forEach((slotKey, slotValue) -> {
            numOfTotemsInStack.set(0);
            if (slotValue.getItem().equals(Items.TOTEM_OF_UNDYING)) {
                numOfTotemsInStack.set(slotValue.getCount());
                if (atomicInteger.get() < numOfTotemsInStack.get()) {
                    atomicInteger.set(numOfTotemsInStack.get());
                    this.preferredTotemSlot = slotKey;
                }
            }
            this.numOfTotems.addAndGet(numOfTotemsInStack);
            return;
        });
        if (BetterAutoTotem.mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {
            this.numOfTotems += BetterAutoTotem.mc.player.getHeldItemOffhand().getCount();
        }
        return this.numOfTotems != 0;
    }
    
    private static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        return getInventorySlots(9, 44);
    }
    
    private static Map<Integer, ItemStack> getInventorySlots(int current, final int last) {
        final HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (current <= last) {
            fullInventorySlots.put(current, (ItemStack)BetterAutoTotem.mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return fullInventorySlots;
    }
    
    public void disableSoft() {
        this.soft.setValue(false);
    }
}
