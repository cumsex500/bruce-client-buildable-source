// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus.theme.wurstplus;

import me.travis.wurstplus.gui.rgui.component.Component;
import me.travis.wurstplus.gui.rgui.component.container.Container;
import me.travis.wurstplus.gui.wurstplus.RenderHelper;
import org.lwjgl.opengl.GL11;
import me.travis.wurstplus.gui.rgui.render.font.FontRenderer;
import me.travis.wurstplus.gui.rgui.render.AbstractComponentUI;
import me.travis.wurstplus.gui.rgui.component.use.InputField;

public class RootInputFieldUI<T extends InputField> extends AbstractComponentUI<InputField>
{
    @Override
    public void renderComponent(final InputField component, final FontRenderer fontRenderer) {
        GL11.glColor3f(0.0f, 0.0f, 102.0f);
        RenderHelper.drawFilledRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
        GL11.glLineWidth(1.5f);
        GL11.glColor4f(0.0f, 0.0f, 100.0f, 10.0f);
        RenderHelper.drawRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
    }
    
    @Override
    public void handleAddComponent(final InputField component, final Container container) {
        component.setWidth(200);
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight());
    }
}
