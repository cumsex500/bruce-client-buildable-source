// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus.theme.wurstplus;

import me.travis.wurstplus.gui.rgui.component.Component;
import me.travis.wurstplus.gui.rgui.component.container.Container;
import me.travis.wurstplus.gui.wurstplus.RenderHelper;
import org.lwjgl.opengl.GL11;
import me.travis.wurstplus.gui.rgui.render.font.FontRenderer;
import me.travis.wurstplus.gui.wurstplus.RootSmallFontRenderer;
import me.travis.wurstplus.gui.rgui.component.use.Slider;
import me.travis.wurstplus.gui.rgui.render.AbstractComponentUI;

public class RootSliderUI extends AbstractComponentUI<Slider>
{
    RootSmallFontRenderer smallFontRenderer;
    
    public RootSliderUI() {
        this.smallFontRenderer = new RootSmallFontRenderer();
    }
    
    @Override
    public void renderComponent(final Slider component, final FontRenderer aa) {
        GL11.glColor4f(1.0f, 25.0f, 255.0f, component.getOpacity());
        GL11.glLineWidth(2.5f);
        final int height = component.getHeight();
        double w = component.getWidth() * (component.getValue() / component.getMaximum());
        final float downscale = 1.1f;
        GL11.glBegin(1);
        GL11.glVertex2d(0.0, (double)(height / downscale));
        GL11.glVertex2d(w, (double)(height / downscale));
        GL11.glColor3f(0.0f, 25.0f, 255.0f);
        GL11.glVertex2d(w, (double)(height / downscale));
        GL11.glVertex2d((double)component.getWidth(), (double)(height / downscale));
        GL11.glEnd();
        GL11.glColor3f(1.0f, 25.0f, 250.0f);
        RenderHelper.drawCircle((float)(int)w, height / downscale, 2.0f);
        String s = "";
        if (Math.floor(component.getValue()) == component.getValue()) {
            s += (int)component.getValue();
        }
        else {
            s += component.getValue();
        }
        if (component.isPressed()) {
            w -= this.smallFontRenderer.getStringWidth(s) / 2;
            w = Math.max(0.0, Math.min(w, component.getWidth() - this.smallFontRenderer.getStringWidth(s)));
            this.smallFontRenderer.drawString((int)w, 0, s);
        }
        else {
            this.smallFontRenderer.drawString(0, 0, component.getText());
            this.smallFontRenderer.drawString(component.getWidth() - this.smallFontRenderer.getStringWidth(s), 0, s);
        }
        GL11.glDisable(3553);
    }
    
    @Override
    public void handleAddComponent(final Slider component, final Container container) {
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight() + 2);
        component.setWidth(this.smallFontRenderer.getStringWidth(component.getText()) + this.smallFontRenderer.getStringWidth(component.getMaximum() + "") + 3);
    }
}
