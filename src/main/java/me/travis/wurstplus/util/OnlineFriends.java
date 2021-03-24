// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.util;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import java.util.Collection;
import net.minecraft.entity.Entity;
import java.util.List;

public class OnlineFriends
{
    public static List<Entity> entities;
    
    public static List<Entity> getFriends() {
        OnlineFriends.entities.clear();
        OnlineFriends.entities.addAll((Collection<? extends Entity>)Minecraft.getMinecraft().world.playerEntities.stream().filter(entityPlayer -> Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        return OnlineFriends.entities;
    }
    
    static {
        OnlineFriends.entities = new ArrayList<Entity>();
    }
}
