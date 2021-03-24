// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.setting.builder.numerical;

import me.travis.wurstplus.setting.builder.SettingBuilder;
import me.travis.wurstplus.setting.Setting;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.travis.wurstplus.setting.impl.numerical.DoubleSetting;
import me.travis.wurstplus.setting.impl.numerical.NumberSetting;

public class DoubleSettingBuilder extends NumericalSettingBuilder<Double>
{
    @Override
    public NumberSetting build() {
        return new DoubleSetting((Double)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate(), (Double)this.min, (Double)this.max);
    }
    
    @Override
    public DoubleSettingBuilder withVisibility(final Predicate<Double> predicate) {
        return (DoubleSettingBuilder)super.withVisibility(predicate);
    }
    
    @Override
    public DoubleSettingBuilder withRestriction(final Predicate<Double> predicate) {
        return (DoubleSettingBuilder)super.withRestriction(predicate);
    }
    
    @Override
    public DoubleSettingBuilder withConsumer(final BiConsumer<Double, Double> consumer) {
        return (DoubleSettingBuilder)super.withConsumer(consumer);
    }
    
    @Override
    public DoubleSettingBuilder withValue(final Double value) {
        return (DoubleSettingBuilder)super.withValue(value);
    }
    
    @Override
    public DoubleSettingBuilder withRange(final Double minimum, final Double maximum) {
        return (DoubleSettingBuilder)super.withRange(minimum, maximum);
    }
    
    @Override
    public DoubleSettingBuilder withMaximum(final Double maximum) {
        return (DoubleSettingBuilder)super.withMaximum(maximum);
    }
    
    @Override
    public DoubleSettingBuilder withListener(final BiConsumer<Double, Double> consumer) {
        return (DoubleSettingBuilder)super.withListener(consumer);
    }
    
    @Override
    public DoubleSettingBuilder withName(final String name) {
        return (DoubleSettingBuilder)super.withName(name);
    }
    
    @Override
    public DoubleSettingBuilder withMinimum(final Double minimum) {
        return (DoubleSettingBuilder)super.withMinimum(minimum);
    }
}
