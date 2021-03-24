// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.module.modules.chat;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.ibm.icu.math.BigDecimal;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import java.util.Iterator;
import java.math.RoundingMode;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketUseBed;
import java.util.function.Predicate;
import net.minecraft.client.gui.GuiGameOver;
import me.travis.wurstplus.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.travis.wurstplus.setting.Setting;
import net.minecraft.util.math.Vec3d;
import me.travis.wurstplus.event.events.GuiScreenEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import me.travis.wurstplus.event.events.PacketEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Queue;
import me.travis.wurstplus.module.Module;

@Info(name = "Announcer", category = Category.CHAT)
public class Announcer extends Module
{
    private static boolean isFirstRun;
    private static Queue<String> messageQueue;
    private static Map<String, Integer> minedBlocks;
    private static Map<String, Integer> placedBlocks;
    private static Map<String, Integer> droppedItems;
    private static Map<String, Integer> consumedItems;
    private static DecimalFormat df;
    private static TimerTask timerTask;
    private static Timer timer;
    private static PacketEvent.Receive lastEventReceive;
    private static PacketEvent.Send lastEventSend;
    private static LivingEntityUseItemEvent.Finish lastLivingEntityUseFinishEvent;
    private static GuiScreenEvent.Displayed lastGuiScreenDisplayedEvent;
    private static String lastmessage;
    private static Vec3d thisTickPos;
    private static Vec3d lastTickPos;
    private static double distanceTraveled;
    private static float thisTickHealth;
    private static float lastTickHealth;
    private static float gainedHealth;
    private static float lostHealth;
    private Setting<Boolean> distance;
    private Setting<Integer> mindistance;
    private Setting<Integer> maxdistance;
    private Setting<Boolean> blocks;
    private Setting<Boolean> items;
    private Setting<Boolean> playerheal;
    private Setting<Boolean> playerdamage;
    private Setting<Boolean> playerdeath;
    private Setting<Boolean> greentext;
    private Setting<Integer> delay;
    private Setting<Integer> queuesize;
    private Setting<Boolean> clearqueue;
    private Setting<DistanceSetting> distanceSet;
    private Setting<ChatSetting> mode;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> guiScreenEventDisplayedlistener;
    @EventHandler
    private Listener<PacketEvent.Receive> packetEventReceiveListener;
    @EventHandler
    private Listener<PacketEvent.Send> packetEventSendListener;
    @EventHandler
    public Listener<LivingEntityUseItemEvent.Finish> listener;
    
