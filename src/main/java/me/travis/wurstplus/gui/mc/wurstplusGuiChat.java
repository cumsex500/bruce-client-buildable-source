// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.mc;

import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import me.travis.wurstplus.command.syntax.SyntaxChunk;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import me.travis.wurstplus.wurstplusMod;
import java.util.HashMap;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;
import me.travis.wurstplus.command.Command;
import net.minecraft.client.gui.GuiChat;

public class wurstplusGuiChat extends GuiChat
{
    private String startString;
    private String currentFillinLine;
    private int cursor;
    
    public wurstplusGuiChat(final String startString, final String historybuffer, final int sentHistoryCursor) {
        super(startString);
        this.startString = startString;
        if (!startString.equals(Command.getCommandPrefix())) {
            this.calculateCommand(startString.substring(Command.getCommandPrefix().length()));
        }
        this.historyBuffer = historybuffer;
        this.cursor = sentHistoryCursor;
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.sentHistoryCursor = this.cursor;
        super.keyTyped(typedChar, keyCode);
        this.cursor = this.sentHistoryCursor;
        final String chatLine = this.inputField.getText();
        if (!chatLine.startsWith(Command.getCommandPrefix())) {
            final GuiChat newGUI = new GuiChat(chatLine) {
                int cursor = wurstplusGuiChat.this.cursor;
                
                protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
                    this.sentHistoryCursor = this.cursor;
                    super.keyTyped(typedChar, keyCode);
                    this.cursor = this.sentHistoryCursor;
                }
            };
            newGUI.historyBuffer = this.historyBuffer;
            this.mc.displayGuiScreen((GuiScreen)newGUI);
            return;
        }
        if (chatLine.equals(Command.getCommandPrefix())) {
            this.currentFillinLine = "";
            return;
        }
        this.calculateCommand(chatLine.substring(Command.getCommandPrefix().length()));
    }
    
    protected void calculateCommand(final String line) {
        final String[] args = line.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        final HashMap<String, Command> options = new HashMap<String, Command>();
        if (args.length == 0) {
            return;
        }
        for (final Command c : wurstplusMod.getInstance().getCommandManager().getCommands()) {
            if ((c.getLabel().startsWith(args[0]) && !line.endsWith(" ")) || c.getLabel().equals(args[0])) {
                options.put(c.getLabel(), c);
            }
        }
        if (options.isEmpty()) {
            this.currentFillinLine = "";
            return;
        }
        final TreeMap<String, Command> map = new TreeMap<String, Command>(options);
        final Command alphaCommand = map.firstEntry().getValue();
        this.currentFillinLine = alphaCommand.getLabel().substring(args[0].length());
        if (alphaCommand.getSyntaxChunks() == null || alphaCommand.getSyntaxChunks().length == 0) {
            return;
        }
        if (!line.endsWith(" ")) {
            this.currentFillinLine += " ";
        }
        final SyntaxChunk[] chunks = alphaCommand.getSyntaxChunks();
        boolean cutSpace = false;
        for (int i = 0; i < chunks.length; ++i) {
            if (i + 1 >= args.length - 1) {
                final SyntaxChunk c2 = chunks[i];
                final String result = c2.getChunk(chunks, c2, args, (i + 1 == args.length - 1) ? args[i + 1] : null);
                if (result != "" && (!result.startsWith("<") || !result.endsWith(">")) && (!result.startsWith("[") || !result.endsWith("]"))) {
                    cutSpace = true;
                }
                this.currentFillinLine = this.currentFillinLine + result + ((result == "") ? "" : " ") + "";
            }
        }
        if (cutSpace) {
            this.currentFillinLine = this.currentFillinLine.substring(1);
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        final int x = this.inputField.fontRenderer.getStringWidth(this.inputField.getText() + "") + 4;
        final int y = this.inputField.getEnableBackgroundDrawing() ? (this.inputField.y + (this.inputField.height - 8) / 2) : this.inputField.y;
        this.inputField.fontRenderer.drawStringWithShadow(this.currentFillinLine, (float)x, (float)y, 6710886);
        this.inputField.drawTextBox();
        final ITextComponent itextcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null) {
            this.handleComponentHover(itextcomponent, mouseX, mouseY);
        }
        final boolean a = GL11.glIsEnabled(3042);
        final boolean b = GL11.glIsEnabled(3553);
        GL11.glDisable(3042);
        GL11.glDisable(3553);
        GL11.glColor3f(0.66f, 0.33f, 0.0f);
        GL11.glBegin(1);
        GL11.glVertex2f((float)(this.inputField.x - 2), (float)(this.inputField.y - 2));
        GL11.glVertex2f((float)(this.inputField.x + this.inputField.getWidth() - 2), (float)(this.inputField.y - 2));
        GL11.glVertex2f((float)(this.inputField.x + this.inputField.getWidth() - 2), (float)(this.inputField.y - 2));
        GL11.glVertex2f((float)(this.inputField.x + this.inputField.getWidth() - 2), (float)(this.inputField.y + this.inputField.height - 2));
        GL11.glVertex2f((float)(this.inputField.x + this.inputField.getWidth() - 2), (float)(this.inputField.y + this.inputField.height - 2));
        GL11.glVertex2f((float)(this.inputField.x - 2), (float)(this.inputField.y + this.inputField.height - 2));
        GL11.glVertex2f((float)(this.inputField.x - 2), (float)(this.inputField.y + this.inputField.height - 2));
        GL11.glVertex2f((float)(this.inputField.x - 2), (float)(this.inputField.y - 2));
        GL11.glEnd();
        if (a) {
            GL11.glEnable(3042);
        }
        if (b) {
            GL11.glEnable(3553);
        }
    }
}
