// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus;

import me.zero.alpine.EventManager;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import com.google.gson.JsonPrimitive;
import me.travis.wurstplus.gui.rgui.component.Component;
import java.util.Optional;
import java.util.Iterator;
import me.travis.wurstplus.gui.rgui.component.container.Container;
import me.travis.wurstplus.gui.rgui.util.ContainerHelper;
import me.travis.wurstplus.gui.rgui.component.AlignedComponent;
import me.travis.wurstplus.gui.rgui.util.Docking;
import me.travis.wurstplus.gui.rgui.component.container.use.Frame;
import com.google.gson.JsonElement;
import java.util.Map;
import me.travis.wurstplus.setting.config.Configuration;
import java.nio.file.LinkOption;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.NoSuchFileException;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import me.travis.wurstplus.module.Module;
import me.travis.wurstplus.setting.SettingsRegister;
import me.travis.wurstplus.command.Command;
import me.travis.wurstplus.util.Friends;
import me.travis.wurstplus.util.Wrapper;
import me.travis.wurstplus.util.LagCompensator;
import me.travis.wurstplus.module.modules.misc.MiddleClickFriend;
import me.travis.wurstplus.module.modules.render.AntiDeathScreen;
import me.travis.wurstplus.module.modules.chat.Announcer;
import me.travis.wurstplus.event.ForgeEventProcessor;
import net.minecraftforge.common.MinecraftForge;
import java.util.function.Consumer;
import me.travis.wurstplus.module.ModuleManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import me.travis.wurstplus.setting.Settings;
import com.google.common.base.Converter;
import com.google.gson.JsonObject;
import me.travis.wurstplus.setting.Setting;
import me.travis.wurstplus.util.CapeManager;
import me.travis.wurstplus.command.CommandManager;
import me.travis.wurstplus.gui.wurstplus.wurstplusGUI;
import me.zero.alpine.EventBus;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "bruceclient", name = "bruceclient", version = "r.73b")
public class wurstplusMod
{
    public static final String MODID = "bruceclient";
    public static final String MODNAME = "bruceclient";
    public static final String MODVER = "r.73b";
    public static final String wurstplus_HIRAGANA = "bruceclient";
    public static final String wurstplus_KATAKANA = "bruceclient";
    public static final String wurstplus_KANJI = "bruceclient";
    public static final String NAME_UNICODE = "Bruce Client";
    public static String lastChat;
    private static final String wurstplus_CONFIG_NAME_DEFAULT = "WurstPlusConfig.json";
    public static final Logger log;
    public static final EventBus EVENT_BUS;
    @Mod.Instance
    private static wurstplusMod INSTANCE;
    public wurstplusGUI guiManager;
    public CommandManager commandManager;
    public CapeManager capeManager;
    private Setting<JsonObject> guiStateSetting;
    
