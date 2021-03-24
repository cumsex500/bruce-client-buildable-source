// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.setting.builder.primitive;

import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.setting.impl.StringSetting;
import me.travis.wurstplus.setting.builder.SettingBuilder;

public class StringSettingBuilder extends SettingBuilder<String>
{
    @Override
    public StringSetting build() {
        return new StringSetting((String)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate());
    }
}
