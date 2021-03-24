// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus.theme.wurstplus;

import me.travis.wurstplus.gui.rgui.component.Component;
import me.travis.wurstplus.gui.wurstplus.wurstplusGUI;
import org.lwjgl.opengl.GL11;
import me.travis.wurstplus.gui.rgui.render.font.FontRenderer;
import me.travis.wurstplus.gui.rgui.component.use.CheckButton;
import java.awt.Color;
import me.travis.wurstplus.gui.wurstplus.RootSmallFontRenderer;
import me.travis.wurstplus.gui.wurstplus.component.ColorizedCheckButton;

public class RootColorizedCheckButtonUI extends RootCheckButtonUI<ColorizedCheckButton>
{
    RootSmallFontRenderer ff;
    
    public RootColorizedCheckButtonUI() {
        this.ff = new RootSmallFontRenderer();
        this.backgroundColour = new Color(200, this.backgroundColour.getGreen(), this.backgroundColour.getBlue());
        this.backgroundColourHover = new Color(255, this.backgroundColourHover.getGreen(), this.backgroundColourHover.getBlue());
        this.downColourNormal = new Color(31, 31, 238);
    }
    
    @Override
    public void renderComponent(final CheckButton component, final FontRenderer aa) {
        GL11.glColor4f(this.backgroundColour.getRed() / 255.0f, this.backgroundColour.getGreen() / 255.0f, this.backgroundColour.getBlue() / 255.0f, component.getOpacity());
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor4f(this.backgroundColourHover.getRed() / 255.0f, this.backgroundColourHover.getGreen() / 255.0f, this.backgroundColourHover.getBlue() / 255.0f, component.getOpacity());
        }
        if (component.isToggled()) {
            GL11.glColor3f(this.backgroundColour.getRed() / 255.0f, this.backgroundColour.getGreen() / 255.0f, this.backgroundColour.getBlue() / 255.0f);
        }
        GL11.glLineWidth(2.5f);
        GL11.glBegin(1);
        GL11.glVertex2d(0.0, (double)component.getHeight());
        GL11.glVertex2d((double)component.getWidth(), (double)component.getHeight());
        GL11.glEnd();
        final Color idleColour = component.isToggled() ? this.idleColourToggle : this.idleColourNormal;
        final Color downColour = component.isToggled() ? this.downColourToggle : this.downColourNormal;
        GL11.glColor3f(1.0f, 1.0f, 102.0f);
        GL11.glEnable(3553);
        this.ff.drawString(component.getWidth() / 2 - wurstplusGUI.fontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? downColour : idleColour, component.getName());
        GL11.glDisable(3553);
    }
}
