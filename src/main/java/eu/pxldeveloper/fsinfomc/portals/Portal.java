package eu.pxldeveloper.fsinfomc.portals;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.pxldeveloper.fsinfomc.Cuboid;
import eu.pxldeveloper.fsinfomc.FSInfoMC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Portal extends Cuboid {

    private String name;
    private String message;
    private String serverName;
    boolean enabled;

    public Portal(String name, Location locationA, Location locationB) {
        super(locationA, locationB);
        enabled = false;
        this.name = name;
    }

    public Portal(Map<?, ?> map) {
        super(map);
        this.name = (String) map.get("name");
        this.message = (String) map.get("message");
        this.serverName = (String) map.get("serverName");
        this.enabled = (boolean) map.get("enabled");
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMessage() {
        return message;
    }

    public String getCordsString() {
        return "A: (" + x1 + " " + y1 + " " + z1 + "), B: (" + x2 + " " + y2 + " " + z2 + ")";
    }

    public String getName() {
        return name;
    }

    public String getServerName() { return serverName; }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void jumpBungeeCord(Player player) {
        if(serverName != null) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            player.sendPluginMessage(FSInfoMC.instance, "BungeeCord", out.toByteArray());
            System.out.println("[FSINFOMC] Moving player " + player.getName() + " to server " + serverName);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("worldName", this.worldName);
        map.put("name", this.name);
        map.put("enabled", this.enabled);
        map.put("message", this.message);
        map.put("serverName", this.serverName);
        map.put("x1", this.x1);
        map.put("y1", this.y1);
        map.put("z1", this.z1);
        map.put("x2", this.x2);
        map.put("y2", this.y2);
        map.put("z2", this.z2);
        return map;
    }

}
