// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.event.events;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.network.NetworkPlayerInfo;
import me.travis.wurstplus.event.wurstplusEvent;

public class PlayerSkin extends wurstplusEvent
{
    private final NetworkPlayerInfo networkPlayerInfo;
    
    public PlayerSkin(final NetworkPlayerInfo networkPlayerInfo) {
        this.networkPlayerInfo = networkPlayerInfo;
    }
    
    public NetworkPlayerInfo getNetworkPlayerInfo() {
        return this.networkPlayerInfo;
    }
    
    public static class HasSkin extends PlayerSkin
    {
        public boolean result;
        
        public HasSkin(final NetworkPlayerInfo networkPlayerInfo, final boolean result) {
            super(networkPlayerInfo);
            this.result = result;
        }
    }
    
    public static class GetSkin extends PlayerSkin
    {
        public ResourceLocation skinLocation;
        
        public GetSkin(final NetworkPlayerInfo networkPlayerInfo, final ResourceLocation skinLocation) {
            super(networkPlayerInfo);
            this.skinLocation = skinLocation;
        }
    }
}
