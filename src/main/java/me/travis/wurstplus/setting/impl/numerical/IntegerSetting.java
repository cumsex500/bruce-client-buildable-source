// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.setting.impl.numerical;

import com.google.common.base.Converter;
import me.travis.wurstplus.setting.converter.AbstractBoxedNumberConverter;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.travis.wurstplus.setting.converter.BoxedIntegerConverter;

public class IntegerSetting extends NumberSetting<Integer>
{
    private static final BoxedIntegerConverter converter;
    
    public IntegerSetting(final Integer value, final Predicate<Integer> restriction, final BiConsumer<Integer, Integer> consumer, final String name, final Predicate<Integer> visibilityPredicate, final Integer min, final Integer max) {
        super(value, restriction, consumer, name, visibilityPredicate, min, max);
    }
    
    @Override
    public AbstractBoxedNumberConverter converter() {
        return IntegerSetting.converter;
    }
    
    static {
        converter = new BoxedIntegerConverter();
    }
}
