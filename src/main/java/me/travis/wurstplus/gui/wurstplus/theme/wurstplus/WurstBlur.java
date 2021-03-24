// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus.theme.wurstplus;

import me.travis.wurstplus.gui.rgui.component.Component;
import me.travis.wurstplus.gui.wurstplus.RenderHelper;
import me.travis.wurstplus.gui.rgui.render.font.FontRenderer;
import me.travis.wurstplus.gui.rgui.render.AbstractComponentUI;
import me.travis.wurstplus.gui.rgui.component.container.use.Frame;

public class WurstBlur<T extends Frame> extends AbstractComponentUI<Frame>
{
    @Override
    public void renderComponent(final Frame component, final FontRenderer fontRenderer) {
        RenderHelper.blurScreen();
    }
}
