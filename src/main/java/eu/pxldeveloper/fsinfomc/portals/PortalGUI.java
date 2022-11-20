package eu.pxldeveloper.fsinfomc.portals;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.pxldeveloper.fsinfomc.FSInfoMC;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PortalGUI implements Listener {

    private final Inventory inv;

    public PortalGUI() {
        inv = Bukkit.createInventory(null, 27, "Navigator");
        initializeItems();
    }

    public void initializeItems() {
        for(int i = 0; i<=8; i++){
            inv.setItem(i, createGUIItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " "));
            inv.setItem(i+18, createGUIItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " "));
        }
        inv.setItem(11, createGUIItem(Material.TNT, ChatColor.DARK_PURPLE + "Minigames", "-> Knockback Blast", "-> Skyfighters"));
        inv.setItem(12, createGUIItem(Material.GRASS_BLOCK, ChatColor.GREEN + "Skyblock", "-> MrPixel's Skyblock Server"));
        inv.setItem(14, createGUIItem(Material.DIAMOND_PICKAXE, ChatColor.GOLD + "Survival Multiplayer"));
        inv.setItem(15, createGUIItem(Material.BEACON, ChatColor.BLUE + "Mineopolis"));
    }

    protected ItemStack createGUIItem(final Material material , final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    public void closeInventory(final HumanEntity ent) {
        ent.closeInventory();
    }

    public void jumpBungeeCord(Player player, String server) {
        closeInventory(player);
        Bukkit.getWorlds().get(0).spawnParticle(Particle.FLAME, player.getLocation(), 100, 0, 1, 0, 0.1);
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 100, 0);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(FSInfoMC.instance, "BungeeCord", out.toByteArray());
        System.out.println("[FSINFOMC] Moving player " + player.getName() + " to server " + server);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if(!e.getInventory().equals(inv)) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        final Player p = (Player) e.getWhoClicked();

        switch (clickedItem.getType()) {
            case TNT:
                jumpBungeeCord(p, "pxlworld");
                break;
            case GRASS_BLOCK:
                jumpBungeeCord(p, "skyblock");
                break;
            case DIAMOND_PICKAXE:
                jumpBungeeCord(p, "smp");
                break;
            case BEACON:
                jumpBungeeCord(p, "mineopolis");
                break;
        }

    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if(e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        if(e.getItem() == null) { return; }
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            Material item = e.getItem().getType();
            if(item == Material.COMPASS) {
                openInventory(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        ItemStack navigator = new ItemStack(Material.COMPASS);
        ItemMeta meta = navigator.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Navigator");
        navigator.setItemMeta(meta);
        e.getPlayer().getInventory().setItem(0, navigator);
        e.getPlayer().sendTitle(ChatColor.GOLD + "Willkommen", ChatColor.BLUE + "FSinfo" +ChatColor.GREEN + " Netzwerk",  25, 100, 25);
        e.getPlayer().playSound(e.getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 500.0f, 0.8f);
    }
}
