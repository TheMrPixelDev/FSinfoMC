package eu.pxldeveloper.fsinfomc.portals;

import eu.pxldeveloper.fsinfomc.FSInfoMC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PortalCommandExecutor implements CommandExecutor, TabExecutor {

    private ArrayList<Portal> portals = FSInfoMC.getInstance().portals;
    private ArrayList<PortalCreateSession> sessions = FSInfoMC.getInstance().sessions;

    private void addPortal(String[] args, CommandSender sender) {

        if(!(sender instanceof Player)) {
            return;
        }

        String portalName = args[1];

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[PORTALS] Du musst einen Namen für das Portal angeben: /portal add <name> \n Vergiss nicht die Eckpunkte zu setzen: /portal config <name> <A|B>");
        } else {
            PortalCreateSession session = new PortalCreateSession((Player) sender);
            session.setPortalName(portalName);
            sessions.add(session);
            sender.sendMessage(ChatColor.GREEN + "[PORTALS] Wähle nun eine Region für dein Portal.");
        }
    }

    private void removePortal(String[] args, CommandSender sender) {
        String portalName = args[1];
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[PORTALS] Du musst den Namen des Portals angeben: /portal remove <name>");
        } else {
            portals = (ArrayList<Portal>) portals.stream().filter(portal -> !portal.getName().equals(portalName)).collect(Collectors.toList());
            sender.sendMessage(ChatColor.GREEN + "[PORTALS] Portal entfernt: " + portalName);
        }
    }

    private void configPortal(String[] args, CommandSender sender) {
        if(args.length >= 3){

            portals.forEach(portal -> {
                if(portal.getName().equals(args[1])){

                    String option = args[2];

                    switch (option) {
                        case "server":
                            String serverName = args[3];
                            portal.setServerName(serverName);
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED + "[PORTALS] Usage: /portal config <name> <server|message> <value>");
                            break;
                    }
                }
            });

        }else{
            sender.sendMessage(ChatColor.RED + "[PORTALS] Usage: /portal config <name> <server|message> <value>");
        }
    }

    private void enablePortal(String[] args, CommandSender sender) {

        String portalName = args[1];

        portals.forEach(portal -> {
            if(portal.getName().equals(portalName)) {
                portal.enable();
                sender.sendMessage(ChatColor.GREEN + "[PORTALS] Das Portal " + portalName + " wurde aktiviert.");
            }
        });
    }

    private void disablePortal(String[] args, CommandSender sender) {

        String portalName = args[1];

        portals.forEach(portal -> {
            if(portal.getName().equals(portalName)) {
                portal.disable();
                sender.sendMessage(ChatColor.GREEN + "[PORTALS] Das Portal " + portalName + " wurde deaktiviert.");
            }
        });
    }

    private void listPortals(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "###### Alle Portale #######");
        portals.forEach(portal -> {
            sender.sendMessage("+ Name: " + ChatColor.GOLD + portal.getName() + ChatColor.WHITE + " an " + ChatColor.DARK_AQUA + portal.getCordsString() + ChatColor.RED + " (enabled: " + portal.enabled + ") " + ChatColor.BLUE + "Server: " + portal.getServerName());
        });
        sender.sendMessage(ChatColor.GOLD + "#######################");
    }

    private void toggleNaviator(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) { return; }

        switch (args[1]) {
            case "enable":
                FSInfoMC.getInstance().enableLobbyCompass = true;
                sender.sendMessage(ChatColor.GREEN + "[PORTALS] Navigator wurde aktiviert. (Wirksam nach reload)");
                break;
            case "disable":
                FSInfoMC.getInstance().enableLobbyCompass = false;
                sender.sendMessage(ChatColor.GREEN + "[PORTALS] Navigator wurde deaktiviert. (Wirksam nach reload)");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "[PORTALS] Usage: /portal navigator <enable|disable>");
                break;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {

            String option = args[0];

            switch (option){
                case "add":
                    addPortal(args, sender);
                    break;

                case "remove":
                    removePortal(args, sender);
                    break;

                case "enable":
                    enablePortal(args, sender);
                    break;

                case "disable":
                    disablePortal(args, sender);
                    break;

                case "config":
                    configPortal(args, sender);
                    break;

                case "list":
                    listPortals(sender);
                    break;
                case "navigator":
                    toggleNaviator(sender, args);
                    break;

            }

            return true;
        }
        return false;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
