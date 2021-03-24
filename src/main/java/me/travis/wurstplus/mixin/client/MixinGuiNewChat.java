// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.mixin.client;

import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraft.util.text.TextComponentString;
import me.travis.wurstplus.util.ChatUtils;
import me.travis.wurstplus.module.ModuleManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat
{
    @ModifyVariable(method = { "printChatMessageWithOptionalDeletion" }, at = @At("HEAD"))
    private ITextComponent addTimestamp(final ITextComponent componentIn) {
        if (ModuleManager.isModuleEnabled("ChatTimeStamps")) {
            final TextComponentString newComponent = new TextComponentString(ChatUtils.getChatTimestamp());
            newComponent.appendSibling(componentIn);
            return (ITextComponent)newComponent;
        }
        return componentIn;
    }
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V", ordinal = 0))
    private void overrideChatBackgroundColour(final int left, final int top, final int right, final int bottom, final int color) {
        if (!ModuleManager.isModuleEnabled("RemoveChatBox")) {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }
}
