// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.DispenserPVP;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import me.travis.wurstplus.command.Command;
import me.travis.wurstplus.util.BlockInteractionHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import java.math.RoundingMode;
import me.travis.wurstplus.module.ModuleManager;
import me.travis.wurstplus.setting.Settings;
import net.minecraft.util.math.BlockPos;
import me.travis.wurstplus.setting.Setting;
import java.text.DecimalFormat;
import me.travis.wurstplus.module.Module;

@Info(name = "BetterAuto32k", category = Category.DispenserPVP)
public class BetterAuto32k extends Module
{
    private static final DecimalFormat df;
    private Setting<Boolean> rotate;
    private Setting<Boolean> grabItem;
    private Setting<Boolean> autoEnableHitAura;
    private Setting<Boolean> autoEnableBypass;
    private Setting<Boolean> debugMessages;
    private int stage;
    private BlockPos placeTarget;
    private int obiSlot;
    private int dispenserSlot;
    private int shulkerSlot;
    private int redstoneSlot;
    private int hopperSlot;
    private boolean isSneaking;
    
    public BetterAuto32k() {
        this.rotate = this.register(Settings.b("Rotate", false));
        this.grabItem = this.register(Settings.b("Grab Item", false));
        this.autoEnableHitAura = this.register(Settings.b("Auto enable Hit Aura", false));
        this.autoEnableBypass = this.register(Settings.b("Auto enable Illegals Bypass", false));
        this.debugMessages = this.register(Settings.b("Debug Messages", false));
    }
    
