package eu.pxldeveloper.fsinfomc.portals;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PortalCreateSession {
    private Player player;
    private Location locationA = null;
    private Location locationB = null;
    private String portalName;

    public PortalCreateSession(Player player) {
        this.player = player;
    }

    public void setLocationA(Location locationA) {
        this.locationA = locationA;
    }

    public void setLocationB(Location locationB) {
        this.locationB = locationB;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public boolean isFinised() {
        return locationA != null && locationB != null;
    }

    public Location getLocationA() {
        return locationA;
    }

    public Location getLocationB() {
        return locationB;
    }

    public String getPortalName() {
        return portalName;
    }

    public Player getPlayer() {
        return this.player;
    }

}
