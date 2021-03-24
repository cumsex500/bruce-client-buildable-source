// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.util;

import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public final class Invdraw
{
    public void drawInventory(final Integer x, final Integer y, final EntityPlayerSP player) {
        final NonNullList<ItemStack> inventory = (NonNullList<ItemStack>)Minecraft.getMinecraft().player.inventory.mainInventory;
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/gui/container/shulker_box.png"));
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x - 8, y + 1, 0, 0, 176, 76);
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        for (int i = 9; i < inventory.size(); ++i) {
            GlStateManager.pushMatrix();
            GlStateManager.clear(256);
            GlStateManager.disableDepth();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.scale(1.0f, 1.0f, 0.01f);
            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((ItemStack)inventory.get(i), x + i % 9 * 18, (i / 9 + 1) * 18 + y + 1 - 18);
            Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRenderer, (ItemStack)inventory.get(i), x + i % 9 * 18, (i / 9 + 1) * 18 + y + 1 - 18);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.popMatrix();
        }
    }
}
