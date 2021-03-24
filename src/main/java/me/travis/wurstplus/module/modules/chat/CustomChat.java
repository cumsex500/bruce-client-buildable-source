// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.chat;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.travis.wurstplus.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.travis.wurstplus.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.module.Module;

@Info(name = "CustomChat", category = Category.CHAT)
public class CustomChat extends Module
{
    private Setting<ChatSetting> mode;
    private Setting<Boolean> commands;
    private Setting<Boolean> twoBMode;
    public String wurstplus_SUFFIX;
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    
    public CustomChat() {
        this.mode = this.register(Settings.e("Mode", ChatSetting.Wurst));
        this.commands = this.register(Settings.b("Commands", false));
        this.twoBMode = this.register(Settings.b("2B Mode", false));
        String s;
        String s2;
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (this.mode.getValue() == ChatSetting.Wurst) {
                this.wurstplus_SUFFIX = (this.twoBMode.getValue() ? " | bruce client mode" : " \u2356 \u24b7\u24c7\u24ca\u24b8\u24ba \u24b8\u24c1\u24be\u24ba\u24c3\u24c9");
            }
            if (event.getPacket() instanceof CPacketChatMessage) {
                s = ((CPacketChatMessage)event.getPacket()).getMessage();
                if (!s.startsWith("/") || this.commands.getValue()) {
                    s2 = s + this.wurstplus_SUFFIX;
                    if (s2.length() >= 256) {
                        s2 = s2.substring(0, 256);
                    }
                    ((CPacketChatMessage)event.getPacket()).message = s2;
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public Boolean getCommand() {
        return this.commands.getValue();
    }
    
    public enum ChatSetting
    {
        Wurst;
    }
}
