// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.DispenserPVP;

import java.util.Arrays;
import me.travis.wurstplus.util.BlockInteractionHelper;
import java.util.ArrayList;
import me.travis.wurstplus.module.modules.combat.CrystalAura;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import java.util.Iterator;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import me.travis.wurstplus.command.Command;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import java.math.RoundingMode;
import me.travis.wurstplus.setting.Settings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import me.travis.wurstplus.setting.Setting;
import net.minecraft.block.Block;
import java.util.List;
import java.text.DecimalFormat;
import me.travis.wurstplus.module.Module;

@Info(name = "Auto32kVersionOne", category = Category.DispenserPVP)
public class Auto32kVersionOne extends Module
{
    private static final DecimalFormat df;
    private static final List<Block> shulkerList;
    private Setting<Boolean> moveToHotbar;
    private Setting<Double> placeRange;
    private Setting<Boolean> spoofRotation;
    private Setting<Boolean> raytraceCheck;
    private Setting<Boolean> debugMessages;
    private Setting<Integer> wait;
    private boolean dispenserPlace;
    private boolean flag;
    private boolean lastBlock;
    private int hopperSlot;
    private int delay;
    private int swordSlot;
    private int shulkerSlot;
    private int obiSlot;
    private int redSlot;
    private int disSlot;
    private Float yaw;
    public int xPlus;
    public int zPlus;
    public double xDis;
    public double zDis;
    public EnumFacing face;
    private BlockPos placeTarget;
    
    public Auto32kVersionOne() {
        this.moveToHotbar = this.register(Settings.b("Move 32k to Hotbar", true));
        this.placeRange = this.register(Settings.d("Place Range", 5.0));
        this.spoofRotation = this.register(Settings.b("Spoof Rotation", true));
        this.raytraceCheck = this.register(Settings.b("Raytrace Check", true));
        this.debugMessages = this.register(Settings.b("Debug Messages", false));
        this.wait = this.register((Setting<Integer>)Settings.integerBuilder("Hopper Wait").withMinimum(1).withValue(10).withMaximum(20).build());
    }
    
