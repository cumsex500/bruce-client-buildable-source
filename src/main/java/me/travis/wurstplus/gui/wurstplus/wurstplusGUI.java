// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.gui.wurstplus;

import java.awt.Font;
import me.travis.wurstplus.gui.rgui.util.Docking;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.LinkedList;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.projectile.EntityWitherSkull;
import javax.annotation.Nonnull;
import java.text.NumberFormat;
import me.travis.wurstplus.gui.wurstplus.component.Radar;
import java.util.function.Consumer;
import net.minecraft.util.text.TextFormatting;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.Collection;
import me.travis.wurstplus.gui.rgui.component.listen.TickListener;
import net.minecraft.entity.EntityLivingBase;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import net.minecraft.entity.Entity;
import me.travis.wurstplus.util.OnlineFriends;
import net.minecraft.client.Minecraft;
import me.travis.wurstplus.command.Command;
import me.travis.wurstplus.util.LagCompensator;
import me.travis.wurstplus.gui.rgui.render.font.FontRenderer;
import me.travis.wurstplus.gui.rgui.component.use.Label;
import me.travis.wurstplus.gui.wurstplus.component.ActiveModules;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.travis.wurstplus.gui.rgui.component.container.Container;
import me.travis.wurstplus.gui.rgui.util.ContainerHelper;
import me.travis.wurstplus.util.Wrapper;
import me.travis.wurstplus.gui.rgui.component.container.use.Frame;
import java.util.Map;
import me.travis.wurstplus.gui.rgui.poof.IPoof;
import me.travis.wurstplus.gui.rgui.poof.PoofInfo;
import me.travis.wurstplus.gui.rgui.component.Component;
import me.travis.wurstplus.gui.rgui.component.use.CheckButton.CheckButtonPoof;
import me.travis.wurstplus.gui.rgui.component.listen.MouseListener;
import me.travis.wurstplus.gui.rgui.component.use.CheckButton;
import me.travis.wurstplus.gui.rgui.layout.Layout;
import me.travis.wurstplus.module.ModuleManager;
import me.travis.wurstplus.gui.wurstplus.component.SettingsPanel;
import me.travis.wurstplus.gui.rgui.component.container.use.Scrollpane;
import me.travis.wurstplus.util.Pair;
import me.travis.wurstplus.module.Module;
import java.util.HashMap;
import me.travis.wurstplus.gui.wurstplus.theme.wurstplus.wurstplusTheme;
import me.travis.wurstplus.util.ColourHolder;
import me.travis.wurstplus.gui.font.CFontRenderer;
import me.travis.wurstplus.gui.rgui.render.theme.Theme;
import me.travis.wurstplus.util.ModuleMan;
import me.travis.wurstplus.gui.rgui.GUI;

public class wurstplusGUI extends GUI
{
    public ModuleMan manager;
    public static final RootFontRenderer fontRenderer;
    public Theme theme;
    public static CFontRenderer cFontRenderer;
    public static ColourHolder primaryColour;
    private static final int DOCK_OFFSET = 0;
    
    public wurstplusGUI() {
        super(new wurstplusTheme());
        this.manager = new ModuleMan();
        this.theme = this.getTheme();
    }
    
    @Override
    public void drawGUI() {
        super.drawGUI();
    }
    
