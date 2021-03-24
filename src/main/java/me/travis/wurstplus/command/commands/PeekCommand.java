// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.command.commands;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemShulkerBox;
import me.travis.wurstplus.util.Wrapper;
import me.travis.wurstplus.command.syntax.SyntaxChunk;
import net.minecraft.tileentity.TileEntityShulkerBox;
import me.travis.wurstplus.command.Command;

public class PeekCommand extends Command
{
    public static TileEntityShulkerBox sb;
    
    public PeekCommand() {
        super("peek", SyntaxChunk.EMPTY);
    }
    
    @Override
    public void call(final String[] args) {
        final ItemStack is = Wrapper.getPlayer().inventory.getCurrentItem();
        if (is.getItem() instanceof ItemShulkerBox) {
            final TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            entityBox.blockType = ((ItemShulkerBox)is.getItem()).getBlock();
            entityBox.setWorld(Wrapper.getWorld());
            entityBox.readFromNBT(is.getTagCompound().getCompoundTag("BlockEntityTag"));
            PeekCommand.sb = entityBox;
        }
        else {
            Command.sendChatMessage("You aren't carrying a shulker box.");
        }
    }
}
