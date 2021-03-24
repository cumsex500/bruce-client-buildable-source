// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.command.commands;

import java.util.Comparator;
import me.travis.wurstplus.wurstplusMod;
import me.travis.wurstplus.command.syntax.SyntaxChunk;
import me.travis.wurstplus.command.Command;

public class CommandsCommand extends Command
{
    public CommandsCommand() {
        super("commands", SyntaxChunk.EMPTY);
    }
    
    @Override
    public void call(final String[] args) {
        wurstplusMod.getInstance().getCommandManager().getCommands().stream().sorted(Comparator.comparing(command -> command.getLabel())).forEach(command -> Command.sendChatMessage("&7" + Command.getCommandPrefix() + command.getLabel() + "&r ~ &8" + command.getDescription()));
    }
}
