// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus.component;

import me.travis.wurstplus.gui.rgui.util.Docking;
import me.travis.wurstplus.gui.rgui.component.Component;
import me.travis.wurstplus.gui.rgui.util.ContainerHelper;
import me.travis.wurstplus.gui.rgui.component.container.use.Frame;
import me.travis.wurstplus.gui.rgui.component.listen.RenderListener;
import me.travis.wurstplus.gui.rgui.component.use.Label;

public class ActiveModules extends Label
{
    public boolean sort_up;
    
    public ActiveModules() {
        super("");
        this.sort_up = true;
        this.addRenderListener(new RenderListener() {
            @Override
            public void onPreRender() {
                final Frame parentFrame = ContainerHelper.getFirstParent((Class<? extends Frame>)Frame.class, (Component)ActiveModules.this);
                if (parentFrame == null) {
                    return;
                }
                final Docking docking = parentFrame.getDocking();
                if (docking.isTop()) {
                    ActiveModules.this.sort_up = true;
                }
                if (docking.isBottom()) {
                    ActiveModules.this.sort_up = false;
                }
            }
            
            @Override
            public void onPostRender() {
            }
        });
    }
}