    @Override
    protected void onEnable() {
        if (Auto32kVersionOne.mc.player == null) {
            this.disable();
            return;
        }
        this.delay = 0;
        this.lastBlock = false;
        this.dispenserPlace = false;
        this.flag = true;
        Auto32kVersionOne.df.setRoundingMode(RoundingMode.CEILING);
        this.hopperSlot = -1;
        this.obiSlot = -1;
        this.redSlot = -1;
        this.disSlot = -1;
        this.swordSlot = -1;
        this.shulkerSlot = -1;
        boolean isNegative = false;
        this.yaw = Auto32kVersionOne.mc.player.rotationYaw;
        if (this.yaw < 0.0f) {
            isNegative = true;
        }
        final int dir = Math.round(Math.abs(this.yaw)) % 360;
        if (135 < dir && dir < 225) {
            this.xPlus = 0;
            this.zPlus = 1;
            this.xDis = 0.0;
            this.zDis = -0.1;
            this.face = EnumFacing.SOUTH;
        }
        else if (225 < dir && dir < 315) {
            if (isNegative) {
                this.xPlus = 1;
                this.zPlus = 0;
                this.xDis = 0.1;
                this.zDis = 0.0;
                this.face = EnumFacing.EAST;
            }
            else {
                this.xPlus = -1;
                this.zPlus = 0;
                this.xDis = -0.1;
                this.zDis = 0.0;
                this.face = EnumFacing.WEST;
            }
        }
        else if (45 < dir && dir < 135) {
            if (isNegative) {
                this.xPlus = -1;
                this.zPlus = 0;
                this.xDis = -0.1;
                this.zDis = 0.0;
                this.face = EnumFacing.WEST;
            }
            else {
                this.xPlus = 1;
                this.zPlus = 0;
                this.xDis = 0.1;
                this.zDis = 0.0;
                this.face = EnumFacing.EAST;
            }
        }
        else {
            this.xPlus = 0;
            this.zPlus = -1;
            this.xDis = 0.0;
            this.zDis = 0.1;
            this.face = EnumFacing.NORTH;
        }
        for (int i = 0; i < 9 && (this.hopperSlot == -1 || this.shulkerSlot == -1 || this.obiSlot == -1 || this.redSlot == -1 || this.disSlot == -1); ++i) {
            final ItemStack stack = Auto32kVersionOne.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block == Blocks.HOPPER) {
                    this.hopperSlot = i;
                }
                else if (Auto32kVersionOne.shulkerList.contains(block)) {
                    this.shulkerSlot = i;
                }
                else if (block == Blocks.OBSIDIAN) {
                    this.obiSlot = i;
                }
                else if (block == Blocks.REDSTONE_BLOCK) {
                    this.redSlot = i;
                }
                else if (block == Blocks.DISPENSER) {
                    this.disSlot = i;
                }
            }
        }
        if (this.shulkerSlot == -1 || this.hopperSlot == -1 || this.obiSlot == -1 || this.redSlot == -1 || this.disSlot == -1) {
            if (this.debugMessages.getValue()) {
                Command.sendChatMessage("Ensure all blocks needed are in your hotbar x");
            }
            this.disable();
            return;
        }
        try {
            this.placeTarget = new BlockPos((double)Auto32kVersionOne.mc.objectMouseOver.getBlockPos().getX(), Auto32kVersionOne.mc.objectMouseOver.getBlockPos().getY() + 1.0, (double)Auto32kVersionOne.mc.objectMouseOver.getBlockPos().getZ());
        }
        catch (Exception e) {
            Command.sendChatMessage("right mate, ur fucking retarded if you think that's a block");
            this.disable();
            return;
        }
        if (this.isAreaPlaceable(this.placeTarget)) {
            Command.sendChatMessage("server is lagging");
            Auto32kVersionOne.mc.player.inventory.currentItem = this.obiSlot;
            this.placeBlock(new BlockPos((Vec3i)this.placeTarget), this.spoofRotation.getValue());
            Auto32kVersionOne.mc.player.inventory.currentItem = this.disSlot;
            this.placeBlock(new BlockPos((Vec3i)this.placeTarget.add(0, 1, 0)), this.spoofRotation.getValue());
            Auto32kVersionOne.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos((Vec3i)this.placeTarget.add(0, 1, 0)), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            return;
        }
        Command.sendChatMessage("there's no place my g");
        this.disable();
    }
    
    @Override
    public void onUpdate() {
        if (this.isDisabled() || Auto32kVersionOne.mc.player == null) {
            return;
        }
        if (this.lastBlock && this.delay >= this.wait.getValue()) {
            Auto32kVersionOne.mc.player.inventory.currentItem = this.hopperSlot;
            final BlockPos hopperPos = new BlockPos((Vec3i)this.placeTarget.add(this.xPlus, 0, this.zPlus));
            Auto32kVersionOne.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Auto32kVersionOne.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.placeBlock(hopperPos, this.spoofRotation.getValue());
            Auto32kVersionOne.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Auto32kVersionOne.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            Auto32kVersionOne.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos((Vec3i)this.placeTarget.add(this.xPlus, 0, this.zPlus)), this.face, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            Auto32kVersionOne.mc.player.inventory.currentItem = this.shulkerSlot;
            this.swordSlot = this.shulkerSlot + 32;
            this.lastBlock = false;
            this.delay = 0;
        }
        if (!this.dispenserPlace) {
            this.placeItemsInDispenser();
            return;
        }
        if (this.flag) {
            this.placeRestOfBlocks();
        }
        if (!(Auto32kVersionOne.mc.currentScreen instanceof GuiContainer)) {
            ++this.delay;
            return;
        }
        if (!this.moveToHotbar.getValue()) {
            this.disable();
            return;
        }
        if (this.swordSlot == -1) {
            ++this.delay;
            return;
        }
        boolean swapReady = true;
        if (((GuiContainer)Auto32kVersionOne.mc.currentScreen).inventorySlots.getSlot(0).getStack().isEmpty) {
            ++this.delay;
            swapReady = false;
        }
        if (!((GuiContainer)Auto32kVersionOne.mc.currentScreen).inventorySlots.getSlot(this.swordSlot).getStack().isEmpty) {
            ++this.delay;
            swapReady = false;
        }
        if (swapReady) {
            Auto32kVersionOne.mc.playerController.windowClick(((GuiContainer)Auto32kVersionOne.mc.currentScreen).inventorySlots.windowId, 0, this.swordSlot - 32, ClickType.SWAP, (EntityPlayer)Auto32kVersionOne.mc.player);
            this.disable();
        }
    }
    
    private boolean isAreaPlaceable(final BlockPos blockPos) {
        for (final Entity entity : Auto32kVersionOne.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(blockPos))) {
            if (entity instanceof EntityLivingBase) {
                return false;
            }
        }
        if (!Auto32kVersionOne.mc.world.getBlockState(blockPos).getMaterial().isReplaceable()) {
            return false;
        }
        if (!Auto32kVersionOne.mc.world.getBlockState(blockPos.add(0, 1, 0)).getMaterial().isReplaceable()) {
            return false;
        }
        if (!Auto32kVersionOne.mc.world.getBlockState(blockPos.add(this.xPlus, 0, this.zPlus)).getMaterial().isReplaceable()) {
            return false;
        }
        if (!Auto32kVersionOne.mc.world.getBlockState(blockPos.add(this.xPlus, 1, this.zPlus)).getMaterial().isReplaceable()) {
            return false;
        }
        if (!Auto32kVersionOne.mc.world.getBlockState(blockPos.add(0, 2, 0)).getMaterial().isReplaceable()) {
            return false;
        }
        if (Auto32kVersionOne.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() instanceof BlockAir) {
            return false;
        }
        if (Auto32kVersionOne.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() instanceof BlockLiquid) {
            return false;
        }
        if (Auto32kVersionOne.mc.player.getPositionVector().distanceTo(new Vec3d((Vec3i)blockPos)) > this.placeRange.getValue()) {
            return false;
        }
        if (this.raytraceCheck.getValue()) {
            final RayTraceResult result = Auto32kVersionOne.mc.world.rayTraceBlocks(new Vec3d(Auto32kVersionOne.mc.player.posX, Auto32kVersionOne.mc.player.posY + Auto32kVersionOne.mc.player.getEyeHeight(), Auto32kVersionOne.mc.player.posZ), new Vec3d((Vec3i)blockPos), false, true, false);
            return result == null || result.getBlockPos().equals((Object)blockPos);
        }
        return true;
    }
    
    private void placeBlock(final BlockPos pos, final boolean spoofRotation) {
        if (!Auto32kVersionOne.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            Command.sendChatMessage("BLOCK WAS UNABLE TO BE PLACED :(");
            return;
        }
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (Auto32kVersionOne.mc.world.getBlockState(neighbor).getBlock().canCollideCheck(Auto32kVersionOne.mc.world.getBlockState(neighbor), false)) {
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).add(0.5 + this.xDis, 0.5, 0.5 + this.zDis).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (spoofRotation) {
                    final Vec3d eyesPos = new Vec3d(Auto32kVersionOne.mc.player.posX, Auto32kVersionOne.mc.player.posY + Auto32kVersionOne.mc.player.getEyeHeight(), Auto32kVersionOne.mc.player.posZ);
                    final double diffX = hitVec.x - eyesPos.x;
                    final double diffY = hitVec.y - eyesPos.y;
                    final double diffZ = hitVec.z - eyesPos.z;
                    final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
                    final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
                    final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
                    Auto32kVersionOne.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(Auto32kVersionOne.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - Auto32kVersionOne.mc.player.rotationYaw), Auto32kVersionOne.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - Auto32kVersionOne.mc.player.rotationPitch), Auto32kVersionOne.mc.player.onGround));
                }
                Auto32kVersionOne.mc.playerController.processRightClickBlock(Auto32kVersionOne.mc.player, Auto32kVersionOne.mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                Auto32kVersionOne.mc.player.swingArm(EnumHand.MAIN_HAND);
                Auto32kVersionOne.mc.rightClickDelayTimer = 4;
                return;
            }
        }
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(CrystalAura.mc.player.posX), Math.floor(CrystalAura.mc.player.posY), Math.floor(CrystalAura.mc.player.posZ));
    }
    
    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    
    public void placeRestOfBlocks() {
        Auto32kVersionOne.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Auto32kVersionOne.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        Auto32kVersionOne.mc.player.inventory.currentItem = this.redSlot;
        this.placeBlock(new BlockPos((Vec3i)this.placeTarget.add(0, 2, 0)), this.spoofRotation.getValue());
        Auto32kVersionOne.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Auto32kVersionOne.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        this.flag = false;
        this.lastBlock = true;
    }
    
    public void placeItemsInDispenser() {
        try {
            Auto32kVersionOne.mc.playerController.windowClick(((GuiContainer)Auto32kVersionOne.mc.currentScreen).inventorySlots.windowId, 0, this.shulkerSlot, ClickType.SWAP, (EntityPlayer)Auto32kVersionOne.mc.player);
            this.dispenserPlace = true;
        }
        catch (Exception ex) {}
    }
    
    public void faceBlock(final BlockPos pos, final EnumFacing face) {
        final Vec3d hitVec = new Vec3d((Vec3i)pos.offset(face)).add(0.5, 0.5, 0.5).add(new Vec3d(face.getDirectionVec()).scale(0.5));
        BlockInteractionHelper.faceVectorPacketInstant(hitVec);
    }
    
    static {
        df = new DecimalFormat("#.#");
        shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
    }
}
