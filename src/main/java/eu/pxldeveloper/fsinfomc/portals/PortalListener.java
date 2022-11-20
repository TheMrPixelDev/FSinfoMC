package eu.pxldeveloper.fsinfomc.portals;

import eu.pxldeveloper.fsinfomc.FSInfoMC;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class PortalListener implements Listener {

    private ArrayList<Portal> portals = FSInfoMC.getInstance().portals;
    private ArrayList<PortalCreateSession> sessions = FSInfoMC.getInstance().sessions;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        Player player = e.getPlayer();
        Location playersBlock = e.getTo();
        Location playersPrevBlock = e.getFrom();

        portals.forEach(portal -> {
            assert playersBlock != null;
            if(portal.contains(playersBlock) && !portal.contains(playersPrevBlock) && portal.enabled) {

                    Bukkit.getWorlds().get(0).spawnParticle(Particle.LAVA, playersBlock, 100, 0.5, 1, 0);
                    System.out.println(player.getName() + " entered portal " + portal.getName());
                    if (portal.getMessage() != null) {
                        player.sendMessage(portal.getMessage());
                    }else{
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "Du hast das Portal " + portal.getName() + " betreten."));
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 500, 0);
                    portal.jumpBungeeCord(player);

            }
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR || e.getClickedBlock() == null) {
            return;
        }

        PortalCreateSession session = null;

        for (PortalCreateSession portalCreateSession : sessions) {
            if (portalCreateSession.getPlayer().equals(e.getPlayer())) {
                session = portalCreateSession;
                break;
            }
        }

        if(session == null) { return; }

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Location location = e.getClickedBlock().getLocation();
            session.setLocationA(location);
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "[PORTALS] Punkt A für " + session.getPortalName() + " auf " + ChatColor.GOLD + location.toVector().toString());
        }

        if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Location location = e.getClickedBlock().getLocation();
            session.setLocationB(location);
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "[PORTALS] Punkt B für " + session.getPortalName() + " auf " + ChatColor.GOLD + location.toVector().toString());
        }

        if(session.isFinised()) {
            portals.add(new Portal(session.getPortalName(), session.getLocationA(), session.getLocationB()));
            e.getPlayer().sendMessage(ChatColor.GREEN + "[PORTALS] Du hast das Portal " + session.getPortalName() + " erfolgreich hinzugefügt.");
            sessions.remove(session);
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        System.out.println("[FSINFOMC] Teleporting " + p.getName() + " to the entry point.");
    }

}