    public wurstplusMod() {
        this.guiStateSetting = Settings.custom("gui", new JsonObject(), new Converter<JsonObject, JsonObject>() {
            protected JsonObject doForward(final JsonObject jsonObject) {
                return jsonObject;
            }
            
            protected JsonObject doBackward(final JsonObject jsonObject) {
                return jsonObject;
            }
        }).buildAndRegister("");
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        FMLLog.log.info("Loading bruceclient...");
        DiscordPresence.start();
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        wurstplusMod.log.info("\n\nInitializing bruceclient r.73b");
        ModuleManager.initialize();
        ModuleManager.getModules().stream().filter(module -> module.alwaysListening).forEach(wurstplusMod.EVENT_BUS::subscribe);
        MinecraftForge.EVENT_BUS.register((Object)new ForgeEventProcessor());
        MinecraftForge.EVENT_BUS.register((Object)new Announcer());
        MinecraftForge.EVENT_BUS.register((Object)new AntiDeathScreen());
        MinecraftForge.EVENT_BUS.register((Object)new MiddleClickFriend());
        LagCompensator.INSTANCE = new LagCompensator();
        Wrapper.init();
        (this.guiManager = new wurstplusGUI()).initializeGUI();
        this.commandManager = new CommandManager();
        (this.capeManager = new CapeManager()).initializeCapes();
        Friends.initFriends();
        SettingsRegister.register("commandPrefix", Command.commandPrefix);
        loadConfiguration();
        wurstplusMod.log.info("Settings loaded");
        ModuleManager.updateLookup();
        ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::enable);
        wurstplusMod.log.info("BruceClient initialized!\n");
    }
    
    @SubscribeEvent
    public void onChatRecieved(final ClientChatReceivedEvent event) {
        wurstplusMod.lastChat = event.getMessage().getUnformattedText();
    }
    
    public static String getConfigName() {
        final Path config = Paths.get("WurstPlusLastConfig.txt", new String[0]);
        String WurstPlusConfigName = "WurstPlusConfig.json";
        try (final BufferedReader reader = Files.newBufferedReader(config)) {
            WurstPlusConfigName = reader.readLine();
            if (!isFilenameValid(WurstPlusConfigName)) {
                WurstPlusConfigName = "WurstPlusConfig.json";
            }
        }
        catch (NoSuchFileException e3) {
            try (final BufferedWriter writer = Files.newBufferedWriter(config, new OpenOption[0])) {
                writer.write("WurstPlusConfig.json");
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return WurstPlusConfigName;
    }
    
    public static void loadConfiguration() {
        try {
            loadConfigurationUnsafe();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void loadConfigurationUnsafe() throws IOException {
        final String WurstPlusConfigName = getConfigName();
        final Path WurstPlusConfig = Paths.get(WurstPlusConfigName, new String[0]);
        if (!Files.exists(WurstPlusConfig, new LinkOption[0])) {
            return;
        }
        Configuration.loadConfiguration(WurstPlusConfig);
        final JsonObject gui = wurstplusMod.INSTANCE.guiStateSetting.getValue();
        for (final Map.Entry<String, JsonElement> entry : gui.entrySet()) {
            final Optional<Component> optional = wurstplusMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).filter(component -> ((Frame) component).getTitle().equals(entry.getKey())).findFirst();
            if (optional.isPresent()) {
                final JsonObject object = entry.getValue().getAsJsonObject();
                final Frame frame = (Frame) optional.get();
                frame.setX(object.get("x").getAsInt());
                frame.setY(object.get("y").getAsInt());
                final Docking docking = Docking.values()[object.get("docking").getAsInt()];
                if (docking.isLeft()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.LEFT);
                }
                else if (docking.isRight()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.RIGHT);
                }
                else if (docking.isCenterVertical()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.CENTER);
                }
                frame.setDocking(docking);
                frame.setMinimized(object.get("minimized").getAsBoolean());
                frame.setPinned(object.get("pinned").getAsBoolean());
            }
            else {
                System.err.println("Found GUI config entry for " + entry.getKey() + ", but found no frame with that name");
            }
        }
        getInstance().getGuiManager().getChildren().stream().filter(component -> component instanceof Frame && ((Frame) component).isPinneable() && component.isVisible()).forEach(component -> component.setOpacity(0.0f));
    }
    
    public static void saveConfiguration() {
        try {
            saveConfigurationUnsafe();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveConfigurationUnsafe() throws IOException {
        final JsonObject object = new JsonObject();
        final JsonObject[] frameObject = new JsonObject[1];
        final JsonObject jsonObject = new JsonObject();
        wurstplusMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).map(component -> component).forEach(frame -> {
            frameObject[0] = new JsonObject();
            frameObject[0].add("x", (JsonElement)new JsonPrimitive((Number)frame.getX()));
            frameObject[0].add("y", (JsonElement)new JsonPrimitive((Number)frame.getY()));
            frameObject[0].add("docking", (JsonElement)new JsonPrimitive((Number)Arrays.asList(Docking.values()).indexOf(((Frame) frame).getDocking())));
            frameObject[0].add("minimized", (JsonElement)new JsonPrimitive(Boolean.valueOf(((Frame) frame).isMinimized())));
            frameObject[0].add("pinned", (JsonElement)new JsonPrimitive(Boolean.valueOf(((Frame) frame).isPinned())));
            jsonObject.add(((Frame) frame).getTitle(), (JsonElement) frameObject[0]);
            return;
        });
        wurstplusMod.INSTANCE.guiStateSetting.setValue(object);
        final Path outputFile = Paths.get(getConfigName(), new String[0]);
        if (!Files.exists(outputFile, new LinkOption[0])) {
            Files.createFile(outputFile, (FileAttribute<?>[])new FileAttribute[0]);
        }
        Configuration.saveConfiguration(outputFile);
        ModuleManager.getModules().forEach(Module::destroy);
    }
    
    public static boolean isFilenameValid(final String file) {
        final File f = new File(file);
        try {
            f.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    
    public static wurstplusMod getInstance() {
        return wurstplusMod.INSTANCE;
    }
    
    public wurstplusGUI getGuiManager() {
        return this.guiManager;
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    static {
        log = LogManager.getLogger("WurstPlus");
        EVENT_BUS = new EventManager();
        wurstplusMod.lastChat = "";
    }
}
