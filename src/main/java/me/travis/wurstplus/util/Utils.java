// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.Color;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.Minecraft;

public class Utils
{
    private static Minecraft mc;
    
    public static String vectorToString(final Vec3d vector, final boolean... includeY) {
        final boolean reallyIncludeY = includeY.length <= 0 || includeY[0];
        final StringBuilder builder = new StringBuilder();
        builder.append('(');
        builder.append((int)Math.floor(vector.x));
        builder.append(", ");
        if (reallyIncludeY) {
            builder.append((int)Math.floor(vector.y));
            builder.append(", ");
        }
        builder.append((int)Math.floor(vector.z));
        builder.append(")");
        return builder.toString();
    }
    
    public static String vectorToString(final BlockPos pos) {
        return vectorToString(new Vec3d((Vec3i)pos), new boolean[0]);
    }
    
    public static void drawRainbowString(final String string, final int x, final int y, final float brightness) {
        int thisX = x;
        for (final char c : string.toCharArray()) {
            final String s = String.valueOf(c);
            thisX += x;
        }
    }
    
    public static Color rainbowColour(final long offset, final float fade) {
        final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
        final Color c = new Color((int)Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16));
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
    
    public static double roundDouble(final double value, final int places) {
        final double scale = Math.pow(10.0, places);
        return Math.round(value * scale) / scale;
    }
    
    public static void copyToClipboard(final String s) {
        final StringSelection selection = new StringSelection(s);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
    
    public static boolean writeFile(final String file, final String contents) {
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(contents);
            writer.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static void vecByMatrix(final Vector4f vec, final Matrix4f matrix) {
        final float x = vec.x;
        final float y = vec.y;
        final float z = vec.z;
        vec.x = x * matrix.m00 + y * matrix.m10 + z * matrix.m20 + matrix.m30;
        vec.y = x * matrix.m01 + y * matrix.m11 + z * matrix.m21 + matrix.m31;
        vec.z = x * matrix.m02 + y * matrix.m12 + z * matrix.m22 + matrix.m32;
        vec.w = x * matrix.m03 + y * matrix.m13 + z * matrix.m23 + matrix.m33;
    }
    
    public static float getPlayerDirection() {
        float var1 = Utils.mc.player.rotationYaw;
        if (Utils.mc.player.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (Utils.mc.player.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (Utils.mc.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Utils.mc.player.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (Utils.mc.player.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        var1 *= 0.017453292f;
        return var1;
    }
    
    static {
        Utils.mc = Minecraft.getMinecraft();
    }
}
