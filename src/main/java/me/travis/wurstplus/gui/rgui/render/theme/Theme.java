// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.rgui.render.theme;

import me.travis.wurstplus.gui.rgui.render.font.FontRenderer;
import me.travis.wurstplus.gui.rgui.render.ComponentUI;
import me.travis.wurstplus.gui.rgui.component.Component;

public interface Theme
{
    ComponentUI getUIForComponent(final Component p0);
    
    FontRenderer getFontRenderer();
}
