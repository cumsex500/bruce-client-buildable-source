// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.setting.impl;

import com.google.common.base.Converter;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.travis.wurstplus.setting.converter.BooleanConverter;
import me.travis.wurstplus.setting.Setting;

public class BooleanSetting extends Setting<Boolean>
{
    private static final BooleanConverter converter;
    
    public BooleanSetting(final Boolean value, final Predicate<Boolean> restriction, final BiConsumer<Boolean, Boolean> consumer, final String name, final Predicate<Boolean> visibilityPredicate) {
        super(value, restriction, consumer, name, visibilityPredicate);
    }
    
    @Override
    public BooleanConverter converter() {
        return BooleanSetting.converter;
    }
    
    static {
        converter = new BooleanConverter();
    }
}
