// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.command.syntax.parsers;

import me.travis.wurstplus.command.syntax.SyntaxChunk;
import me.travis.wurstplus.command.syntax.SyntaxParser;

public abstract class AbstractParser implements SyntaxParser
{
    @Override
    public abstract String getChunk(final SyntaxChunk[] p0, final SyntaxChunk p1, final String[] p2, final String p3);
    
    protected String getDefaultChunk(final SyntaxChunk chunk) {
        return (chunk.isHeadless() ? "" : chunk.getHead()) + (chunk.isNecessary() ? "<" : "[") + chunk.getType() + (chunk.isNecessary() ? ">" : "]");
    }
}
