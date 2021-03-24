// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.command.commands;

import me.travis.wurstplus.gui.wurstplus.wurstplusGUI;
import java.io.BufferedWriter;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.io.IOException;
import me.travis.wurstplus.wurstplusMod;
import me.travis.wurstplus.command.syntax.parsers.DependantParser;
import me.travis.wurstplus.command.syntax.SyntaxParser;
import me.travis.wurstplus.command.syntax.parsers.EnumParser;
import me.travis.wurstplus.command.syntax.ChunkBuilder;
import me.travis.wurstplus.command.Command;

public class ConfigCommand extends Command
{
    public ConfigCommand() {
        super("config", new ChunkBuilder().append("mode", true, new EnumParser(new String[] { "reload", "save", "path" })).append("path", true, new DependantParser(0, new DependantParser.Dependency(new String[][] { { "path", "path" } }, ""))).build());
    }
    
    @Override
    public void call(final String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Missing argument &bmode&r: Choose from reload, save or path");
            return;
        }
        final String lowerCase = args[0].toLowerCase();
        switch (lowerCase) {
            case "reload": {
                this.reload();
                return;
            }
            case "save": {
                try {
                    wurstplusMod.saveConfigurationUnsafe();
                    Command.sendChatMessage("Saved configuration!");
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Command.sendChatMessage("Failed to save! " + e.getMessage());
                }
                return;
            }
            case "path": {
                if (args[1] == null) {
                    final Path file = Paths.get(wurstplusMod.getConfigName(), new String[0]);
                    Command.sendChatMessage("Path to configuration: &b" + file.toAbsolutePath().toString());
                    return;
                }
                final String newPath = args[1];
                if (!wurstplusMod.isFilenameValid(newPath)) {
                    Command.sendChatMessage("&b" + newPath + "&r is not a valid path");
                    return;
                }
                try (final BufferedWriter writer = Files.newBufferedWriter(Paths.get("BruceClientLastConfig.txt", new String[0]), new OpenOption[0])) {
                    writer.write(newPath);
                    this.reload();
                    Command.sendChatMessage("Configuration path set to &b" + newPath + "&r!");
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                    Command.sendChatMessage("Couldn't set path: " + e2.getMessage());
                    return;
                }
                break;
            }
        }
        Command.sendChatMessage("Incorrect mode, please choose from: reload, save or path");
    }
    
    private void reload() {
        (wurstplusMod.getInstance().guiManager = new wurstplusGUI()).initializeGUI();
        wurstplusMod.loadConfiguration();
        Command.sendChatMessage("Configuration reloaded!");
    }
}
