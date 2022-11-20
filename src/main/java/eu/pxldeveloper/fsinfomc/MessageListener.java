package eu.pxldeveloper.fsinfomc;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals("BungeeCord")) {
            return;
        }
    }
}
