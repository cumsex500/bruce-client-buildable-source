// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.Gson;

interface GsonConstant
{
    public static final Gson GSON = new Gson();
    public static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
    public static final JsonParser PARSER = new JsonParser();
}
