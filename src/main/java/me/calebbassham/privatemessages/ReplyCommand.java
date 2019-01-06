package me.calebbassham.privatemessages;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class ReplyCommand implements CommandExecutor, TabCompleter {

    private PrivateMessagesPlugin plugin;

    public ReplyCommand(PrivateMessagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        var player = (Player) sender;

        var recipient = plugin.senderOfLastMessageReceivedFor(player);

        if (recipient == null) {
            sender.sendMessage(cc("&cYou have not received any messages."));
        }

        var message = String.join(" ", args);

        plugin.sendPrivateMessage(sender, recipient, message);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }

    private String cc(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
