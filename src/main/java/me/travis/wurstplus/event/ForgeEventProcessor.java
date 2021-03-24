// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.event;

import me.travis.wurstplus.gui.rgui.component.Component;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import me.travis.wurstplus.command.Command;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import me.travis.wurstplus.util.wurstplusTessellator;
import me.travis.wurstplus.gui.UIRenderer;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.travis.wurstplus.gui.rgui.component.container.Container;
import me.travis.wurstplus.module.ModuleManager;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.IInventory;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import me.travis.wurstplus.util.Wrapper;
import net.minecraft.client.gui.ScaledResolution;
import me.travis.wurstplus.command.commands.PeekCommand;
import me.travis.wurstplus.gui.wurstplus.wurstplusGUI;
import me.travis.wurstplus.gui.rgui.component.container.use.Frame;
import me.travis.wurstplus.event.events.DisplaySizeChangedEvent;
import me.travis.wurstplus.wurstplusMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ForgeEventProcessor
{
    private int displayWidth;
    private int displayHeight;
    
    @SubscribeEvent
    public void onUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (Minecraft.getMinecraft().displayWidth != this.displayWidth || Minecraft.getMinecraft().displayHeight != this.displayHeight) {
            wurstplusMod.EVENT_BUS.post(new DisplaySizeChangedEvent());
            this.displayWidth = Minecraft.getMinecraft().displayWidth;
            this.displayHeight = Minecraft.getMinecraft().displayHeight;
            wurstplusMod.getInstance().getGuiManager().getChildren().stream().filter(component -> component instanceof Frame).forEach(component -> wurstplusGUI.dock(component));
        }
        if (PeekCommand.sb != null) {
            final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            final int i = scaledresolution.getScaledWidth();
            final int j = scaledresolution.getScaledHeight();
            final GuiShulkerBox gui = new GuiShulkerBox(Wrapper.getPlayer().inventory, (IInventory)PeekCommand.sb);
            gui.setWorldAndResolution(Wrapper.getMinecraft(), i, j);
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)gui);
            PeekCommand.sb = null;
        }
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (Wrapper.getPlayer() == null) {
            return;
        }
        ModuleManager.onUpdate();
        wurstplusMod.getInstance().getGuiManager().callTick(wurstplusMod.getInstance().getGuiManager());
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        ModuleManager.onWorldRender(event);
    }
    
    @SubscribeEvent
    public void onRenderPre(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && ModuleManager.isModuleEnabled("BossStack")) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.isCanceled()) {
            return;
        }
        RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.EXPERIENCE;
        if (!Wrapper.getPlayer().isCreative() && Wrapper.getPlayer().getRidingEntity() instanceof AbstractHorse) {
            target = RenderGameOverlayEvent.ElementType.HEALTHMOUNT;
        }
        if (event.getType() == target) {
            ModuleManager.onRender();
            GL11.glPushMatrix();
            UIRenderer.renderAndUpdateFrames();
            GL11.glPopMatrix();
            wurstplusTessellator.releaseGL();
        }
        else if (event.getType() != RenderGameOverlayEvent.ElementType.BOSSINFO || ModuleManager.isModuleEnabled("BossStack")) {}
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            ModuleManager.onBind(Keyboard.getEventKey());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public ClientChatEvent onChatSent(final ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                Wrapper.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                if (event.getMessage().length() > 1) {
                    wurstplusMod.getInstance().commandManager.callCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                }
                else {
                    Command.sendChatMessage("Please enter a command.");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Command.sendChatMessage("Error occured while running command! (" + e.getMessage() + ")");
            }
            event.setMessage("");
        }
        return event;
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public RenderPlayerEvent.Pre onPlayerDrawn(final RenderPlayerEvent.Pre event) {
        wurstplusMod.EVENT_BUS.post(event);
        return event;
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public RenderPlayerEvent.Post onPlayerDrawn(final RenderPlayerEvent.Post event) {
        wurstplusMod.EVENT_BUS.post(event);
        return event;
    }
    
    @SubscribeEvent
    public void onChunkLoaded(final ChunkEvent.Load event) {
        wurstplusMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public ChunkEvent.Unload onChunkLoaded(final ChunkEvent.Unload event) {
        wurstplusMod.EVENT_BUS.post(event);
        return event;
    }
    
    @SubscribeEvent
    public InputUpdateEvent onInputUpdate(final InputUpdateEvent event) {
        wurstplusMod.EVENT_BUS.post(event);
        return event;
    }
    
    @SubscribeEvent
    public LivingEntityUseItemEvent.Start onLivingEntityUseItemEventTick(final LivingEntityUseItemEvent.Start entityUseItemEvent) {
        wurstplusMod.EVENT_BUS.post(entityUseItemEvent);
        return entityUseItemEvent;
    }
    
    @SubscribeEvent
    public LivingDamageEvent onLivingDamageEvent(final LivingDamageEvent event) {
        wurstplusMod.EVENT_BUS.post(event);
        return event;
    }
    
    @SubscribeEvent
    public EntityJoinWorldEvent onEntityJoinWorldEvent(final EntityJoinWorldEvent entityJoinWorldEvent) {
        wurstplusMod.EVENT_BUS.post(entityJoinWorldEvent);
        return entityJoinWorldEvent;
    }
    
    @SubscribeEvent
    public PlayerSPPushOutOfBlocksEvent onPlayerPush(final PlayerSPPushOutOfBlocksEvent event) {
        wurstplusMod.EVENT_BUS.post(event);
        return event;
    }
    
    @SubscribeEvent
    public PlayerInteractEvent.LeftClickBlock onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        wurstplusMod.EVENT_BUS.post(event);
        return event;
    }
    
    @SubscribeEvent
    public AttackEntityEvent onAttackEntity(final AttackEntityEvent entityEvent) {
        wurstplusMod.EVENT_BUS.post(entityEvent);
        return entityEvent;
    }
    
    @SubscribeEvent
    public RenderBlockOverlayEvent onRenderBlockOverlay(final RenderBlockOverlayEvent event) {
        wurstplusMod.EVENT_BUS.post(event);
        return event;
    }
    
    @SubscribeEvent
    public LivingDeathEvent kill(final LivingDeathEvent event) {
        return event;
    }
}
