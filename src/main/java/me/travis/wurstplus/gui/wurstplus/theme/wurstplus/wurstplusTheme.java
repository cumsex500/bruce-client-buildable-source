// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus.theme.wurstplus;

import me.travis.wurstplus.gui.rgui.render.AbstractComponentUI;
import me.travis.wurstplus.gui.wurstplus.wurstplusGUI;
import me.travis.wurstplus.gui.wurstplus.theme.staticui.TabGuiUI;
import me.travis.wurstplus.gui.wurstplus.theme.staticui.RadarUI;
import me.travis.wurstplus.gui.rgui.render.ComponentUI;
import me.travis.wurstplus.gui.font.CFontRenderer;
import me.travis.wurstplus.gui.rgui.render.font.FontRenderer;
import me.travis.wurstplus.gui.rgui.render.theme.AbstractTheme;

public class wurstplusTheme extends AbstractTheme
{
    FontRenderer fontRenderer;
    CFontRenderer CFontRenderer;
    
    public wurstplusTheme() {
        this.installUI(new WurstBlur<Object>());
        this.installUI(new RootButtonUI<Object>());
        this.installUI(new GUIUI());
        this.installUI(new RootGroupboxUI());
        this.installUI(new wurstplusFrameUI<Object>());
        this.installUI(new RootScrollpaneUI());
        this.installUI(new RootInputFieldUI<Object>());
        this.installUI(new RootLabelUI<Object>());
        this.installUI(new RootChatUI());
        this.installUI(new RootCheckButtonUI<Object>());
        this.installUI(new wurstplusActiveModulesUI());
        this.installUI(new wurstplusSettingsPanelUI());
        this.installUI(new RootSliderUI());
        this.installUI(new wurstplusEnumbuttonUI());
        this.installUI(new RootColorizedCheckButtonUI());
        this.installUI(new wurstplusUnboundSliderUI());
        this.installUI(new RadarUI());
        this.installUI(new TabGuiUI());
        this.fontRenderer = wurstplusGUI.fontRenderer;
    }
    
    @Override
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public class GUIUI extends AbstractComponentUI<wurstplusGUI>
    {
    }
}
