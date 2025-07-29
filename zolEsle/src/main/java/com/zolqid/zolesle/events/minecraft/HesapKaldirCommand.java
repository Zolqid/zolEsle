package com.zolqid.zolesle.events.minecraft;

import com.zolqid.zolesle.DiscordBot;
import com.zolqid.zolesle.MinecraftPlugin;
import com.zolqid.zolesle.util.KodUretici;
import com.zolqid.zolesle.util.KodVeritabani;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HesapKaldirCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String prefix = ChatColor.translateAlternateColorCodes('&', MinecraftPlugin.getInstance().configManager.getConfig().getString("prefix"));
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "Bu komut sadece oyuncular için.");
            return true;
        }

        Player player = (Player) sender;

        String uuid = player.getUniqueId().toString();
        String discord_id = KodVeritabani.mcEslenmis(uuid);

        if (discord_id == null || discord_id.isEmpty()) {
            player.sendMessage(prefix + " §cHesabın zaten eşli değil!");
            return true;
        }

        KodVeritabani.sil(player.getUniqueId());
        player.sendMessage(prefix + " Eşli hesabını başarıyla kaldırdın");

        TextChannel channel = DiscordBot.getJda().getTextChannelById(MinecraftPlugin.getInstance().configManager.getConfig().getString("discord.messages_log"));
        channel.sendMessage(String.format("\uD83D\uDC64 \uD83D\uDD13 **%s**, discord hesabının eşlemesini kaldırdı", player.getName())).queue();

        return true;
    }

}
