// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.misc;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import me.travis.wurstplus.command.Command;
import me.travis.wurstplus.util.WorldUtils;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import me.travis.wurstplus.util.BlockInteractionHelper;
import java.util.Collection;
import net.minecraft.init.Blocks;
import me.travis.wurstplus.module.modules.combat.CrystalAura;
import net.minecraft.util.NonNullList;
import me.travis.wurstplus.setting.Settings;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "Stash Finder", category = Category.MISC)
public class StashFinder extends Module
{
    private Setting<Integer> chestCount;
    private Setting<Integer> waitDelay;
    private List<BlockPos> positions;
    private int wait;
    
    public StashFinder() {
        this.chestCount = this.register((Setting<Integer>)Settings.integerBuilder("Chests Per Chunk").withRange(4, 20).withValue(6).build());
        this.waitDelay = this.register((Setting<Integer>)Settings.integerBuilder("waitDelay (seconds)").withRange(2, 30).withValue(5).build());
    }
    
    @Override
    protected void onEnable() {
        this.wait = this.waitDelay.getValue();
        (this.positions = (List<BlockPos>)NonNullList.create()).clear();
    }
    
    @Override
    protected void onDisable() {
        this.positions.clear();
    }
    
    @Override
    public void onUpdate() {
        if (this.isDisabled() || StashFinder.mc.world == null) {
            return;
        }
        if (this.wait > this.waitDelay.getValue() * 20 && this.isChestBlocks()) {
            this.saveCoords();
            this.wait = 0;
        }
        ++this.wait;
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(CrystalAura.mc.player.posX), Math.floor(CrystalAura.mc.player.posY), Math.floor(CrystalAura.mc.player.posZ));
    }
    
    public Boolean isChest(final BlockPos blockPos) {
        if (StashFinder.mc.world.getBlockState(blockPos).getBlock() == Blocks.CHEST || StashFinder.mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || StashFinder.mc.world.getBlockState(blockPos).getBlock() == Blocks.TRAPPED_CHEST) {
            return true;
        }
        return false;
    }
    
    private boolean isChestBlocks() {
        int c = 0;
        this.positions.clear();
        this.positions.addAll(BlockInteractionHelper.getSphere(getPlayerPos(), 256.0f, 256, false, true, 0).stream().filter((Predicate<? super Object>)this::isChest).collect((Collector<? super Object, ?, Collection<? extends BlockPos>>)Collectors.toList()));
        for (final BlockPos bp : this.positions) {
            ++c;
        }
        return !this.positions.isEmpty() && c > this.chestCount.getValue();
    }
    
    public void saveCoords() {
        final Vec3d playerPos = new Vec3d((double)(int)StashFinder.mc.player.posX, (double)(int)StashFinder.mc.player.posY, (double)(int)StashFinder.mc.player.posZ);
        this.saveFile(WorldUtils.vectorToString(playerPos, new boolean[0]));
    }
    
    public void saveFile(final String pos) {
        Command.sendChatMessage("found a thing, saving file");
        try {
            final File file = new File("./wurst+_stash.txt");
            file.getParentFile().mkdirs();
            final PrintWriter writer = new PrintWriter(new FileWriter(file, true));
            writer.println("Found a stash @ " + pos);
            writer.close();
        }
        catch (Exception e) {
            Command.sendChatMessage("Error saving file: " + e);
        }
    }
}
