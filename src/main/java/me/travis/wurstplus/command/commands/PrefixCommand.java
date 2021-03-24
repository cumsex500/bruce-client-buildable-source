// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.command.commands;

import me.travis.wurstplus.command.syntax.ChunkBuilder;
import me.travis.wurstplus.command.Command;

public class PrefixCommand extends Command
{
    public PrefixCommand() {
        super("prefix", new ChunkBuilder().append("character").build());
    }
    
    @Override
    public void call(final String[] args) {
        if (args.length == 0) {
            Command.sendChatMessage("State ur prefix cuz");
            return;
        }
        Command.commandPrefix.setValue(args[0]);
        Command.sendChatMessage(" prefix was set to &b" + Command.commandPrefix.getValue());
    }
}
