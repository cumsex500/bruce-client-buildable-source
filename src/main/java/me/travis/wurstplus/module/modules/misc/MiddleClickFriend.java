// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.misc;

import java.util.function.Predicate;
import java.util.ArrayList;
import me.travis.wurstplus.command.Command;
import me.travis.wurstplus.command.commands.FriendCommand;
import me.travis.wurstplus.util.Friends;
import net.minecraft.entity.player.EntityPlayer;
import me.zero.alpine.listener.EventHandler;
import me.travis.wurstplus.event.events.MiddleClickEvent;
import me.zero.alpine.listener.Listener;
import me.travis.wurstplus.module.Module;

@Info(name = "MiddleClick Friend", category = Category.RENDER)
public class MiddleClickFriend extends Module
{
    @EventHandler
    private Listener<MiddleClickEvent> listener;
    
    public MiddleClickFriend() {
        String name;
        final String str;
        Friends.Friend f;
        Friends instance;
        Friends instance2;
        Friends.Friend friend2;
        Friends instance3;
        this.listener = new Listener<MiddleClickEvent>(event -> {
            if (MiddleClickFriend.mc.objectMouseOver != null && MiddleClickFriend.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
                name = MiddleClickFriend.mc.objectMouseOver.entityHit.getName();
                if (!Friends.isFriend(name)) {
                    new Thread(() -> {
                        f = FriendCommand.getFriendByName(str);
                        if (f == null) {
                            Command.sendChatMessage("Failed to find UUID of " + str);
                        }
                        else {
                            instance = Friends.INSTANCE;
                            Friends.friends.getValue().add(f);
                            Command.sendChatMessage("Added &b" + str + "&r to friends list");
                        }
                    }).start();
                }
                else if (!Friends.isFriend(name)) {
                    Command.sendChatMessage("That player isn't your friend.");
                }
                else {
                    instance2 = Friends.INSTANCE;
                    friend2 = Friends.friends.getValue().stream().filter(friend1 -> friend1.getUsername().equalsIgnoreCase(name)).findFirst().get();
                    instance3 = Friends.INSTANCE;
                    Friends.friends.getValue().remove(friend2);
                    Command.sendChatMessage("&b" + friend2.getUsername() + "&r has been unfriended.");
                }
            }
        }, (Predicate<MiddleClickEvent>[])new Predicate[0]);
    }
    
    @Override
    protected void onEnable() {
    }
}
