// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.combat;

import java.util.HashSet;
import me.travis.wurstplus.util.BlockInteractionHelper;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import me.travis.wurstplus.util.EntityUtil;
import java.util.Comparator;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.RayTraceResult;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import me.travis.wurstplus.command.Command;
import net.minecraft.util.EnumFacing;
import me.travis.wurstplus.module.modules.chat.AutoEZ;
import me.travis.wurstplus.module.ModuleManager;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.travis.wurstplus.util.Friends;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import me.travis.wurstplus.util.wurstplusTessellator;
import me.travis.wurstplus.event.events.RenderEvent;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import me.travis.wurstplus.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.travis.wurstplus.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Set;
import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "CrystalAura", category = Category.AURAS)
public class CrystalAura extends Module
{
    private Setting<Boolean> place;
    private Setting<Boolean> explode;
    private Setting<Boolean> experamental;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> antiWeakness;
    private Setting<Boolean> spoofRotations;
    private Setting<Boolean> cancelMining;
    private Setting<Boolean> fastMode;
    private Setting<Boolean> fuckedMode;
    private Setting<Boolean> debug;
    private Setting<Integer> hitTickDelay;
    private Setting<Double> hitRange;
    private Setting<Double> placeRange;
    private Setting<Double> minDamage;
    private Setting<Double> maxSelf;
    private Setting<Boolean> rainbow;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Float> satuation;
    private Setting<Float> brightness;
    private Setting<Integer> speed;
    private Setting<Integer> alpha;
    private Setting<RenderMode> renderMode;
    private Setting<Boolean> tabbottMode;
    private Setting<Boolean> travisMode;
    private Setting<Double> tabbottDamage;
    private BlockPos renderBlock;
    private boolean switchCooldown;
    private boolean isAttacking;
    private boolean flag;
    private boolean shouldBreak;
    private static boolean togglePitch;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    private int oldSlot;
    private int newSlot;
    private int hitDelayCounter;
    private float hue;
    private Color rgbc;
    public Set<EntityPlayer> fuckedPlayers;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public CrystalAura() {
        this.place = this.register(Settings.b("Place", true));
        this.explode = this.register(Settings.b("Explode", true));
        this.experamental = this.register(Settings.b("Experamental", true));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.antiWeakness = this.register(Settings.b("Anti Weakness", true));
        this.spoofRotations = this.register(Settings.b("Spoof Rotations", false));
        this.cancelMining = this.register(Settings.b("Cancel Mining", false));
        this.fastMode = this.register(Settings.b("Fast Mode", false));
        this.fuckedMode = this.register(Settings.b("Fucked Detector", false));
        this.debug = this.register(Settings.b("Debug Messages", false));
        this.hitTickDelay = this.register((Setting<Integer>)Settings.integerBuilder("Delay").withMinimum(0).withValue(4).withMaximum(6).build());
        this.hitRange = this.register((Setting<Double>)Settings.doubleBuilder("Hit Range").withMinimum(0.0).withValue(5.5).build());
        this.placeRange = this.register((Setting<Double>)Settings.doubleBuilder("Place Range").withMinimum(0.0).withValue(3.5).build());
        this.minDamage = this.register((Setting<Double>)Settings.doubleBuilder("Min Damage").withMinimum(0.0).withValue(2.0).withMaximum(20.0).build());
        this.maxSelf = this.register((Setting<Double>)Settings.doubleBuilder("Max Self").withMinimum(0.0).withValue(8.0).withMaximum(20.0).build());
        this.rainbow = this.register(Settings.b("RainbowMode", false));
        this.red = this.register(Settings.integerBuilder("Red").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.green = this.register(Settings.integerBuilder("Green").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.blue = this.register(Settings.integerBuilder("Blue").withRange(0, 255).withValue(255).withVisibility(o -> !this.rainbow.getValue()).build());
        this.satuation = this.register(Settings.floatBuilder("Saturation").withRange(0.0f, 1.0f).withValue(0.6f).withVisibility(o -> this.rainbow.getValue()).build());
        this.brightness = this.register(Settings.floatBuilder("Brightness").withRange(0.0f, 1.0f).withValue(0.6f).withVisibility(o -> this.rainbow.getValue()).build());
        this.speed = this.register(Settings.integerBuilder("Speed").withRange(0, 10).withValue(2).withVisibility(o -> this.rainbow.getValue()).build());
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Transparency").withRange(0, 255).withValue(70).build());
        this.renderMode = this.register(Settings.e("Render Mode", RenderMode.SOLID));
        this.tabbottMode = this.register(Settings.b("Tabbott Mode", false));
        this.travisMode = this.register(Settings.b("Travis Mode", false));
        this.tabbottDamage = this.register(Settings.doubleBuilder("Enemy Health Min").withMinimum(0.0).withValue(8.0).withMaximum(20.0).withVisibility(o -> this.tabbottMode.getValue()).build());
        this.switchCooldown = false;
        this.isAttacking = false;
        this.flag = false;
        this.shouldBreak = false;
        this.oldSlot = -1;
        Packet packet;
        this.packetListener = new Listener<PacketEvent.Send>(event -> {
            if (!(!this.spoofRotations.getValue())) {
                packet = event.getPacket();
                if (packet instanceof CPacketPlayer && CrystalAura.isSpoofingAngles) {
                    ((CPacketPlayer)packet).yaw = (float)CrystalAura.yaw;
                    ((CPacketPlayer)packet).pitch = (float)CrystalAura.pitch;
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(CrystalAura.mc.player.posX), Math.floor(CrystalAura.mc.player.posY), Math.floor(CrystalAura.mc.player.posZ));
    }
    
    static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)CrystalAura.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    private static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = CrystalAura.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        CrystalAura.yaw = yaw1;
        CrystalAura.pitch = pitch1;
        CrystalAura.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (CrystalAura.isSpoofingAngles) {
            CrystalAura.yaw = CrystalAura.mc.player.rotationYaw;
            CrystalAura.pitch = CrystalAura.mc.player.rotationPitch;
            CrystalAura.isSpoofingAngles = false;
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (CrystalAura.mc.getRenderManager().options == null) {
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
            final IBlockState iBlockState2 = CrystalAura.mc.world.getBlockState(blockPos);
            final Vec3d interp2 = interpolateEntity((Entity)CrystalAura.mc.player, CrystalAura.mc.getRenderPartialTicks());
            wurstplusTessellator.drawBoundingBox(iBlockState2.getSelectedBoundingBox((World)CrystalAura.mc.world, blockPos).offset(-interp2.x, -interp2.y, -interp2.z), 1.5f, r, g, b, this.alpha.getValue());
        }
        else {
            final IBlockState iBlockState3 = CrystalAura.mc.world.getBlockState(blockPos);
            final Vec3d interp3 = interpolateEntity((Entity)CrystalAura.mc.player, CrystalAura.mc.getRenderPartialTicks());
            wurstplusTessellator.drawFullBox(iBlockState3.getSelectedBoundingBox((World)CrystalAura.mc.world, blockPos).offset(-interp3.x, -interp3.y, -interp3.z), blockPos, 1.5f, r, g, b, this.alpha.getValue());
        }
        wurstplusTessellator.release();
    }
    
    public Boolean checkHole(final EntityPlayer ent) {
        final BlockPos pos = new BlockPos(ent.posX, ent.posY - 1.0, ent.posZ);
        if (CrystalAura.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
            return false;
        }
        if (this.canPlaceCrystal(pos.south()) || (this.canPlaceCrystal(pos.south().south()) && CrystalAura.mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR)) {
            return true;
        }
        if (this.canPlaceCrystal(pos.east()) || (this.canPlaceCrystal(pos.east().east()) && CrystalAura.mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR)) {
            return true;
        }
        if (this.canPlaceCrystal(pos.west()) || (this.canPlaceCrystal(pos.west().west()) && CrystalAura.mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR)) {
            return true;
        }
        if (this.canPlaceCrystal(pos.north()) || (this.canPlaceCrystal(pos.north().north()) && CrystalAura.mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR)) {
            return true;
        }
        return false;
    }
    
    public void placeCrystals() {
        final int crys = 1;
        if (this.oldSlot != -1) {
            CrystalAura.mc.player.inventory.currentItem = this.oldSlot;
            this.oldSlot = -1;
        }
        this.isAttacking = false;
        final int n;
        int crystalSlot = n = ((CrystalAura.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? CrystalAura.mc.player.inventory.currentItem : -1);
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (CrystalAura.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (CrystalAura.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        for (int i = 0; i < crys; ++i) {
            final List<BlockPos> blocks = this.findCrystalBlocks();
            final List<Entity> entities = new ArrayList<Entity>();
            entities.addAll((Collection<? extends Entity>)CrystalAura.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
            BlockPos targetBlock = null;
            double targetBlockDamage = 0.0;
            EntityPlayer target = null;
            double minDam = this.minDamage.getValue();
            for (final Entity entity2 : entities) {
                if (entity2 != CrystalAura.mc.player) {
                    if (!(entity2 instanceof EntityPlayer)) {
                        continue;
                    }
                    final EntityPlayer testTarget = (EntityPlayer)entity2;
                    if (testTarget.isDead || testTarget.getHealth() <= 0.0f) {
                        continue;
                    }
                    if (testTarget.getDistanceSq(CrystalAura.mc.player.getPosition()) >= 169.0) {
                        continue;
                    }
                    for (final BlockPos blockPos : blocks) {
                        if (testTarget.getDistanceSq(blockPos) >= 169.0) {
                            continue;
                        }
                        final double targetDamage = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)testTarget);
                        final double selfDamage = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)CrystalAura.mc.player);
                        final float healthTarget = testTarget.getHealth() + testTarget.getAbsorptionAmount();
                        final float healthSelf = CrystalAura.mc.player.getHealth() + CrystalAura.mc.player.getAbsorptionAmount();
                        if (testTarget.getHealth() < this.tabbottDamage.getValue() && this.tabbottMode.getValue()) {
                            minDam = 0.1;
                        }
                        if (targetDamage < minDam || selfDamage >= healthSelf - 0.5 || (selfDamage > targetDamage && targetDamage < healthTarget) || targetDamage <= targetBlockDamage || selfDamage > this.maxSelf.getValue()) {
                            continue;
                        }
                        if (CrystalAura.mc.player.getHealth() - selfDamage < 2.0) {
                            continue;
                        }
                        targetBlock = blockPos;
                        targetBlockDamage = targetDamage;
                        target = testTarget;
                    }
                }
            }
            if (target == null) {
                this.renderBlock = null;
                resetRotation();
                return;
            }
            this.renderBlock = targetBlock;
            if (ModuleManager.getModuleByName("AutoEZ").isEnabled()) {
                final AutoEZ autoGG = (AutoEZ)ModuleManager.getModuleByName("AutoEZ");
                autoGG.addTargetedPlayer(target.getName());
            }
            if (this.place.getValue()) {
                if (!offhand && CrystalAura.mc.player.inventory.currentItem != crystalSlot) {
                    if (this.autoSwitch.getValue()) {
                        CrystalAura.mc.player.inventory.currentItem = crystalSlot;
                        resetRotation();
                        this.switchCooldown = true;
                    }
                    return;
                }
                this.lookAtPacket(targetBlock.x + 0.5, targetBlock.y - 0.5, targetBlock.z + 0.5, (EntityPlayer)CrystalAura.mc.player);
                final RayTraceResult result = CrystalAura.mc.world.rayTraceBlocks(new Vec3d(CrystalAura.mc.player.posX, CrystalAura.mc.player.posY + CrystalAura.mc.player.getEyeHeight(), CrystalAura.mc.player.posZ), new Vec3d(targetBlock.x + 0.5, targetBlock.y - 0.5, targetBlock.z + 0.5));
                final EnumFacing f = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                if (this.switchCooldown) {
                    this.switchCooldown = false;
                    return;
                }
                if (this.debug.getValue()) {
                    Command.sendChatMessage("placing crystal");
                }
                CrystalAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(targetBlock, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
            if (this.spoofRotations.getValue() && CrystalAura.isSpoofingAngles) {
                if (CrystalAura.togglePitch) {
                    CrystalAura.mc.player.rotationPitch += (float)4.0E-4;
                    CrystalAura.togglePitch = false;
                }
                else {
                    CrystalAura.mc.player.rotationPitch -= (float)4.0E-4;
                    CrystalAura.togglePitch = true;
                }
            }
        }
    }
    
    public void breakCrystals(final EntityEnderCrystal crystal) {
        if (crystal == null) {
            return;
        }
        if (this.shouldBreak) {
            this.hitDelayCounter = 0;
            if (this.antiWeakness.getValue() && CrystalAura.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                if (!this.isAttacking) {
                    this.oldSlot = CrystalAura.mc.player.inventory.currentItem;
                    this.isAttacking = true;
                }
                this.newSlot = -1;
                for (int i = 0; i < 9; ++i) {
                    final ItemStack stack = CrystalAura.mc.player.inventory.getStackInSlot(i);
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
                    CrystalAura.mc.player.inventory.currentItem = this.newSlot;
                    this.switchCooldown = true;
                }
            }
            if (!this.travisMode.getValue()) {
                if (this.debug.getValue()) {
                    Command.sendChatMessage("hitting crystal");
                }
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)CrystalAura.mc.player);
                CrystalAura.mc.playerController.attackEntity((EntityPlayer)CrystalAura.mc.player, (Entity)crystal);
                CrystalAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                return;
            }
            final double selfDamage = calculateDamage(crystal.posX + 0.5, crystal.posY + 1.0, crystal.posZ + 0.5, (Entity)CrystalAura.mc.player);
            if (selfDamage < this.maxSelf.getValue() && CrystalAura.mc.player.getHealth() - selfDamage > 0.0) {
                if (this.debug.getValue()) {
                    Command.sendChatMessage("hitting crystal");
                }
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)CrystalAura.mc.player);
                CrystalAura.mc.playerController.attackEntity((EntityPlayer)CrystalAura.mc.player, (Entity)crystal);
                CrystalAura.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (CrystalAura.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE && !this.cancelMining.getValue() && CrystalAura.mc.player.isHandActive()) {
            return;
        }
        final EntityEnderCrystal crystal = (EntityEnderCrystal)CrystalAura.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> CrystalAura.mc.player.getDistance(c))).orElse(null);
        if (crystal != null && CrystalAura.mc.player.getDistance((Entity)crystal) <= this.hitRange.getValue()) {
            this.shouldBreak = true;
            if (this.hitDelayCounter < this.hitTickDelay.getValue()) {
                ++this.hitDelayCounter;
                this.shouldBreak = false;
                if (!this.fastMode.getValue()) {
                    return;
                }
            }
        }
        if (this.experamental.getValue()) {
            if (this.explode.getValue()) {
                this.placeCrystals();
            }
            if (this.place.getValue()) {
                this.breakCrystals(crystal);
            }
        }
        else {
            if (this.explode.getValue()) {
                this.breakCrystals(crystal);
            }
            if (this.place.getValue()) {
                this.placeCrystals();
            }
        }
        resetRotation();
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (CrystalAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || CrystalAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && CrystalAura.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && CrystalAura.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && CrystalAura.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && CrystalAura.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList positions = NonNullList.create();
        positions.addAll((Collection)BlockInteractionHelper.getSphere(getPlayerPos(), this.placeRange.getValue().floatValue(), this.placeRange.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
    
    public void onEnable() {
        this.fuckedPlayers = new HashSet<EntityPlayer>();
        if (CrystalAura.mc.player == null) {
            this.disable();
            return;
        }
        if (this.fuckedMode.getValue() && ModuleManager.getModuleByName("Fucked Detector").isDisabled()) {
            this.flag = true;
            ModuleManager.getModuleByName("Fucked Detector").enable();
        }
        Command.sendChatMessage("we §l§2gaming§r");
        this.hitDelayCounter = this.hitTickDelay.getValue();
        this.hue = 0.0f;
    }
    
    public void onDisable() {
        if (this.flag) {
            ModuleManager.getModuleByName("Fucked Detector").disable();
            this.flag = false;
        }
        this.renderBlock = null;
        resetRotation();
        Command.sendChatMessage("we aint §l§4gaming§r no more");
    }
    
    static {
        CrystalAura.togglePitch = false;
    }
    
    private enum RenderMode
    {
        SOLID, 
        OUTLINE, 
        UP, 
        FULL;
    }
}
