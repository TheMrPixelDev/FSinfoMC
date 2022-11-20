package eu.pxldeveloper.fsinfomc;

import eu.pxldeveloper.fsinfomc.hub.HubCommandExecutor;
import eu.pxldeveloper.fsinfomc.portals.*;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class FSInfoMC extends JavaPlugin implements Listener {

    public static FSInfoMC instance;
    public ArrayList<Portal> portals = new ArrayList<>();
    public ArrayList<PortalCreateSession> sessions = new ArrayList<>();

    public boolean enableLobbyCompass = false;
    public FSInfoMC() {
        instance = this;
    }

    @Override
    public void onEnable() {

        /*Loading portal config*/
        PortalConfigurationSaver ps = new PortalConfigurationSaver(this);
        portals = ps.restore();

        enableLobbyCompass = getConfig().getBoolean("lobby-compass");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                portals.forEach(portal -> {
                    List<Block> portalBlocks = portal.getBlocks();
                    portalBlocks.forEach(block -> {
                        Bukkit.getWorlds().get(0).spawnParticle(Particle.PORTAL, block.getLocation().add(0.5, 0.5, 0.5), 5, 0, 0, 0, 0.5);
                    });
                });
            }
        };

        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, runnable, 0, 10);

        /* Initialize Portal Module */
        PortalListener pl = new PortalListener();
        getServer().getPluginManager().registerEvents(pl, this);

        if(enableLobbyCompass) {
            PortalGUI portalGUI = new PortalGUI();
            getServer().getPluginManager().registerEvents(portalGUI, this);
        }

        Objects.requireNonNull(this.getCommand("portal")).setExecutor(new PortalCommandExecutor());
        Objects.requireNonNull(this.getCommand("hub")).setExecutor(new HubCommandExecutor());

        /*Initialize BungeeCord Module*/
        MessageListener ml = new MessageListener();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", ml);
        System.out.println("[FSINFOMC] Plugin has been enabled.");
    }

    @Override
    public void onDisable() {

        /*Saving portals to config*/
        PortalConfigurationSaver ps = new PortalConfigurationSaver(this);
        if(portals.size() > 0){
            ps.save(portals);
        }

        getConfig().set("lobby-compass", enableLobbyCompass);
        saveConfig();

        /* Deatch Plugin Messaging Channels */
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord");
        System.out.println("[FSINFOMC] Plugin has been disabled.");
    }

    public static FSInfoMC getInstance() {
        return instance;
    }

}
