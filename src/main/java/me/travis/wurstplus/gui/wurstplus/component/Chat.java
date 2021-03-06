// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus.component;

import me.travis.wurstplus.gui.rgui.component.listen.KeyListener;
import me.travis.wurstplus.gui.rgui.component.Component;
import me.travis.wurstplus.gui.rgui.layout.Layout;
import me.travis.wurstplus.gui.wurstplus.Stretcherlayout;
import me.travis.wurstplus.gui.rgui.render.theme.Theme;
import me.travis.wurstplus.gui.rgui.component.use.InputField;
import me.travis.wurstplus.gui.rgui.component.use.Label;
import me.travis.wurstplus.gui.rgui.component.container.use.Scrollpane;
import me.travis.wurstplus.gui.rgui.component.container.AbstractContainer;

public class Chat extends AbstractContainer
{
    Scrollpane scrollpane;
    Label label;
    InputField field;
    
    public Chat(final Theme theme, final int width, final int height) {
        super(theme);
        this.label = new Label("", true);
        this.field = new InputField(width);
        (this.scrollpane = new Scrollpane(this.getTheme(), new Stretcherlayout(1), width, height)).setWidth(width);
        this.scrollpane.setHeight(height);
        this.scrollpane.setLockHeight(true).setLockWidth(true);
        this.scrollpane.addChild(this.label);
        this.field.addKeyListener(new KeyListener() {
            @Override
            public void onKeyDown(final KeyEvent event) {
                if (event.getKey() == 28) {
                    Chat.this.label.addLine(Chat.this.field.getText());
                    Chat.this.field.setText("");
                    if (Chat.this.scrollpane.canScrollY()) {
                        Chat.this.scrollpane.setScrolledY(Chat.this.scrollpane.getMaxScrollY());
                    }
                }
            }
            
            @Override
            public void onKeyUp(final KeyEvent event) {
            }
        });
        this.addChild(this.scrollpane);
        this.addChild(this.field);
        this.scrollpane.setLockHeight(false);
        this.scrollpane.setHeight(height - this.field.getHeight());
        this.scrollpane.setLockHeight(true);
        this.setWidth(width);
        this.setHeight(height);
        this.field.setY(this.getHeight() - this.field.getHeight());
    }
}
