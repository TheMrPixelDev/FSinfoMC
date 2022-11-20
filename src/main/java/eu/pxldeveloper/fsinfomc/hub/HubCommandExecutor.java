package eu.pxldeveloper.fsinfomc.hub;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.pxldeveloper.fsinfomc.FSInfoMC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0 && sender instanceof Player) {
            Player player = (Player) sender;
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("lobby");
            player.sendPluginMessage(FSInfoMC.instance, "BungeeCord", out.toByteArray());
            System.out.println("[FSINFOMC] Moving player " + player.getName() + " to server lobby");
            return true;
        }
        return false;
    }
}
