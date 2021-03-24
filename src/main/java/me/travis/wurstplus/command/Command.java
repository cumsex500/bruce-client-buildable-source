// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.text.TextComponentBase;
import me.travis.wurstplus.setting.Settings;
import net.minecraft.util.text.ITextComponent;
import me.travis.wurstplus.util.Wrapper;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.command.syntax.SyntaxChunk;

public abstract class Command
{
    protected String label;
    protected String syntax;
    protected String description;
    protected SyntaxChunk[] syntaxChunks;
    public static Setting<String> commandPrefix;
    
    public Command(final String label, final SyntaxChunk[] syntaxChunks) {
        this.label = label;
        this.syntaxChunks = syntaxChunks;
        this.description = "Descriptionless";
    }
    
    public static void sendChatMessage(final String message) {
        sendRawChatMessage("\u00a73[\u00a76bruceclient\u00a73] &r" + message);
    }
    
    public static void sendStringChatMessage(final String[] messages) {
        sendChatMessage("");
        for (final String s : messages) {
            sendRawChatMessage(s);
        }
    }
    
    public static void sendRawChatMessage(final String message) {
        try {
            Wrapper.getPlayer().sendMessage((ITextComponent)new ChatMessage(message));
        }
        catch (Exception ex) {}
    }
    
    protected void setDescription(final String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public static String getCommandPrefix() {
        return Command.commandPrefix.getValue();
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public abstract void call(final String[] p0);
    
    public SyntaxChunk[] getSyntaxChunks() {
        return this.syntaxChunks;
    }
    
    protected SyntaxChunk getSyntaxChunk(final String name) {
        for (final SyntaxChunk c : this.syntaxChunks) {
            if (c.getType().equals(name)) {
                return c;
            }
        }
        return null;
    }
    
    public static char SECTIONSIGN() {
        return '\u00a7';
    }
    
    static {
        Command.commandPrefix = Settings.s("commandPrefix", "`");
    }
    
    public static class ChatMessage extends TextComponentBase
    {
        String text;
        
        public ChatMessage(final String text) {
            final Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            final Matcher m = p.matcher(text);
            final StringBuffer sb = new StringBuffer();
            while (m.find()) {
                final String replacement = "\u00a7" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }
            m.appendTail(sb);
            this.text = sb.toString();
        }
        
        public String getUnformattedComponentText() {
            return this.text;
        }
        
        public ITextComponent createCopy() {
            return (ITextComponent)new ChatMessage(this.text);
        }
    }
}
