package me.calebbassham.privatemessages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class PrivateMessagesPlugin extends JavaPlugin {

    private HashMap<UUID, UUID> lastMessageReceivedFrom = new HashMap<>();

    @Override
    public void onEnable() {
        registerCommand("message", new PrivateMessageCommand(this));
        registerCommand("reply", new ReplyCommand(this));
    }

    private void registerCommand(String commandName, CommandExecutor command) {
        var pluginCommand = Bukkit.getPluginCommand(commandName);
        pluginCommand.setExecutor(command);
        if (command instanceof TabCompleter) {
            pluginCommand.setTabCompleter((TabCompleter) command);
        }
    }

    public CommandSender senderOfLastMessageReceivedFor(Player recipient) {
        if (!lastMessageReceivedFrom.containsKey(recipient.getUniqueId())) return null;

        var sender = lastMessageReceivedFrom.get(recipient.getUniqueId());
        if (sender != null) {
            return Bukkit.getPlayer(sender);
        } else {
            return Bukkit.getConsoleSender();
        }
    }

    public void sendPrivateMessage(CommandSender sender, CommandSender recipient, String message) {
        String recipientDisplayName;
        UUID recipientUUID = null;
        if (recipient instanceof Player) {
            var player = (Player) recipient;
            recipientDisplayName = player.getDisplayName();
            recipientUUID = player.getUniqueId();
        } else {
            recipientDisplayName = recipient.getName();
        }

        sender.sendMessage(cc(String.format("&8[&3&oYou &7-> &b%s&8] &7%s", recipientDisplayName, message)));

        String senderDisplayName;
        if (sender instanceof Player) {
            var player = (Player) sender;
            senderDisplayName = player.getDisplayName();
            if (recipientUUID != null) {
                lastMessageReceivedFrom.put(recipientUUID, player.getUniqueId());
            }
        } else {
            senderDisplayName = sender.getName();
            if (recipientUUID != null) {
                lastMessageReceivedFrom.put(recipientUUID, null);
            }
        }

        recipient.sendMessage(cc(String.format("&8[&3&oYou &7<- &b%s&8] &7%s", senderDisplayName, message)));
    }

    private String cc(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