    @Override
    public void initializeGUI() {
        final HashMap<Module.Category, Pair<Scrollpane, SettingsPanel>> categoryScrollpaneHashMap = new HashMap<Module.Category, Pair<Scrollpane, SettingsPanel>>();
        for (final Module module : ModuleManager.getModules()) {
            if (module.getCategory().isHidden()) {
                continue;
            }
            final Module.Category moduleCategory = module.getCategory();
            if (!categoryScrollpaneHashMap.containsKey(moduleCategory)) {
                final Stretcherlayout stretcherlayout = new Stretcherlayout(1);
                stretcherlayout.setComponentOffsetWidth(0);
                final Scrollpane scrollpane = new Scrollpane(this.getTheme(), stretcherlayout, 300, 260);
                scrollpane.setMaximumHeight(180);
                categoryScrollpaneHashMap.put(moduleCategory, new Pair<Scrollpane, SettingsPanel>(scrollpane, new SettingsPanel(this.getTheme(), (Module)null)));
            }
            final Pair<Scrollpane, SettingsPanel> pair = categoryScrollpaneHashMap.get(moduleCategory);
            final Scrollpane scrollpane = pair.getKey();
            final CheckButton checkButton = new CheckButton(module.getName());
            checkButton.setToggled(module.isEnabled());
            final CheckButton checkButton2;
            final Module module2;
            checkButton.addTickListener(() -> {
                checkButton2.setToggled(module2.isEnabled());
                checkButton2.setName(module2.getName());
                return;
            });
            checkButton.addMouseListener(new MouseListener() {
                @Override
                public void onMouseDown(final MouseButtonEvent event) {
                    if (event.getButton() == 1) {
                        pair.getValue().setModule(module);
                        pair.getValue().setX(event.getX() + checkButton.getX());
                        pair.getValue().setY(event.getY() + checkButton.getY());
                    }
                }
                
                @Override
                public void onMouseRelease(final MouseButtonEvent event) {
                }
                
                @Override
                public void onMouseDrag(final MouseButtonEvent event) {
                }
                
                @Override
                public void onMouseMove(final MouseMoveEvent event) {
                }
                
                @Override
                public void onScroll(final MouseScrollEvent event) {
                }
            });
            checkButton.addPoof(new CheckButton.CheckButtonPoof<CheckButton, CheckButton.CheckButtonPoof.CheckButtonPoofInfo>() {
                @Override
                public void execute(final CheckButton component, final CheckButtonPoofInfo info) {
                    if (info.getAction().equals(CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE)) {
                        module.setEnabled(checkButton.isToggled());
                    }
                }
            });
            scrollpane.addChild(checkButton);
        }
        int x = 10;
        int nexty;
        int y = nexty = 10;
        for (final Map.Entry<Module.Category, Pair<Scrollpane, SettingsPanel>> entry : categoryScrollpaneHashMap.entrySet()) {
            final Stretcherlayout stretcherlayout2 = new Stretcherlayout(1);
            stretcherlayout2.COMPONENT_OFFSET_Y = 1;
            final Frame frame = new Frame(this.getTheme(), stretcherlayout2, entry.getKey().getName());
            final Scrollpane scrollpane2 = entry.getValue().getKey();
            frame.addChild(scrollpane2);
            frame.addChild(entry.getValue().getValue());
            scrollpane2.setOriginOffsetY(0);
            scrollpane2.setOriginOffsetX(0);
            frame.setCloseable(false);
            frame.setX(x);
            frame.setY(y);
            this.addChild(frame);
            nexty = Math.max(y + frame.getHeight() + 50, nexty);
            x += frame.getWidth() + 10;
            if (x > Wrapper.getMinecraft().displayWidth / 1.2f) {
                y = (nexty = nexty);
            }
        }
        this.addMouseListener(new MouseListener() {
            private boolean isBetween(final int min, final int val, final int max) {
                return val <= max && val >= min;
            }
            
            @Override
            public void onMouseDown(final MouseButtonEvent event) {
                final List<SettingsPanel> panels = ContainerHelper.getAllChildren((Class<? extends SettingsPanel>)SettingsPanel.class, (Container)wurstplusGUI.this);
                for (final SettingsPanel settingsPanel : panels) {
                    if (!settingsPanel.isVisible()) {
                        continue;
                    }
                    final int[] real = GUI.calculateRealPosition(settingsPanel);
                    final int pX = event.getX() - real[0];
                    final int pY = event.getY() - real[1];
                    if (this.isBetween(0, pX, settingsPanel.getWidth()) && this.isBetween(0, pY, settingsPanel.getHeight())) {
                        continue;
                    }
                    settingsPanel.setVisible(false);
                }
            }
            
            @Override
            public void onMouseRelease(final MouseButtonEvent event) {
            }
            
            @Override
            public void onMouseDrag(final MouseButtonEvent event) {
            }
            
            @Override
            public void onMouseMove(final MouseMoveEvent event) {
            }
            
            @Override
            public void onScroll(final MouseScrollEvent event) {
            }
        });
        final ArrayList<Frame> frames = new ArrayList<Frame>();
        Frame frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Active Goods");
        frame2.setCloseable(false);
        frame2.addChild(new ActiveModules());
        frame2.setPinneable(true);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Welcomer");
        frame2.setCloseable(false);
        frame2.setPinneable(true);
        final Label welcomer = new Label("");
        welcomer.setShadow(true);
        final Label label;
        welcomer.addTickListener(() -> {
            label.setText("");
            label.addLine("§l§6" + Wrapper.getPlayer().getDisplayNameString() + "§r Bruce WIllis thinks you're looking sexy today!. §k /");
            return;
        });
        frame2.addChild(welcomer);
        welcomer.setFontRenderer(wurstplusGUI.fontRenderer);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Info");
        frame2.setCloseable(false);
        frame2.setPinneable(true);
        final Label information = new Label("");
        information.setShadow(true);
        final Label label2;
        final StringBuilder sb;
        information.addTickListener(() -> {
            label2.setText("");
            label2.addLine("§6§l Bruce Client");
            label2.addLine("§r§3" + Math.round(LagCompensator.INSTANCE.getTickRate()) + Command.SECTIONSIGN() + "3 tps");
            new StringBuilder().append("§r§3");
            Wrapper.getMinecraft();
            label2.addLine(sb.append(Minecraft.debugFPS).append(Command.SECTIONSIGN()).append("3 fps").toString());
            label2.addLine("Fun Fact: Bruce Willis loves you.");
            return;
        });
        frame2.addChild(information);
        information.setFontRenderer(wurstplusGUI.fontRenderer);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Stats");
        frame2.setCloseable(false);
        frame2.setPinneable(true);
        final Label goodsLabel = new Label("");
        goodsLabel.setShadow(true);
        final Label label3;
        goodsLabel.addTickListener(() -> {
            label3.setText("");
            label3.addLine("§6 Totems:" + this.manager.getTotems());
            label3.addLine("§6 Hole:" + this.manager.getHoleType());
            label3.addLine("§6 Aura:" + this.manager.isAura());
            label3.addLine("§6 Trap:" + this.manager.isTrap());
            label3.addLine("§6 Surround:" + this.manager.isSurround());
            label3.addLine("§6 HoleFill:" + this.manager.isFill());
            return;
        });
        frame2.addChild(goodsLabel);
        goodsLabel.setFontRenderer(wurstplusGUI.fontRenderer);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Friends");
        frame2.setCloseable(false);
        frame2.setPinneable(true);
        final Label friendLabel = new Label("");
        friendLabel.setShadow(true);
        final Label label4;
        final Iterator<Entity> iterator3;
        Entity e;
        friendLabel.addTickListener(() -> {
            label4.setText("");
            if (OnlineFriends.getFriends().isEmpty()) {
                label4.addLine("");
            }
            else {
                label4.addLine("§3§l The Boys");
                OnlineFriends.getFriends().iterator();
                while (iterator3.hasNext()) {
                    e = iterator3.next();
                    label4.addLine("§6 " + e.getName());
                }
            }
            return;
        });
        frame2.addChild(friendLabel);
        friendLabel.setFontRenderer(wurstplusGUI.fontRenderer);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Text Radar");
        final Label list = new Label("");
        final DecimalFormat dfHealth = new DecimalFormat("#.#");
        dfHealth.setRoundingMode(RoundingMode.HALF_UP);
        final StringBuilder healthSB = new StringBuilder();
        final Label label5;
        Minecraft mc;
        List<EntityPlayer> entityList;
        Map<String, Integer> players;
        final Iterator<EntityPlayer> iterator4;
        Entity e2;
        String s;
        String posString;
        float hpRaw;
        final NumberFormat numberFormat;
        String hp;
        final StringBuilder sb2;
        Map<String, Integer> players2;
        final Iterator<Map.Entry<String, Integer>> iterator5;
        Map.Entry<String, Integer> player;
        list.addTickListener(() -> {
            if (!label5.isVisible()) {
                return;
            }
            else {
                label5.setText("");
                mc = Wrapper.getMinecraft();
                if (mc.player == null) {
                    return;
                }
                else {
                    entityList = (List<EntityPlayer>)mc.world.playerEntities;
                    players = new HashMap<String, Integer>();
                    entityList.iterator();
                    while (iterator4.hasNext()) {
                        e2 = (Entity)iterator4.next();
                        if (e2.getName().equals(mc.player.getName())) {
                            continue;
                        }
                        else {
                            if (e2.posY > mc.player.posY) {
                                s = ChatFormatting.DARK_GREEN + "+";
                            }
                            else if (e2.posY == mc.player.posY) {
                                s = " ";
                            }
                            else {
                                s = ChatFormatting.DARK_RED + "-";
                            }
                            posString = s;
                            hpRaw = ((EntityLivingBase)e2).getHealth() + ((EntityLivingBase)e2).getAbsorptionAmount();
                            hp = numberFormat.format(hpRaw);
                            sb2.append(Command.SECTIONSIGN());
                            if (hpRaw >= 20.0f) {
                                sb2.append("a");
                            }
                            else if (hpRaw >= 10.0f) {
                                sb2.append("e");
                            }
                            else if (hpRaw >= 5.0f) {
                                sb2.append("6");
                            }
                            else {
                                sb2.append("c");
                            }
                            sb2.append(hp);
                            players.put(ChatFormatting.GRAY + posString + " " + sb2.toString() + " " + ChatFormatting.GRAY + e2.getName(), (int)mc.player.getDistance(e2));
                            sb2.setLength(0);
                        }
                    }
                    if (players.isEmpty()) {
                        label5.setText("");
                        return;
                    }
                    else {
                        players2 = sortByValue(players);
                        players2.entrySet().iterator();
                        while (iterator5.hasNext()) {
                            player = iterator5.next();
                            label5.addLine(Command.SECTIONSIGN() + "7" + player.getKey() + " " + Command.SECTIONSIGN() + "8" + player.getValue());
                        }
                        return;
                    }
                }
            }
        });
        frame2.setCloseable(false);
        frame2.setPinneable(true);
        frame2.setMinimumWidth(75);
        list.setShadow(true);
        frame2.addChild(list);
        list.setFontRenderer(wurstplusGUI.fontRenderer);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Entities");
        final Label entityLabel = new Label("");
        frame2.setCloseable(false);
        entityLabel.addTickListener(new TickListener() {
            Minecraft mc = Wrapper.getMinecraft();
            
            @Override
            public void onTick() {
                if (this.mc.player == null || !entityLabel.isVisible()) {
                    return;
                }
                final List<Entity> entityList = new ArrayList<Entity>(this.mc.world.loadedEntityList);
                if (entityList.size() <= 1) {
                    entityLabel.setText("");
                    return;
                }
                final Map<String, Integer> entityCounts = entityList.stream().filter(Objects::nonNull).filter(e -> !(e instanceof EntityPlayer)).collect(Collectors.groupingBy(x$0 -> getEntityName(x$0), (Collector<? super Object, ?, Integer>)Collectors.reducing((D)0, ent -> {
                    if (ent instanceof EntityItem) {
                        return Integer.valueOf(ent.getItem().getCount());
                    }
                    else {
                        return Integer.valueOf(1);
                    }
                }, Integer::sum)));
                entityLabel.setText("");
                entityCounts.entrySet().stream().sorted((Comparator<? super Object>)Map.Entry.comparingByValue()).map(entry -> TextFormatting.GRAY + entry.getKey() + " " + TextFormatting.DARK_GRAY + "x" + entry.getValue()).forEach((Consumer<? super Object>)entityLabel::addLine);
            }
        });
        frame2.addChild(entityLabel);
        frame2.setPinneable(true);
        entityLabel.setShadow(true);
        entityLabel.setFontRenderer(wurstplusGUI.fontRenderer);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Coordinates");
        frame2.setCloseable(false);
        frame2.setPinneable(true);
        final Label coordsLabel = new Label("");
        coordsLabel.addTickListener(new TickListener() {
            Minecraft mc = Minecraft.getMinecraft();
            
            @Override
            public void onTick() {
                final boolean inHell = this.mc.world.getBiome(this.mc.player.getPosition()).getBiomeName().equals("Hell");
                final int posX = (int)this.mc.player.posX;
                final int posY = (int)this.mc.player.posY;
                final int posZ = (int)this.mc.player.posZ;
                final float f = inHell ? 8.0f : 0.125f;
                final int hposX = (int)(this.mc.player.posX * f);
                final int hposZ = (int)(this.mc.player.posZ * f);
                coordsLabel.setText(String.format(" %sf%,d%s7, %sf%,d%s7, %sf%,d %s7(%sf%,d%s7, %sf%,d%s7, %sf%,d%s7)", Command.SECTIONSIGN(), posX, Command.SECTIONSIGN(), Command.SECTIONSIGN(), posY, Command.SECTIONSIGN(), Command.SECTIONSIGN(), posZ, Command.SECTIONSIGN(), Command.SECTIONSIGN(), hposX, Command.SECTIONSIGN(), Command.SECTIONSIGN(), posY, Command.SECTIONSIGN(), Command.SECTIONSIGN(), hposZ, Command.SECTIONSIGN()));
            }
        });
        frame2.addChild(coordsLabel);
        coordsLabel.setFontRenderer(wurstplusGUI.fontRenderer);
        coordsLabel.setShadow(true);
        frame2.setHeight(20);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Radar");
        frame2.setCloseable(false);
        frame2.setMinimizeable(true);
        frame2.setPinneable(true);
        frame2.addChild(new Radar());
        frame2.setWidth(100);
        frame2.setHeight(100);
        for (final Frame frame3 : frames) {
            frame3.setX(x);
            frame3.setY(y);
            nexty = Math.max(y + frame3.getHeight() + 10, nexty);
            x += frame3.getWidth() + 10;
            if (x * DisplayGuiScreen.getScale() > Wrapper.getMinecraft().displayWidth / 1.2f) {
                y = (nexty = nexty);
                x = 10;
            }
            this.addChild(frame3);
        }
    }
    
