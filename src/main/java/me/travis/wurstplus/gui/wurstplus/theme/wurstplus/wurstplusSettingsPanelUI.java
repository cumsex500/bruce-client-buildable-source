// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus.theme.wurstplus;

import me.travis.wurstplus.gui.rgui.component.Component;
import me.travis.wurstplus.gui.wurstplus.RenderHelper;
import org.lwjgl.opengl.GL11;
import me.travis.wurstplus.gui.rgui.render.font.FontRenderer;
import me.travis.wurstplus.gui.wurstplus.component.SettingsPanel;
import me.travis.wurstplus.gui.rgui.render.AbstractComponentUI;

public class wurstplusSettingsPanelUI extends AbstractComponentUI<SettingsPanel>
{
    @Override
    public void renderComponent(final SettingsPanel component, final FontRenderer fontRenderer) {
        super.renderComponent(component, fontRenderer);
        GL11.glLineWidth(2.0f);
        GL11.glColor4f(0.0f, 65.0f, 255.0f, 0.9f);
        RenderHelper.drawFilledRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
        GL11.glColor3f(0.0f, 25.0f, 255.0f);
        GL11.glLineWidth(2.5f);
        RenderHelper.drawRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
    }
}
