package pcl.openprinter.mud;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * MUD is a client side only utility for mods to send automated report for updates and changelog
 */
public class ModUpdateDetector {
    
    private static boolean hasInitialised = false;
    private static Map<String, UpdateEntry> updateMap;
    public static boolean hasChecked = false;
    private static Property check;
    public static boolean enabled = true;
    private static ICommandSender sender = null;

    /**
     * The main registration method for a mod
     * @param mc The FML wrapper for a mod, you can get it with {@link FMLCommonHandler#findContainerFor(Object)}
     * @param updateXML An expected url for an xml file, listing mod versions and download links by Minecraft releases
     * @param changelog An expected url for a file containing text to describe any changes, can be null
     */
    public static void registerMod(ModContainer mc, URL updateXML, URL changelog){
        if(!hasInitialised){
            initialise();
            hasInitialised = true;
        }
        updateMap.put(mc.getModId(), new UpdateEntry(mc, updateXML, changelog));
    }

    /**
     * Helper registration method for a mod
     * @param mc The FML wrapper for a mod, you can get it with {@link FMLCommonHandler#findContainerFor(Object)}
     * @param updateXML String that can be converted as an url for an xml file, listing mod versions and download links by Minecraft releases
     * @param changelog String that can be converted as an url for a file containing text to describe any changes, can be null
     * @throws MalformedURLException If no known protocol is found, or <tt>updateXML</tt> is <tt>null</tt>.
     */
    public static void registerMod(ModContainer mc, String updateXML, String changelog) throws MalformedURLException {
        registerMod(mc, new URL(updateXML), changelog!=null?new URL(changelog):null);
    }

    /**
     * Helper registration method for a mod
     * @param mod A modid or mod instance
     * @param updateXML String that can be converted as an url for an xml file, listing mod versions and download links by Minecraft releases
     * @param changelog String that can be converted as an url for a file containing text to describe any changes, can be null
     * @throws MalformedURLException If no known protocol is found, or <tt>updateXML</tt> is <tt>null</tt>.
     */
    public static void registerMod(Object mod, String updateXML, String changelog) throws MalformedURLException {
        registerMod(FMLCommonHandler.instance().findContainerFor(mod), updateXML, changelog);
    }

    public static void runUpdateChecker(){

        if(enabled){
        	/*
            ICommandSender sender = getSender();
            sender.sendChatToPlayer(ChatMessageComponent.createFromText(
                    EnumChatFormatting.YELLOW + StatCollector.translateToLocal("mud.name") +
                    EnumChatFormatting.WHITE + ": "+StatCollector.translateToLocal("message.checking")
            ));
        	 */
            Thread t = new Thread(new UpdateChecker(updateMap.values()));
            t.start();
        }

    }

    public static Collection<UpdateEntry> getAllUpdateEntries(){
        return updateMap.values();
    }

    private static void initialise() {
        updateMap = new HashMap<String, UpdateEntry>();/*
         * The time between update checks in minutes.
         * A value <=0 will only run the updater when a player joins the world.
         */
        int Timer = 0;
        TickRegistry.registerTickHandler(new ModUpdateDetectorTickHandeler(Timer), Side.CLIENT);
        ClientCommandHandler.instance.registerCommand(new MudCommands());
    }

    public static void toggleState(){
        enabled = !enabled;
        check.set(enabled);
    }

    public static ICommandSender getSender() {
        if(sender == null){
        	sender = Minecraft.getMinecraft().thePlayer;
        }
        return sender;
    }


    public static void notifyUpdateDone(){
        ICommandSender sender = getSender();
        /*
        if(sender != null){
            sender.sendChatToPlayer(ChatMessageComponent.createFromText(
                    EnumChatFormatting.YELLOW + StatCollector.translateToLocal("mud.name") +
                            EnumChatFormatting.WHITE + ": "+StatCollector.translateToLocal("message.check.done")
            ));
        }
        */

        int outOfDateCount = 0;
        int failedCount = 0;
        for(UpdateEntry e : updateMap.values()){
            try {
                if(!e.isUpToDate()){
                    outOfDateCount ++;
                }
            } catch (Exception e1) {
                failedCount++;
            }
        }
        if(outOfDateCount > 0){
            if(sender != null){
                sender.sendChatToPlayer(ChatMessageComponent.createFromText(
                        String.format("%s%s",
                        EnumChatFormatting.RED, StatCollector.translateToLocal("openprinter.outdated"))
                ));
                /*sender.sendChatToPlayer(ChatMessageComponent.createFromText(
                        String.format("%s%s", EnumChatFormatting.RED, StatCollector.translateToLocal("message.type.to.view"))
                ));*/
            }
        }else{
            if(sender != null){
                sender.sendChatToPlayer(ChatMessageComponent.createFromText(
                        String.format("%s%s",
                                EnumChatFormatting.DARK_GREEN, StatCollector.translateToLocal("openprinter.uptodate"))
                ));
            }
        }
        hasChecked = true;
    }
}
