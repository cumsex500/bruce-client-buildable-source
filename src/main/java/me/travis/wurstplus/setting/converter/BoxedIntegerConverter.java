// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.setting.converter;

import com.google.gson.JsonElement;

public class BoxedIntegerConverter extends AbstractBoxedNumberConverter<Integer>
{
    protected Integer doBackward(final JsonElement s) {
        return s.getAsInt();
    }
}
