// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.rgui.component.listen;

import me.travis.wurstplus.gui.rgui.component.Component;

public interface UpdateListener<T extends Component>
{
    void updateSize(final T p0, final int p1, final int p2);
    
    void updateLocation(final T p0, final int p1, final int p2);
}
