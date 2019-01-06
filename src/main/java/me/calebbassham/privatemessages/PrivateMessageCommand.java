package me.calebbassham.privatemessages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PrivateMessageCommand implements CommandExecutor, TabCompleter {

    private PrivateMessagesPlugin plugin;

    public PrivateMessageCommand(PrivateMessagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 2) {
            return false;
        }

        var recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null) {
            sender.sendMessage(cc(String.format("&c%s is offline.", args[0])));
            return true;
        }

        var message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]);
            if(i != args.length - 1) {
                message.append(" ");
            }
        }

        plugin.sendPrivateMessage(sender, recipient, message.toString());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(HumanEntity::getName)
                    .filter(player -> !player.equals(sender.getName()))
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    private String cc(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
