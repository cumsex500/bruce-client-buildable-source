// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.setting.converter;

import com.google.gson.JsonElement;

public class BoxedDoubleConverter extends AbstractBoxedNumberConverter<Double>
{
    protected Double doBackward(final JsonElement s) {
        return s.getAsDouble();
    }
}
