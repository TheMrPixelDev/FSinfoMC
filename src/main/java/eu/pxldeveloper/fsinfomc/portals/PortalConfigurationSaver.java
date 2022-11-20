package eu.pxldeveloper.fsinfomc.portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PortalConfigurationSaver {

    JavaPlugin plugin;

    public PortalConfigurationSaver(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void save(ArrayList<Portal> portals) {
        ArrayList<HashMap<String, Object>> mapList = new ArrayList<>();
        portals.forEach(portal -> mapList.add((HashMap<String, Object>) portal.serialize()));
        plugin.getConfig().set("portals", mapList);
        plugin.saveConfig();
        /*plugin.getConfig().set("portals", portals);
        plugin.saveConfig();*/
    }

    public ArrayList<Portal> restore() {

        /*List<Map<?, ?>> portalsMaps = plugin.getConfig().getMapList("portals");

        if(portalsMaps == null) {
            return new ArrayList<Portal>();
        }

        ArrayList<Portal> portals = new ArrayList<>();
        portalsMaps.forEach(map -> {
            portals.add(new Portal(map));
        });

        return portals;*/

        List<Map<?,?>> restoredPortalMaps = plugin.getConfig().getMapList("portals");

        System.out.println(restoredPortalMaps);
        if (restoredPortalMaps == null || restoredPortalMaps.size() == 0) { return new ArrayList<Portal>(); }

        ArrayList<Portal> restoredPortals = new ArrayList<>();
        restoredPortalMaps.forEach(map -> {

            Location locationA = new Location(Bukkit.getWorld((String) map.get("worldName")), (int) map.get("x1"), (int) map.get("y1"), (int) map.get("z1"));
            Location locationB = new Location(Bukkit.getWorld((String) map.get("worldName")), (int) map.get("x2"), (int) map.get("y2"), (int) map.get("z2"));

            Portal restoredPortal = new Portal((String) map.get("name"), locationA, locationB);
            restoredPortal.setMessage((String) map.get("message"));
            restoredPortal.setServerName((String) map.get("serverName"));
            restoredPortal.setEnabled((boolean) map.get("enabled"));
            restoredPortals.add(restoredPortal);
        });
        return restoredPortals;
    }

}
