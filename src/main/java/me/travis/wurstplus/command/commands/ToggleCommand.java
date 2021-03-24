// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.command.commands;

import me.travis.wurstplus.module.Module;
import me.travis.wurstplus.module.ModuleManager;
import me.travis.wurstplus.command.syntax.SyntaxParser;
import me.travis.wurstplus.command.syntax.parsers.ModuleParser;
import me.travis.wurstplus.command.syntax.ChunkBuilder;
import me.travis.wurstplus.command.Command;

public class ToggleCommand extends Command
{
    public ToggleCommand() {
        super("toggle", new ChunkBuilder().append("module", true, new ModuleParser()).build());
    }
    
    @Override
    public void call(final String[] args) {
        if (args.length == 0) {
            Command.sendChatMessage("Please specify a module!");
            return;
        }
        final Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Unknown module '" + args[0] + "'");
            return;
        }
        m.toggle();
        Command.sendChatMessage(m.getName() + (m.isEnabled() ? " &aenabled" : " &cdisabled"));
    }
}
