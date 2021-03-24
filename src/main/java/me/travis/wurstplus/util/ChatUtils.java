// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.util;

import java.util.Date;
import java.text.SimpleDateFormat;
import com.mojang.realmsclient.gui.ChatFormatting;

public class ChatUtils
{
    public static String getChatTimestamp() {
        final String timestamp = ChatFormatting.GRAY + "§6<" + new SimpleDateFormat("k:mm a").format(new Date()) + ">" + ChatFormatting.RESET + " ";
        return timestamp;
    }
}