    @Override
    protected void onEnable() {
        if (BetterAuto32k.mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
            this.disable();
            return;
        }
        BetterAuto32k.df.setRoundingMode(RoundingMode.CEILING);
        this.stage = 0;
        this.placeTarget = null;
        this.obiSlot = -1;
        this.dispenserSlot = -1;
        this.shulkerSlot = -1;
        this.redstoneSlot = -1;
        this.hopperSlot = -1;
        this.isSneaking = false;
        for (int i = 0; i < 9 && (this.obiSlot == -1 || this.dispenserSlot == -1 || this.shulkerSlot == -1 || this.redstoneSlot == -1 || this.hopperSlot == -1); ++i) {
            final ItemStack stack = BetterAuto32k.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block == Blocks.HOPPER) {
                        this.hopperSlot = i;
                    }
                    else if (BlockInteractionHelper.shulkerList.contains(block)) {
                        this.shulkerSlot = i;
                    }
                    else if (block == Blocks.OBSIDIAN) {
                        this.obiSlot = i;
                    }
                    else if (block == Blocks.DISPENSER) {
                        this.dispenserSlot = i;
                    }
                    else if (block == Blocks.REDSTONE_BLOCK) {
                        this.redstoneSlot = i;
                    }
                }
            }
        }
        if (this.obiSlot == -1 || this.dispenserSlot == -1 || this.shulkerSlot == -1 || this.redstoneSlot == -1 || this.hopperSlot == -1) {
            if (this.debugMessages.getValue()) {
                Command.sendChatMessage("[Auto32k] Items missing, disabling.");
            }
            this.disable();
            return;
        }
        if (BetterAuto32k.mc.objectMouseOver == null || BetterAuto32k.mc.objectMouseOver.getBlockPos() == null || BetterAuto32k.mc.objectMouseOver.getBlockPos().up() == null) {
            if (this.debugMessages.getValue()) {
                Command.sendChatMessage("[Auto32k] Not a valid place target, disabling.");
            }
            this.disable();
            return;
        }
        this.placeTarget = BetterAuto32k.mc.objectMouseOver.getBlockPos().up();
        if (this.autoEnableBypass.getValue()) {
            ModuleManager.getModuleByName("IllegalItemBypass").enable();
        }
        if (!this.debugMessages.getValue()) {
            return;
        }
        Command.sendChatMessage("[Auto32k] Place Target: " + this.placeTarget.x + " " + this.placeTarget.y + " " + this.placeTarget.z + " Distance: " + BetterAuto32k.df.format(BetterAuto32k.mc.player.getPositionVector().distanceTo(new Vec3d((Vec3i)this.placeTarget))));
    }
    
    @Override
    public void onUpdate() {
        if (BetterAuto32k.mc.player == null) {
            return;
        }
        if (ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (this.stage == 0) {
            BetterAuto32k.mc.player.inventory.currentItem = this.obiSlot;
            this.placeBlock(new BlockPos((Vec3i)this.placeTarget), EnumFacing.DOWN);
            BetterAuto32k.mc.player.inventory.currentItem = this.dispenserSlot;
            this.placeBlock(new BlockPos((Vec3i)this.placeTarget.add(0, 1, 0)), EnumFacing.DOWN);
            BetterAuto32k.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BetterAuto32k.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
            BetterAuto32k.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.placeTarget.add(0, 1, 0), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.stage = 1;
            return;
        }
        if (this.stage == 1) {
            if (!(BetterAuto32k.mc.currentScreen instanceof GuiContainer)) {
                return;
            }
            BetterAuto32k.mc.playerController.windowClick(BetterAuto32k.mc.player.openContainer.windowId, 1, this.shulkerSlot, ClickType.SWAP, (EntityPlayer)BetterAuto32k.mc.player);
            BetterAuto32k.mc.player.closeScreen();
            BetterAuto32k.mc.player.inventory.currentItem = this.redstoneSlot;
            this.placeBlock(new BlockPos((Vec3i)this.placeTarget.add(0, 2, 0)), EnumFacing.DOWN);
            this.stage = 2;
        }
        else if (this.stage == 2) {
            final Block block = BetterAuto32k.mc.world.getBlockState(this.placeTarget.offset(BetterAuto32k.mc.player.getHorizontalFacing().getOpposite()).up()).getBlock();
            if (block instanceof BlockAir) {
                return;
            }
            if (block instanceof BlockLiquid) {
                return;
            }
            BetterAuto32k.mc.player.inventory.currentItem = this.hopperSlot;
            this.placeBlock(new BlockPos((Vec3i)this.placeTarget.offset(BetterAuto32k.mc.player.getHorizontalFacing().getOpposite())), BetterAuto32k.mc.player.getHorizontalFacing());
            BetterAuto32k.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BetterAuto32k.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
            BetterAuto32k.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.placeTarget.offset(BetterAuto32k.mc.player.getHorizontalFacing().getOpposite()), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            BetterAuto32k.mc.player.inventory.currentItem = this.shulkerSlot;
            if (!this.grabItem.getValue()) {
                this.disable();
                return;
            }
            this.stage = 3;
        }
        else {
            if (this.stage != 3) {
                return;
            }
            if (!(BetterAuto32k.mc.currentScreen instanceof GuiContainer)) {
                return;
            }
            if (((GuiContainer)BetterAuto32k.mc.currentScreen).inventorySlots.getSlot(0).getStack().isEmpty) {
                return;
            }
            BetterAuto32k.mc.playerController.windowClick(BetterAuto32k.mc.player.openContainer.windowId, 0, BetterAuto32k.mc.player.inventory.currentItem, ClickType.SWAP, (EntityPlayer)BetterAuto32k.mc.player);
            if (this.autoEnableHitAura.getValue()) {
                ModuleManager.getModuleByName("Aura").enable();
            }
            this.disable();
        }
    }
    
    private void placeBlock(final BlockPos pos, final EnumFacing side) {
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!this.isSneaking) {
            BetterAuto32k.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BetterAuto32k.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        if (this.rotate.getValue()) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        BetterAuto32k.mc.playerController.processRightClickBlock(BetterAuto32k.mc.player, BetterAuto32k.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        BetterAuto32k.mc.player.swingArm(EnumHand.MAIN_HAND);
    }
    
    static {
        df = new DecimalFormat("#.#");
    }
}
