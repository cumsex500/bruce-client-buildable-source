// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.misc;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.Packet;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import java.util.function.Predicate;
import me.travis.wurstplus.util.BlockInteractionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import me.travis.wurstplus.setting.Settings;
import me.travis.wurstplus.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;
import java.util.List;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "PacketFly", category = Category.MISC)
public class PacketFly extends Module
{
    public final Setting<Float> speed;
    public final Setting<Boolean> noKick;
    private int teleportId;
    private List<CPacketPlayer> packets;
    @EventHandler
    public Listener<InputUpdateEvent> listener;
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;
    
    public PacketFly() {
        this.speed = this.register((Setting<Float>)Settings.floatBuilder("Speed").withValue(0.1f).withMaximum(5.0f).withMinimum(0.0f).build());
        this.noKick = this.register(Settings.b("NoKick", true));
        this.packets = new ArrayList<CPacketPlayer>();
        final CPacketPlayer[] bounds = { null };
        final double[] ySpeed = { 0.0 };
        final double[] ySpeed2 = { 0.0 };
        final double[] n = { 0.0 };
        final double[][] directionalSpeed = new double[1][1];
        final int[] i = { 0 };
        final int[] j = { 0 };
        final int[] k = { 0 };
        final Object o;
        final Object o2;
        final Object o3;
        final Object o4;
        final Object o5;
        final Object o6;
        final int n2;
        final Object o7;
        final int n3;
        final Object o8;
        final int n4;
        this.listener = new Listener<InputUpdateEvent>(event -> {
            if (this.teleportId <= 0) {
                o[0] = new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX, 0.0, Minecraft.getMinecraft().player.posZ, Minecraft.getMinecraft().player.onGround);
                this.packets.add(o[0]);
                Minecraft.getMinecraft().player.connection.sendPacket(o[0]);
                return;
            }
            else {
                PacketFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                if (PacketFly.mc.world.getCollisionBoxes((Entity)PacketFly.mc.player, PacketFly.mc.player.getEntityBoundingBox().expand(-0.0625, 0.0, -0.0625)).isEmpty()) {
                    o2[0] = 0.0;
                    if (PacketFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                        if (this.noKick.getValue()) {
                            o3[0] = ((PacketFly.mc.player.ticksExisted % 20 == 0) ? -0.03999999910593033 : 0.06199999898672104);
                        }
                        else {
                            o3[0] = 0.06199999898672104;
                        }
                    }
                    else if (PacketFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        o3[0] = -0.062;
                    }
                    else {
                        if (PacketFly.mc.world.getCollisionBoxes((Entity)PacketFly.mc.player, PacketFly.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty()) {
                            if (PacketFly.mc.player.ticksExisted % 4 == 0) {
                                o4[0] = (double)(this.noKick.getValue() ? -0.04f : 0.0f);
                            }
                            else {
                                o4[0] = 0.0;
                            }
                        }
                        else {
                            o4[0] = 0.0;
                        }
                        o3[0] = o4[0];
                    }
                    o5[0] = BlockInteractionHelper.directionSpeed(this.speed.getValue());
                    if (PacketFly.mc.gameSettings.keyBindJump.isKeyDown() || PacketFly.mc.gameSettings.keyBindSneak.isKeyDown() || PacketFly.mc.gameSettings.keyBindForward.isKeyDown() || PacketFly.mc.gameSettings.keyBindBack.isKeyDown() || PacketFly.mc.gameSettings.keyBindRight.isKeyDown() || PacketFly.mc.gameSettings.keyBindLeft.isKeyDown()) {
                        if (o5[0][0] != 0.0 || o3[0] != 0.0 || o5[0][1] != 0.0) {
                            if (PacketFly.mc.player.movementInput.jump && (PacketFly.mc.player.moveStrafing != 0.0f || PacketFly.mc.player.moveForward != 0.0f)) {
                                PacketFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                                this.move(0.0, 0.0, 0.0);
                                o6[0] = false;
                                while (o6[0] <= 3) {
                                    PacketFly.mc.player.setVelocity(0.0, o3[0] * (double)o6[0], 0.0);
                                    this.move(0.0, o3[0] * (double)o6[0], 0.0);
                                    ++o6[n2];
                                }
                            }
                            else if (PacketFly.mc.player.movementInput.jump) {
                                PacketFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                                this.move(0.0, 0.0, 0.0);
                                o7[0] = false;
                                while (o7[0] <= 3) {
                                    PacketFly.mc.player.setVelocity(0.0, o3[0] * (double)o7[0], 0.0);
                                    this.move(0.0, o3[0] * (double)o7[0], 0.0);
                                    ++o7[n3];
                                }
                            }
                            else {
                                o8[0] = false;
                                while (o8[0] <= 2) {
                                    PacketFly.mc.player.setVelocity(o5[0][0] * (double)o8[0], o3[0] * (double)o8[0], o5[0][1] * (double)o8[0]);
                                    this.move(o5[0][0] * (double)o8[0], o3[0] * (double)o8[0], o5[0][1] * (double)o8[0]);
                                    ++o8[n4];
                                }
                            }
                        }
                    }
                    else if (this.noKick.getValue() && PacketFly.mc.world.getCollisionBoxes((Entity)PacketFly.mc.player, PacketFly.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty()) {
                        PacketFly.mc.player.setVelocity(0.0, (PacketFly.mc.player.ticksExisted % 2 == 0) ? 0.03999999910593033 : -0.03999999910593033, 0.0);
                        this.move(0.0, (PacketFly.mc.player.ticksExisted % 2 == 0) ? 0.03999999910593033 : -0.03999999910593033, 0.0);
                    }
                }
                return;
            }
        }, (Predicate<InputUpdateEvent>[])new Predicate[0]);
        final CPacketPlayer[] packet = { null };
        final Object o9;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
                event.cancel();
            }
            if (event.getPacket() instanceof CPacketPlayer) {
                o9[0] = (CPacketPlayer)event.getPacket();
                if (this.packets.contains(o9[0])) {
                    this.packets.remove(o9[0]);
                }
                else {
                    event.cancel();
                }
            }
            return;
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        final SPacketPlayerPosLook[] packet2 = { null };
        final Object o10;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                o10[0] = (SPacketPlayerPosLook)event.getPacket();
                if (Minecraft.getMinecraft().player.isEntityAlive()) {
                    if (Minecraft.getMinecraft().world.isBlockLoaded(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ)) && !(Minecraft.getMinecraft().currentScreen instanceof GuiDownloadTerrain)) {
                        if (this.teleportId <= 0) {
                            this.teleportId = o10[0].getTeleportId();
                        }
                        else {
                            event.cancel();
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    public void onEnable() {
        if (PacketFly.mc.world != null) {
            this.teleportId = 0;
            this.packets.clear();
            final CPacketPlayer bounds = (CPacketPlayer)new CPacketPlayer.Position(PacketFly.mc.player.posX, 0.0, PacketFly.mc.player.posZ, PacketFly.mc.player.onGround);
            this.packets.add(bounds);
            PacketFly.mc.player.connection.sendPacket((Packet)bounds);
        }
    }
    
    private void move(final double x, final double y, final double z) {
        final Minecraft mc = Minecraft.getMinecraft();
        final CPacketPlayer pos = (CPacketPlayer)new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z, mc.player.onGround);
        this.packets.add(pos);
        mc.player.connection.sendPacket((Packet)pos);
        final CPacketPlayer bounds = (CPacketPlayer)new CPacketPlayer.Position(mc.player.posX + x, 0.0, mc.player.posZ + z, mc.player.onGround);
        this.packets.add(bounds);
        mc.player.connection.sendPacket((Packet)bounds);
        ++this.teleportId;
        mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(this.teleportId - 1));
        mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(this.teleportId));
        mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(this.teleportId + 1));
    }
}