    public Announcer() {
        this.distance = this.register(Settings.b("Distance", true));
        this.mindistance = this.register((Setting<Integer>)Settings.integerBuilder("Min Distance").withRange(1, 100).withValue(10).build());
        this.maxdistance = this.register((Setting<Integer>)Settings.integerBuilder("Max Distance").withRange(100, 10000).withValue(150).build());
        this.blocks = this.register(Settings.b("Blocks", true));
        this.items = this.register(Settings.b("Items", true));
        this.playerheal = this.register(Settings.b("Player Heal", true));
        this.playerdamage = this.register(Settings.b("Player Damage", true));
        this.playerdeath = this.register(Settings.b("Death", true));
        this.greentext = this.register(Settings.b("Greentext", false));
        this.delay = this.register((Setting<Integer>)Settings.integerBuilder("Send Delay").withRange(1, 20).withValue(2).build());
        this.queuesize = this.register((Setting<Integer>)Settings.integerBuilder("Queue Size").withRange(1, 100).withValue(5).build());
        this.clearqueue = this.register(Settings.b("Clear Queue", false));
        this.distanceSet = this.register(Settings.e("Unit of distance", DistanceSetting.Feet));
        this.mode = this.register(Settings.e("Mode", ChatSetting.Lata));
        this.guiScreenEventDisplayedlistener = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (this.isDisabled() || Announcer.mc.player == null) {
                return;
            }
            else if (Announcer.lastGuiScreenDisplayedEvent != null && Announcer.lastGuiScreenDisplayedEvent.equals(event)) {
                return;
            }
            else if (this.playerdeath.getValue() && event.getScreen() instanceof GuiGameOver) {
                this.queueMessage("I appear to be not living");
                return;
            }
            else {
                Announcer.lastGuiScreenDisplayedEvent = event;
                return;
            }
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
        this.packetEventReceiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (this.isDisabled() || Announcer.mc.player == null) {
                return;
            }
            else if (Announcer.lastEventReceive != null && Announcer.lastEventReceive.equals(event)) {
                return;
            }
            else if (event.getPacket() instanceof SPacketUseBed) {
                this.queueMessage("Sleepy Time");
                Announcer.lastEventReceive = event;
                return;
            }
            else {
                return;
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        CPacketPlayerDigging p;
        String name;
        String name2;
        CPacketUpdateSign p2;
        CPacketPlayerTryUseItemOnBlock p3;
        ItemStack itemStack;
        String name3;
        this.packetEventSendListener = new Listener<PacketEvent.Send>(event -> {
            if (this.isDisabled() || Announcer.mc.player == null) {
                return;
            }
            else if (Announcer.lastEventSend != null && Announcer.lastEventSend.equals(event)) {
                return;
            }
            else {
                if ((this.items.getValue() || this.blocks.getValue()) && event.getPacket() instanceof CPacketPlayerDigging) {
                    p = (CPacketPlayerDigging)event.getPacket();
                    if (this.items.getValue() && Announcer.mc.player.getHeldItemMainhand().getItem() != Items.AIR && (p.getAction().equals((Object)CPacketPlayerDigging.Action.DROP_ITEM) || p.getAction().equals((Object)CPacketPlayerDigging.Action.DROP_ALL_ITEMS))) {
                        name = Announcer.mc.player.inventory.getCurrentItem().getDisplayName();
                        if (Announcer.droppedItems.containsKey(name)) {
                            Announcer.droppedItems.put(name, Announcer.droppedItems.get(name) + 1);
                        }
                        else {
                            Announcer.droppedItems.put(name, 1);
                        }
                        Announcer.lastEventSend = event;
                    }
                    else if (this.blocks.getValue() && p.getAction().equals((Object)CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                        name2 = Announcer.mc.world.getBlockState(p.getPosition()).getBlock().getLocalizedName();
                        if (Announcer.minedBlocks.containsKey(name2)) {
                            Announcer.minedBlocks.put(name2, Announcer.minedBlocks.get(name2) + 1);
                        }
                        else {
                            Announcer.minedBlocks.put(name2, 1);
                        }
                        Announcer.lastEventSend = event;
                    }
                }
                else if (this.items.getValue() && event.getPacket() instanceof CPacketUpdateSign) {
                    p2 = (CPacketUpdateSign)event.getPacket();
                    this.queueMessage("signs xd");
                    Announcer.lastEventSend = event;
                }
                else if (this.blocks.getValue() && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                    p3 = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
                    itemStack = Announcer.mc.player.inventory.getCurrentItem();
                    if (itemStack.isEmpty) {
                        Announcer.lastEventSend = event;
                    }
                    else if (itemStack.getItem() instanceof ItemBlock) {
                        name3 = Announcer.mc.player.inventory.getCurrentItem().getDisplayName();
                        if (Announcer.placedBlocks.containsKey(name3)) {
                            Announcer.placedBlocks.put(name3, Announcer.placedBlocks.get(name3) + 1);
                        }
                        else {
                            Announcer.placedBlocks.put(name3, 1);
                        }
                        Announcer.lastEventSend = event;
                    }
                }
                return;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        String name4;
        this.listener = new Listener<LivingEntityUseItemEvent.Finish>(event -> {
            if (event.getEntity().equals((Object)Announcer.mc.player) && event.getItem().getItem() instanceof ItemFood) {
                name4 = event.getItem().getDisplayName();
                if (Announcer.consumedItems.containsKey(name4)) {
                    Announcer.consumedItems.put(name4, Announcer.consumedItems.get(name4) + 1);
                }
                else {
                    Announcer.consumedItems.put(name4, 1);
                }
                Announcer.lastLivingEntityUseFinishEvent = event;
            }
        }, (Predicate<LivingEntityUseItemEvent.Finish>[])new Predicate[0]);
    }
    
    public void onEnable() {
        Announcer.timer = new Timer();
        if (Announcer.mc.player == null) {
            this.disable();
            return;
        }
        (Announcer.df = new DecimalFormat("#.#")).setRoundingMode(RoundingMode.CEILING);
        Announcer.timerTask = new TimerTask() {
            @Override
            public void run() {
                Announcer.this.sendMessageCycle();
            }
        };
        Announcer.timer.schedule(Announcer.timerTask, 0L, this.delay.getValue() * 1000);
    }
    
    public void onDisable() {
        Announcer.timer.cancel();
        Announcer.timer.purge();
        Announcer.messageQueue.clear();
    }
    
    @Override
    public void onUpdate() {
        if (this.isDisabled() || Announcer.mc.player == null) {
            return;
        }
        if (this.clearqueue.getValue()) {
            this.clearqueue.setValue(false);
            Announcer.messageQueue.clear();
        }
        this.getGameTickData();
    }
    
    private void getGameTickData() {
        if (this.distance.getValue()) {
            Announcer.lastTickPos = Announcer.thisTickPos;
            Announcer.thisTickPos = Announcer.mc.player.getPositionVector();
            Announcer.distanceTraveled += Announcer.thisTickPos.distanceTo(Announcer.lastTickPos);
        }
        if (this.playerheal.getValue() || this.playerdamage.getValue()) {
            Announcer.lastTickHealth = Announcer.thisTickHealth;
            Announcer.thisTickHealth = Announcer.mc.player.getHealth() + Announcer.mc.player.getAbsorptionAmount();
            final float healthDiff = Announcer.thisTickHealth - Announcer.lastTickHealth;
            if (healthDiff < 0.0f) {
                Announcer.lostHealth += healthDiff * -1.0f;
            }
            else {
                Announcer.gainedHealth += healthDiff;
            }
        }
    }
    
    private void composeGameTickData() {
        if (Announcer.isFirstRun) {
            Announcer.isFirstRun = false;
            this.clearTickData();
            return;
        }
        if (this.distance.getValue() && Announcer.distanceTraveled >= 1.0) {
            if (Announcer.distanceTraveled < this.delay.getValue() * this.mindistance.getValue()) {
                return;
            }
            if (Announcer.distanceTraveled > this.delay.getValue() * this.maxdistance.getValue()) {
                Announcer.distanceTraveled = 0.0;
                return;
            }
            final CharSequence sb = new StringBuilder();
            if (this.mode.getValue() == ChatSetting.Lata) {
                ((StringBuilder)sb).append("lol i traveled  ");
            }
            else {
                ((StringBuilder)sb).append("i wanna kms l0l, traveled ");
            }
            if (this.distanceSet.getValue() == DistanceSetting.Feet) {
                ((StringBuilder)sb).append(round(Announcer.distanceTraveled * 3.2808, 2));
                if ((int)Announcer.distanceTraveled == 1.0) {
                    ((StringBuilder)sb).append(" Foot :D");
                }
                else {
                    ((StringBuilder)sb).append(" Feet :D");
                }
            }
            else if (this.distanceSet.getValue() == DistanceSetting.Yards) {
                ((StringBuilder)sb).append(round(Announcer.distanceTraveled * 1.0936, 2));
                if ((int)Announcer.distanceTraveled == 1.0) {
                    ((StringBuilder)sb).append(" Yard :D");
                }
                else {
                    ((StringBuilder)sb).append(" Yards :D");
                }
            }
            else if (this.distanceSet.getValue() == DistanceSetting.Inches) {
                ((StringBuilder)sb).append(round(Announcer.distanceTraveled * 39.37, 2));
                if ((int)Announcer.distanceTraveled == 1.0) {
                    ((StringBuilder)sb).append(" Inch :D");
                }
                else {
                    ((StringBuilder)sb).append(" Inches :D");
                }
            }
            else {
                ((StringBuilder)sb).append(round(Announcer.distanceTraveled, 2));
                if ((int)Announcer.distanceTraveled == 1.0) {
                    ((StringBuilder)sb).append(" Block :D");
                }
                else {
                    ((StringBuilder)sb).append(" Blocks :D");
                }
            }
            this.queueMessage(((StringBuilder)sb).toString());
            Announcer.distanceTraveled = 0.0;
        }
        if (this.playerdamage.getValue() && Announcer.lostHealth != 0.0f) {
            final CharSequence sb = "Bruce Willis just lost " + Announcer.df.format(Announcer.lostHealth) + " bitches D:";
            this.queueMessage((String)sb);
            Announcer.lostHealth = 0.0f;
        }
        if (this.playerheal.getValue() && Announcer.gainedHealth != 0.0f) {
            final CharSequence sb = "Bruce Willis now has " + Announcer.df.format(Announcer.gainedHealth) + " more bitches :D";
            this.queueMessage((String)sb);
            Announcer.gainedHealth = 0.0f;
        }
    }
    
    private void composeEventData() {
        for (final Map.Entry<String, Integer> kv : Announcer.minedBlocks.entrySet()) {
            this.queueMessage("We be mining " + kv.getValue() + " " + kv.getKey() + " bitcoin out here");
            Announcer.minedBlocks.remove(kv.getKey());
        }
        for (final Map.Entry<String, Integer> kv : Announcer.placedBlocks.entrySet()) {
            this.queueMessage("Bruce placed " + kv.getValue() + " " + kv.getKey() + " out here");
            Announcer.placedBlocks.remove(kv.getKey());
        }
        for (final Map.Entry<String, Integer> kv : Announcer.droppedItems.entrySet()) {
            this.queueMessage("I just dropped " + kv.getValue() + " " + kv.getKey() + ", whoops!");
            Announcer.droppedItems.remove(kv.getKey());
        }
        for (final Map.Entry<String, Integer> kv : Announcer.consumedItems.entrySet()) {
            this.queueMessage("I just ate  " + kv.getValue() + " " + kv.getKey() + ", would've wanted chicken nuggets but ok.");
            Announcer.consumedItems.remove(kv.getKey());
        }
    }
    
    public void sendMessageCycle() {
        if (this.isDisabled() || Announcer.mc.player == null) {
            return;
        }
        this.composeGameTickData();
        this.composeEventData();
        final Iterator iterator = Announcer.messageQueue.iterator();
        if (iterator.hasNext()) {
            final String message = iterator.next();
            this.sendMessage(message);
            Announcer.messageQueue.remove(message);
        }
    }
    
    private void sendMessage(final String s) {
        final StringBuilder sb = new StringBuilder();
        if (this.greentext.getValue()) {
            sb.append("> ");
        }
        sb.append(s);
        Announcer.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(sb.toString().replaceAll("§", "")));
    }
    
    private void clearTickData() {
        final Vec3d pos = Announcer.thisTickPos = (Announcer.lastTickPos = Announcer.mc.player.getPositionVector());
        Announcer.distanceTraveled = 0.0;
        final float health = Announcer.thisTickHealth = (Announcer.lastTickHealth = Announcer.mc.player.getHealth() + Announcer.mc.player.getAbsorptionAmount());
        Announcer.lostHealth = 0.0f;
        Announcer.gainedHealth = 0.0f;
    }
    
    private Block getBlock(final BlockPos pos) {
        return Announcer.mc.world.getBlockState(pos).getBlock();
    }
    
    private void queueMessage(final String message) {
        if (Announcer.messageQueue.size() > this.queuesize.getValue()) {
            return;
        }
        Announcer.messageQueue.add(message);
    }
    
    public static double round(final double unrounded, final int precision) {
        final BigDecimal bd = new BigDecimal(unrounded);
        final BigDecimal rounded = bd.setScale(precision, 4);
        return rounded.doubleValue();
    }
    
    static {
        Announcer.isFirstRun = true;
        Announcer.messageQueue = new ConcurrentLinkedQueue<String>();
        Announcer.minedBlocks = new ConcurrentHashMap<String, Integer>();
        Announcer.placedBlocks = new ConcurrentHashMap<String, Integer>();
        Announcer.droppedItems = new ConcurrentHashMap<String, Integer>();
        Announcer.consumedItems = new ConcurrentHashMap<String, Integer>();
        Announcer.df = new DecimalFormat();
        Announcer.lastmessage = "";
    }
    
    public enum ChatSetting
    {
        Lata, 
        aha;
    }
    
    public enum DistanceSetting
    {
        Feet, 
        Meters, 
        Yards, 
        Inches;
    }
}
