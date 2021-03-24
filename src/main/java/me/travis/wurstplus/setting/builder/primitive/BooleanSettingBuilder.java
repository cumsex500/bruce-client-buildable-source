// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.setting.builder.primitive;

import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.setting.impl.BooleanSetting;
import me.travis.wurstplus.setting.builder.SettingBuilder;

public class BooleanSettingBuilder extends SettingBuilder<Boolean>
{
    @Override
    public BooleanSetting build() {
        return new BooleanSetting((Boolean)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate());
    }
    
    @Override
    public BooleanSettingBuilder withName(final String name) {
        return (BooleanSettingBuilder)super.withName(name);
    }
}
