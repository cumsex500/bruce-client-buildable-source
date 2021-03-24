// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus.theme.wurstplus;

import me.travis.wurstplus.gui.rgui.component.Component;
import me.travis.wurstplus.gui.rgui.component.container.Container;
import me.travis.wurstplus.gui.wurstplus.wurstplusGUI;
import org.lwjgl.opengl.GL11;
import me.travis.wurstplus.gui.rgui.render.font.FontRenderer;
import java.awt.Color;
import me.travis.wurstplus.gui.rgui.render.AbstractComponentUI;
import me.travis.wurstplus.gui.rgui.component.use.CheckButton;

public class RootCheckButtonUI<T extends CheckButton> extends AbstractComponentUI<CheckButton>
{
    protected Color backgroundColour;
    protected Color backgroundColourHover;
    protected Color idleColourNormal;
    protected Color downColourNormal;
    protected Color idleColourToggle;
    protected Color downColourToggle;
    
    public RootCheckButtonUI() {
        this.backgroundColour = new Color(20, 52, 120);
        this.backgroundColourHover = new Color(22, 28, 147);
        this.idleColourNormal = new Color(29, 46, 149);
        this.downColourNormal = new Color(24, 59, 160);
        this.idleColourToggle = new Color(27, 42, 144);
        this.downColourToggle = this.idleColourToggle.brighter();
    }
    
    @Override
    public void renderComponent(final CheckButton component, final FontRenderer ff) {
        GL11.glColor4f(this.backgroundColour.getRed() / 255.0f, this.backgroundColour.getGreen() / 255.0f, this.backgroundColour.getBlue() / 255.0f, component.getOpacity());
        if (component.isToggled()) {
            GL11.glColor3f(0.9f, this.backgroundColour.getGreen() / 255.0f, this.backgroundColour.getBlue() / 255.0f);
        }
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor4f(this.backgroundColourHover.getRed() / 255.0f, this.backgroundColourHover.getGreen() / 255.0f, this.backgroundColourHover.getBlue() / 255.0f, component.getOpacity());
        }
        final String text = component.getName();
        int c = component.isPressed() ? 11184810 : (component.isToggled() ? 16724787 : 14540253);
        if (component.isHovered()) {
            c = (c & 0x7F7F7F) << 1;
        }
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        wurstplusGUI.fontRenderer.drawString(component.getWidth() / 2 - wurstplusGUI.fontRenderer.getStringWidth(text) / 2, wurstplusGUI.fontRenderer.getFontHeight() / 2 - 2, c, text);
        GL11.glDisable(3553);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleAddComponent(final CheckButton component, final Container container) {
        component.setWidth(wurstplusGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(wurstplusGUI.fontRenderer.getFontHeight() + 2);
    }
}
