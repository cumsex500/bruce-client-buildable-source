// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.setting.impl;

import com.google.common.base.Converter;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.travis.wurstplus.setting.converter.StringConverter;
import me.travis.wurstplus.setting.Setting;

public class StringSetting extends Setting<String>
{
    private static final StringConverter converter;
    
    public StringSetting(final String value, final Predicate<String> restriction, final BiConsumer<String, String> consumer, final String name, final Predicate<String> visibilityPredicate) {
        super(value, restriction, consumer, name, visibilityPredicate);
    }
    
    @Override
    public StringConverter converter() {
        return StringSetting.converter;
    }
    
    static {
        converter = new StringConverter();
    }
}
