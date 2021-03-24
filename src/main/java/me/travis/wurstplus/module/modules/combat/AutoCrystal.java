// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.combat;

import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.entity.EntityLivingBase;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import me.travis.wurstplus.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import me.travis.wurstplus.util.wurstplusTessellator;
import me.travis.wurstplus.event.events.RenderEvent;
import java.util.function.ToIntFunction;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.ClickType;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.init.MobEffects;
import java.util.Iterator;
import me.travis.wurstplus.module.modules.chat.AutoEZ;
import me.travis.wurstplus.module.ModuleManager;
import me.travis.wurstplus.command.Command;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.travis.wurstplus.util.Friends;
import java.util.List;
import net.minecraft.network.Packet;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import me.travis.wurstplus.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.travis.wurstplus.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import java.awt.Color;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.BlockPos;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "AutoCrystal", category = Category.AURAS)
public class AutoCrystal extends Module
{
    private Setting<Boolean> debug;
    private Setting<Boolean> place;
    private Setting<Boolean> explode;
    private Setting<Boolean> onlyOwn;
    private Setting<Boolean> antiWeakness;
    private Setting<Boolean> offhand;
    private Setting<Integer> offhandHealth;
    private Setting<Integer> hitTickDelay;
    private Setting<Integer> placeTickDelay;
    private Setting<Double> hitRange;
    private Setting<Double> placeRange;
    private Setting<Double> minDamage;
    private Setting<Double> maxSelfDamage;
    private Setting<Boolean> rotate;
    private Setting<Boolean> tabbottMode;
    private Setting<PlaceMode> placeMode;
    private Setting<RenderMode> renderMode;
    private Setting<Integer> alpha;
    private Setting<Boolean> rainbow;
    private Setting<Float> satuation;
    private Setting<Float> brightness;
    private Setting<Integer> speed;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private BlockPos renderBlock;
    private boolean switchCooldown;
    private boolean isAttacking;
    private static boolean togglePitch;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    private int oldSlot;
    private int newSlot;
    private int hitDelayCounter;
    private int placeDelayCounter;
    EntityEnderCrystal crystal;
    private float hue;
    private Color rgbc;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public AutoCrystal() {
        this.debug = this.register(Settings.b("Debug", false));
        this.place = this.register(Settings.b("Place", true));
        this.explode = this.register(Settings.b("Explode", true));
        this.onlyOwn = this.register(Settings.b("Only Explode Own", true));
        this.antiWeakness = this.register(Settings.b("Anti Weakness", true));
        this.offhand = this.register(Settings.b("Smart Offhand", false));
        this.offhandHealth = this.register(Settings.integerBuilder("Offhand Min Health").withMinimum(0).withValue(3).withMaximum(20).withVisibility(o -> this.offhand.getValue()).build());
        this.hitTickDelay = this.register((Setting<Integer>)Settings.integerBuilder("Hit Delay").withMinimum(0).withValue(3).withMaximum(20).build());
        this.placeTickDelay = this.register((Setting<Integer>)Settings.integerBuilder("Place Delay").withMinimum(0).withValue(3).withMaximum(20).build());
        this.hitRange = this.register((Setting<Double>)Settings.doubleBuilder("Hit Range").withMinimum(0.0).withValue(5.5).build());
        this.placeRange = this.register((Setting<Double>)Settings.doubleBuilder("Place Range").withMinimum(0.0).withValue(3.5).build());
        this.minDamage = this.register((Setting<Double>)Settings.doubleBuilder("Min Damage").withMinimum(0.0).withValue(2.0).withMaximum(20.0).build());
        this.maxSelfDamage = this.register((Setting<Double>)Settings.doubleBuilder("Max Self Damage").withMinimum(0.0).withValue(8.0).withMaximum(20.0).build());
        this.rotate = this.register(Settings.b("Spoof Rotations", false));
        this.tabbottMode = this.register(Settings.b("Tabbott Mode", false));
        this.placeMode = this.register(Settings.e("Place Mode", PlaceMode.PLACEFIRST));
        this.renderMode = this.register(Settings.e("Render Mode", RenderMode.UP));
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Transparency").withRange(0, 255).withValue(70).build());
        this.rainbow = this.register(Settings.b("RainbowMode", false));
        this.satuation = this.register(Settings.floatBuilder("Saturation").withRange(0.0f, 1.0f).withValue(0.6f).withVisibility(o -> this.rainbow.getValue()).build());
        this.brightness = this.register(Settings.floatBuilder("Brightness").withRange(0.0f, 1.0f).withValue(0.6f).withVisibility(o -> this.rainbow.getValue()).build());
        this.speed = this.register(Settings.integerBuilder("Speed").withRange(0, 10).withValue(2).withVisibility(o -> this.rainbow.getValue()).build());
        this.red = this.register(Settings.integerBuilder("Red").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.green = this.register(Settings.integerBuilder("Green").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.blue = this.register(Settings.integerBuilder("Blue").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.switchCooldown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        Packet packet;
        this.packetListener = new Listener<PacketEvent.Send>(event -> {
            if (!(!this.rotate.getValue())) {
                packet = event.getPacket();
                if (packet instanceof CPacketPlayer && AutoCrystal.isSpoofingAngles) {
                    ((CPacketPlayer)packet).yaw = (float)AutoCrystal.yaw;
                    ((CPacketPlayer)packet).pitch = (float)AutoCrystal.pitch;
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (AutoCrystal.mc.player.getHealth() >= this.offhandHealth.getValue() && this.offhand.getValue()) {
            this.placeCrystalOffhand();
        }
        if (this.placeMode.getValue().equals(PlaceMode.PLACEFIRST)) {
            if (this.placeDelayCounter >= this.placeTickDelay.getValue()) {
                this.placeCrystal();
            }
            if (this.hitDelayCounter >= this.hitTickDelay.getValue()) {
                this.breakCrystal();
            }
        }
        else {
            if (this.hitDelayCounter >= this.hitTickDelay.getValue()) {
                this.breakCrystal();
            }
            if (this.placeDelayCounter >= this.placeTickDelay.getValue()) {
                this.placeCrystal();
            }
        }
        ++this.placeDelayCounter;
        ++this.hitDelayCounter;
        resetRotation();
    }
    
    public EntityEnderCrystal getBestCrystal(final double range) {
        final int totems = this.getTotems();
        double bestDam = 0.0;
        double minDam = this.minDamage.getValue();
        EntityEnderCrystal bestCrystal = null;
        Entity target = null;
        final List<Entity> players = (List<Entity>)AutoCrystal.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList());
        final List<Entity> crystals = (List<Entity>)AutoCrystal.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).collect(Collectors.toList());
        for (final Entity crystal : crystals) {
            if (AutoCrystal.mc.player.getDistance(crystal) <= range) {
                if (crystal == null) {
                    continue;
                }
                for (final Entity player : players) {
                    if (player != AutoCrystal.mc.player) {
                        if (!(player instanceof EntityPlayer)) {
                            continue;
                        }
                        final EntityPlayer testTarget = (EntityPlayer)player;
                        if (testTarget.isDead || testTarget.getHealth() <= 0.0f || testTarget.getDistanceSq(AutoCrystal.mc.player.getPosition()) >= 169.0) {
                            if (!this.debug.getValue()) {
                                continue;
                            }
                            Command.sendChatMessage("passing - target shit");
                        }
                        else {
                            if (testTarget.getDistanceSq(crystal) >= 169.0) {
                                continue;
                            }
                            if (testTarget.getHealth() < 8.0f && this.tabbottMode.getValue()) {
                                minDam = 0.1;
                            }
                            final double targetDamage = calculateDamage(crystal.posX, crystal.posY, crystal.posZ, (Entity)testTarget);
                            final double selfDamage = calculateDamage(crystal.posX, crystal.posY, crystal.posZ, (Entity)AutoCrystal.mc.player);
                            final float healthTarget = testTarget.getHealth() + testTarget.getAbsorptionAmount();
                            final float healthSelf = AutoCrystal.mc.player.getHealth() + AutoCrystal.mc.player.getAbsorptionAmount();
                            if ((targetDamage < minDam || (selfDamage > targetDamage && targetDamage < healthTarget) || selfDamage > this.maxSelfDamage.getValue() || (healthSelf < 0.5 && totems == 0)) && this.debug.getValue()) {
                                Command.sendChatMessage("passing - too much self damage / too little damage");
                            }
                            if (targetDamage <= bestDam) {
                                continue;
                            }
                            if (this.debug.getValue()) {
                                Command.sendChatMessage("lookin good");
                            }
                            target = player;
                            bestDam = targetDamage;
                            bestCrystal = (EntityEnderCrystal)crystal;
                        }
                    }
                }
            }
        }
        if (ModuleManager.getModuleByName("AutoEZ").isEnabled() && target != null) {
            final AutoEZ autoGG = (AutoEZ)ModuleManager.getModuleByName("AutoEZ");
            autoGG.addTargetedPlayer(target.getName());
        }
        players.clear();
        crystals.clear();
        return bestCrystal;
    }
    
    public BlockPos getBestBlock() {
        final List<BlockPos> blocks = this.findCrystalBlocks(this.placeRange.getValue());
        final List<Entity> players = (List<Entity>)AutoCrystal.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList());
        BlockPos targetBlock = null;
        EntityPlayer target = null;
        final int totems = this.getTotems();
        double bestDam = 0.0;
        double minDam = this.minDamage.getValue();
        for (final Entity player : players) {
            if (player != AutoCrystal.mc.player) {
                if (!(player instanceof EntityPlayer)) {
                    continue;
                }
                final EntityPlayer testTarget = (EntityPlayer)player;
                if (testTarget.isDead || testTarget.getHealth() <= 0.0f) {
                    continue;
                }
                if (testTarget.getDistanceSq(AutoCrystal.mc.player.getPosition()) >= 169.0) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    if (testTarget.getDistanceSq(blockPos) >= 169.0) {
                        continue;
                    }
                    final double targetDamage = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)testTarget);
                    final double selfDamage = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)AutoCrystal.mc.player);
                    final float healthTarget = testTarget.getHealth() + testTarget.getAbsorptionAmount();
                    final float healthSelf = AutoCrystal.mc.player.getHealth() + AutoCrystal.mc.player.getAbsorptionAmount();
                    if (testTarget.getHealth() < 8.0f && this.tabbottMode.getValue()) {
                        minDam = 0.1;
                    }
                    if (targetDamage < minDam || (selfDamage > targetDamage && targetDamage < healthTarget) || selfDamage > this.maxSelfDamage.getValue()) {
                        continue;
                    }
                    if (healthSelf < 0.5 && totems == 0) {
                        continue;
                    }
                    if (targetDamage <= bestDam) {
                        continue;
                    }
                    bestDam = targetDamage;
                    targetBlock = blockPos;
                    target = testTarget;
                }
            }
        }
        if (target == null) {
            this.renderBlock = null;
            resetRotation();
        }
        return targetBlock;
    }
    
    public void breakCrystal() {
        this.crystal = this.getBestCrystal(this.hitRange.getValue());
        if (this.crystal == null) {
            return;
        }
        if (this.explode.getValue()) {
            if (this.antiWeakness.getValue() && AutoCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                if (!this.isAttacking) {
                    this.oldSlot = AutoCrystal.mc.player.inventory.currentItem;
                    this.isAttacking = true;
                }
                this.newSlot = -1;
                for (int i = 0; i < 9; ++i) {
                    final ItemStack stack = AutoCrystal.mc.player.inventory.getStackInSlot(i);
                    if (stack != ItemStack.EMPTY) {
                        if (stack.getItem() instanceof ItemSword) {
                            this.newSlot = i;
                            break;
                        }
                        if (stack.getItem() instanceof ItemTool) {
                            this.newSlot = i;
                            break;
                        }
                    }
                }
                if (this.newSlot != -1) {
                    AutoCrystal.mc.player.inventory.currentItem = this.newSlot;
                    this.switchCooldown = true;
                }
            }
            if (this.debug.getValue()) {
                Command.sendChatMessage("hitting crystal");
            }
            this.lookAtPacket(this.crystal.posX, this.crystal.posY, this.crystal.posZ, (EntityPlayer)AutoCrystal.mc.player);
            AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, (Entity)this.crystal);
            AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
        this.hitDelayCounter = 0;
    }
    
    public void placeCrystal() {
        if (this.oldSlot != -1) {
            AutoCrystal.mc.player.inventory.currentItem = this.oldSlot;
            this.oldSlot = -1;
        }
        this.isAttacking = false;
        int crystalSlot = (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystal.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int i = 0; i < 9; ++i) {
                if (AutoCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = i;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        final BlockPos targetBlock = this.getBestBlock();
        if (targetBlock == null) {
            return;
        }
        this.renderBlock = targetBlock;
        if (this.place.getValue()) {
            if (!offhand && AutoCrystal.mc.player.inventory.currentItem != crystalSlot) {
                AutoCrystal.mc.player.inventory.currentItem = crystalSlot;
                resetRotation();
                this.switchCooldown = true;
            }
            this.lookAtPacket(targetBlock.x + 0.5, targetBlock.y - 0.5, targetBlock.z + 0.5, (EntityPlayer)AutoCrystal.mc.player);
            final RayTraceResult result = AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d(targetBlock.x + 0.5, targetBlock.y - 0.5, targetBlock.z + 0.5));
            final EnumFacing f = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
            if (this.switchCooldown) {
                this.switchCooldown = false;
                return;
            }
            if (this.debug.getValue()) {
                Command.sendChatMessage("placing crystal");
            }
            AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(targetBlock, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        }
        if (this.rotate.getValue() && AutoCrystal.isSpoofingAngles) {
            if (AutoCrystal.togglePitch) {
                AutoCrystal.mc.player.rotationPitch += (float)4.0E-4;
                AutoCrystal.togglePitch = false;
            }
            else {
                AutoCrystal.mc.player.rotationPitch -= (float)4.0E-4;
                AutoCrystal.togglePitch = true;
            }
        }
        this.placeDelayCounter = 0;
    }
    
    private void placeCrystalOffhand() {
        final int slot = this.findCrystalsInHotbar();
        if (this.getOffhand().getItem() == Items.END_CRYSTAL || slot == -1) {
            return;
        }
        if (this.debug.getValue()) {
            Command.sendChatMessage("swapping " + AutoCrystal.mc.player.inventory.getStackInSlot(45).getItem());
            Command.sendChatMessage("with " + AutoCrystal.mc.player.inventory.getStackInSlot(slot).getItem());
        }
        this.invPickup(slot);
        this.invPickup(45);
        this.invPickup(slot);
    }
    
    private void invPickup(final int slot) {
        AutoCrystal.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)AutoCrystal.mc.player);
    }
    
    private ItemStack getOffhand() {
        return AutoCrystal.mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
    }
    
    public int getTotems() {
        return this.offhand() + AutoCrystal.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::func_190916_E).sum();
    }
    
    public int offhand() {
        if (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (AutoCrystal.mc.getRenderManager().options == null) {
            return;
        }
        if (this.renderBlock != null) {
            if (this.rainbow.getValue()) {
                this.rgbc = Color.getHSBColor(this.hue, this.satuation.getValue(), this.brightness.getValue());
                this.drawBlock(this.renderBlock, this.rgbc.getRed(), this.rgbc.getGreen(), this.rgbc.getBlue());
                if (this.hue + this.speed.getValue() / 200.0f > 1.0f) {
                    this.hue = 0.0f;
                }
                else {
                    this.hue += this.speed.getValue() / 200.0f;
                }
            }
            else {
                this.drawBlock(this.renderBlock, this.red.getValue(), this.green.getValue(), this.blue.getValue());
            }
        }
    }
    
    private int findCrystalsInHotbar() {
        int slot = -1;
        for (int i = 44; i >= 9; --i) {
            if (AutoCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    private void drawBlock(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.alpha.getValue());
        wurstplusTessellator.prepare(7);
        if (this.renderMode.getValue().equals(RenderMode.UP)) {
            wurstplusTessellator.drawBox(blockPos, color.getRGB(), 2);
        }
        else if (this.renderMode.getValue().equals(RenderMode.SOLID)) {
            wurstplusTessellator.drawBox(blockPos, color.getRGB(), 63);
        }
        else if (this.renderMode.getValue().equals(RenderMode.OUTLINE)) {
            final IBlockState iBlockState2 = AutoCrystal.mc.world.getBlockState(blockPos);
            final Vec3d interp2 = interpolateEntity((Entity)AutoCrystal.mc.player, AutoCrystal.mc.getRenderPartialTicks());
            wurstplusTessellator.drawBoundingBox(iBlockState2.getSelectedBoundingBox((World)AutoCrystal.mc.world, blockPos).offset(-interp2.x, -interp2.y, -interp2.z), 1.5f, r, g, b, this.alpha.getValue());
        }
        else {
            final IBlockState iBlockState3 = AutoCrystal.mc.world.getBlockState(blockPos);
            final Vec3d interp3 = interpolateEntity((Entity)AutoCrystal.mc.player, AutoCrystal.mc.getRenderPartialTicks());
            wurstplusTessellator.drawFullBox(iBlockState3.getSelectedBoundingBox((World)AutoCrystal.mc.world, blockPos).offset(-interp3.x, -interp3.y, -interp3.z), blockPos, 1.5f, r, g, b, this.alpha.getValue());
        }
        wurstplusTessellator.release();
    }
    
    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (AutoCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || AutoCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && AutoCrystal.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && AutoCrystal.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(AutoCrystal.mc.player.posX), Math.floor(AutoCrystal.mc.player.posY), Math.floor(AutoCrystal.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks(final double range) {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)range, (int)range, false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
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
    
    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 9.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)AutoCrystal.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage -= damage / 4.0f;
            }
            return Math.max(damage - ep.getAbsorptionAmount(), 0.0f);
        }
        return CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystal.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        AutoCrystal.yaw = yaw1;
        AutoCrystal.pitch = pitch1;
        AutoCrystal.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (AutoCrystal.isSpoofingAngles) {
            AutoCrystal.yaw = AutoCrystal.mc.player.rotationYaw;
            AutoCrystal.pitch = AutoCrystal.mc.player.rotationPitch;
            AutoCrystal.isSpoofingAngles = false;
        }
    }
    
    @Override
    protected void onEnable() {
        Command.sendChatMessage("we §l§2gaming§r");
        if (AutoCrystal.mc.player == null) {
            this.disable();
        }
    }
    
    public void onDisable() {
        this.renderBlock = null;
        Command.sendChatMessage("we aint §l§4gaming§r no more");
        resetRotation();
    }
    
    static {
        AutoCrystal.togglePitch = false;
    }
    
    private enum RenderMode
    {
        SOLID, 
        OUTLINE, 
        UP, 
        FULL;
    }
    
    private enum PlaceMode
    {
        PLACEFIRST, 
        BREAKFIRST;
    }
}