    private static String getEntityName(@Nonnull final Entity entity) {
        if (entity instanceof EntityItem) {
            return TextFormatting.DARK_AQUA + ((EntityItem)entity).getItem().getItem().getItemStackDisplayName(((EntityItem)entity).getItem());
        }
        if (entity instanceof EntityWitherSkull) {
            return TextFormatting.DARK_GRAY + "Wither skull";
        }
        if (entity instanceof EntityEnderCrystal) {
            return TextFormatting.LIGHT_PURPLE + "End crystal";
        }
        if (entity instanceof EntityEnderPearl) {
            return "Thrown ender pearl";
        }
        if (entity instanceof EntityMinecart) {
            return "Minecart";
        }
        if (entity instanceof EntityItemFrame) {
            return "Item frame";
        }
        if (entity instanceof EntityEgg) {
            return "Thrown egg";
        }
        if (entity instanceof EntitySnowball) {
            return "Thrown snowball";
        }
        return entity.getName();
    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
        final List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, Comparator.comparing(o -> o.getValue()));
        final Map<K, V> result = new LinkedHashMap<K, V>();
        for (final Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    @Override
    public void destroyGUI() {
        this.kill();
    }
    
    public static void dock(final Frame component) {
        final Docking docking = component.getDocking();
        if (docking.isTop()) {
            component.setY(0);
        }
        if (docking.isBottom()) {
            component.setY(Wrapper.getMinecraft().displayHeight / DisplayGuiScreen.getScale() - component.getHeight() - 0);
        }
        if (docking.isLeft()) {
            component.setX(0);
        }
        if (docking.isRight()) {
            component.setX(Wrapper.getMinecraft().displayWidth / DisplayGuiScreen.getScale() - component.getWidth() - 0);
        }
        if (docking.isCenterHorizontal()) {
            component.setX(Wrapper.getMinecraft().displayWidth / (DisplayGuiScreen.getScale() * 2) - component.getWidth() / 2);
        }
        if (docking.isCenterVertical()) {
            component.setY(Wrapper.getMinecraft().displayHeight / (DisplayGuiScreen.getScale() * 2) - component.getHeight() / 2);
        }
    }
    
    static {
        fontRenderer = new RootFontRenderer(1.0f);
        wurstplusGUI.primaryColour = new ColourHolder(29, 29, 29);
        wurstplusGUI.cFontRenderer = new CFontRenderer(new Font("comic sans", 0, 18), true, false);
    }
}
