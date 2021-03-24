// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus.theme.wurstplus;

import me.travis.wurstplus.gui.rgui.component.Component;
import me.travis.wurstplus.gui.rgui.component.container.Container;
import me.travis.wurstplus.gui.wurstplus.wurstplusGUI;
import me.travis.wurstplus.gui.wurstplus.RenderHelper;
import org.lwjgl.opengl.GL11;
import me.travis.wurstplus.gui.rgui.render.font.FontRenderer;
import java.awt.Color;
import me.travis.wurstplus.gui.rgui.render.AbstractComponentUI;
import me.travis.wurstplus.gui.rgui.component.use.Button;

public class RootButtonUI<T extends Button> extends AbstractComponentUI<Button>
{
    protected Color idleColour;
    protected Color downColour;
    
    public RootButtonUI() {
        this.idleColour = new Color(19, 25, 142);
        this.downColour = new Color(21, 35, 137);
    }
    
    @Override
    public void renderComponent(final Button component, final FontRenderer ff) {
        GL11.glColor3f(0.0f, 0.0f, 102.0f);
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor3f(0.0f, 0.0f, 255.0f);
        }
        RenderHelper.drawRoundedRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight(), 3.0f);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        wurstplusGUI.fontRenderer.drawString(component.getWidth() / 2 - wurstplusGUI.fontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? this.downColour : this.idleColour, component.getName());
        GL11.glDisable(3553);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleAddComponent(final Button component, final Container container) {
        component.setWidth(wurstplusGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(wurstplusGUI.fontRenderer.getFontHeight() + 2);
    }
}
