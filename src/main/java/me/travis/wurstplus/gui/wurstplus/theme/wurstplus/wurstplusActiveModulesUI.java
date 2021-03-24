// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus.theme.wurstplus;

import me.travis.wurstplus.util.ColourUtils;
import me.travis.wurstplus.command.Command;
import java.awt.Color;
import me.travis.wurstplus.module.Module;
import java.util.function.Function;
import me.travis.wurstplus.gui.rgui.component.Component;
import me.travis.wurstplus.gui.rgui.render.font.FontRenderer;
import java.awt.Font;
import me.travis.wurstplus.gui.font.CFontRenderer;
import me.travis.wurstplus.gui.wurstplus.component.ActiveModules;
import me.travis.wurstplus.gui.rgui.render.AbstractComponentUI;

public class wurstplusActiveModulesUI extends AbstractComponentUI<ActiveModules>
{
    CFontRenderer cFontRenderer;
    
    public wurstplusActiveModulesUI() {
        this.cFontRenderer = new CFontRenderer(new Font("Comic Sans MS", 0, 18), true, false);
    }
    
    @Override
    public void renderComponent(final ActiveModules component, final FontRenderer f) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: invokestatic    org/lwjgl/opengl/GL11.glDisable:(I)V
        //     6: sipush          3042
        //     9: invokestatic    org/lwjgl/opengl/GL11.glEnable:(I)V
        //    12: sipush          3553
        //    15: invokestatic    org/lwjgl/opengl/GL11.glEnable:(I)V
        //    18: invokestatic    me/travis/wurstplus/util/Wrapper.getFontRenderer:()Lme/travis/wurstplus/gui/rgui/render/font/FontRenderer;
        //    21: astore_3        /* renderer */
        //    22: invokestatic    me/travis/wurstplus/module/ModuleManager.getModules:()Ljava/util/ArrayList;
        //    25: invokevirtual   java/util/ArrayList.stream:()Ljava/util/stream/Stream;
        //    28: invokedynamic   BootstrapMethod #0, test:()Ljava/util/function/Predicate;
        //    33: invokeinterface java/util/stream/Stream.filter:(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;
        //    38: aload_0         /* this */
        //    39: aload_1         /* component */
        //    40: invokedynamic   BootstrapMethod #1, apply:(Lme/travis/wurstplus/gui/wurstplus/theme/wurstplus/wurstplusActiveModulesUI;Lme/travis/wurstplus/gui/wurstplus/component/ActiveModules;)Ljava/util/function/Function;
        //    45: invokestatic    java/util/Comparator.comparing:(Ljava/util/function/Function;)Ljava/util/Comparator;
        //    48: invokeinterface java/util/stream/Stream.sorted:(Ljava/util/Comparator;)Ljava/util/stream/Stream;
        //    53: invokestatic    java/util/stream/Collectors.toList:()Ljava/util/stream/Collector;
        //    56: invokeinterface java/util/stream/Stream.collect:(Ljava/util/stream/Collector;)Ljava/lang/Object;
        //    61: checkcast       Ljava/util/List;
        //    64: astore          mods
        //    66: iconst_1       
        //    67: newarray        I
        //    69: dup            
        //    70: iconst_0       
        //    71: iconst_2       
        //    72: iastore        
        //    73: astore          y
        //    75: aload_1         /* component */
        //    76: invokevirtual   me/travis/wurstplus/gui/wurstplus/component/ActiveModules.getParent:()Lme/travis/wurstplus/gui/rgui/component/container/Container;
        //    79: invokeinterface me/travis/wurstplus/gui/rgui/component/container/Container.getY:()I
        //    84: bipush          26
        //    86: if_icmpge       145
        //    89: invokestatic    me/travis/wurstplus/util/Wrapper.getPlayer:()Lnet/minecraft/client/entity/EntityPlayerSP;
        //    92: invokevirtual   net/minecraft/client/entity/EntityPlayerSP.getActivePotionEffects:()Ljava/util/Collection;
        //    95: invokeinterface java/util/Collection.size:()I
        //   100: ifle            145
        //   103: aload_1         /* component */
        //   104: invokevirtual   me/travis/wurstplus/gui/wurstplus/component/ActiveModules.getParent:()Lme/travis/wurstplus/gui/rgui/component/container/Container;
        //   107: invokeinterface me/travis/wurstplus/gui/rgui/component/container/Container.getOpacity:()F
        //   112: fconst_0       
        //   113: fcmpl          
        //   114: ifne            145
        //   117: aload           y
        //   119: iconst_0       
        //   120: aload_1         /* component */
        //   121: invokevirtual   me/travis/wurstplus/gui/wurstplus/component/ActiveModules.getParent:()Lme/travis/wurstplus/gui/rgui/component/container/Container;
        //   124: invokeinterface me/travis/wurstplus/gui/rgui/component/container/Container.getY:()I
        //   129: bipush          26
        //   131: aload_1         /* component */
        //   132: invokevirtual   me/travis/wurstplus/gui/wurstplus/component/ActiveModules.getParent:()Lme/travis/wurstplus/gui/rgui/component/container/Container;
        //   135: invokeinterface me/travis/wurstplus/gui/rgui/component/container/Container.getY:()I
        //   140: isub           
        //   141: invokestatic    java/lang/Math.max:(II)I
        //   144: iastore        
        //   145: iconst_1       
        //   146: newarray        F
        //   148: dup            
        //   149: iconst_0       
        //   150: invokestatic    java/lang/System.currentTimeMillis:()J
        //   153: ldc2_w          11520
        //   156: lrem           
        //   157: l2f            
        //   158: ldc             11520.0
        //   160: fdiv           
        //   161: fastore        
        //   162: astore          hue
        //   164: aload_1         /* component */
        //   165: invokevirtual   me/travis/wurstplus/gui/wurstplus/component/ActiveModules.getAlignment:()Lme/travis/wurstplus/gui/rgui/component/AlignedComponent$Alignment;
        //   168: getstatic       me/travis/wurstplus/gui/rgui/component/AlignedComponent$Alignment.LEFT:Lme/travis/wurstplus/gui/rgui/component/AlignedComponent$Alignment;
        //   171: if_acmpne       178
        //   174: iconst_1       
        //   175: goto            179
        //   178: iconst_0       
        //   179: istore          lAlign
        //   181: getstatic       me/travis/wurstplus/gui/wurstplus/theme/wurstplus/wurstplusActiveModulesUI$1.$SwitchMap$me$travis$wurstplus$gui$rgui$component$AlignedComponent$Alignment:[I
        //   184: aload_1         /* component */
        //   185: invokevirtual   me/travis/wurstplus/gui/wurstplus/component/ActiveModules.getAlignment:()Lme/travis/wurstplus/gui/rgui/component/AlignedComponent$Alignment;
        //   188: invokevirtual   me/travis/wurstplus/gui/rgui/component/AlignedComponent$Alignment.ordinal:()I
        //   191: iaload         
        //   192: tableswitch {
        //                2: 220
        //                3: 231
        //                4: 242
        //          default: 242
        //        }
        //   220: aload_1         /* component */
        //   221: invokedynamic   BootstrapMethod #2, apply:(Lme/travis/wurstplus/gui/wurstplus/component/ActiveModules;)Ljava/util/function/Function;
        //   226: astore          xFunc
        //   228: goto            249
        //   231: aload_1         /* component */
        //   232: invokedynamic   BootstrapMethod #3, apply:(Lme/travis/wurstplus/gui/wurstplus/component/ActiveModules;)Ljava/util/function/Function;
        //   237: astore          xFunc
        //   239: goto            249
        //   242: invokedynamic   BootstrapMethod #4, apply:()Ljava/util/function/Function;
        //   247: astore          xFunc
        //   249: aload           mods
        //   251: invokeinterface java/util/List.stream:()Ljava/util/stream/Stream;
        //   256: aload_0         /* this */
        //   257: aload           hue
        //   259: aload_3         /* renderer */
        //   260: aload           xFunc
        //   262: aload           y
        //   264: invokedynamic   BootstrapMethod #5, accept:(Lme/travis/wurstplus/gui/wurstplus/theme/wurstplus/wurstplusActiveModulesUI;[FLme/travis/wurstplus/gui/rgui/render/font/FontRenderer;Ljava/util/function/Function;[I)Ljava/util/function/Consumer;
        //   269: invokeinterface java/util/stream/Stream.forEach:(Ljava/util/function/Consumer;)V
        //   274: aload_1         /* component */
        //   275: aload           y
        //   277: iconst_0       
        //   278: iaload         
        //   279: invokevirtual   me/travis/wurstplus/gui/wurstplus/component/ActiveModules.setHeight:(I)V
        //   282: sipush          2884
        //   285: invokestatic    org/lwjgl/opengl/GL11.glEnable:(I)V
        //   288: sipush          3042
        //   291: invokestatic    org/lwjgl/opengl/GL11.glDisable:(I)V
        //   294: return         
        //    StackMapTable: 00 07 FE 00 91 07 00 AB 07 00 84 07 00 AD FC 00 20 07 00 C0 40 01 FC 00 28 01 0A 0A FC 00 06 07 00 DE
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void handleSizeComponent(final ActiveModules component) {
        component.setWidth(100);
        component.setHeight(100);
    }
}
