package com.zolqid.zolesle.events.minecraft;

import com.zolqid.zolesle.DiscordBot;
import com.zolqid.zolesle.MinecraftPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        TextChannel channel = DiscordBot.getJda().getTextChannelById(MinecraftPlugin.getInstance().configManager.getConfig().getString("discord.messages_log"));

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(237, 142, 22));
        embed.setAuthor(p.getName() + " adlı kullanıcı giriş yaptı", null, "https://mc-heads.net/avatar/" + p.getUniqueId() + "/100");
        channel.sendMessageEmbeds(embed.build()).queue();

        e.setJoinMessage(ChatColor.GOLD + p.getDisplayName() + ChatColor.YELLOW + " adlı kullanıcı sunucumuza katıldı!");

        String message = "§e" + p.getName() + " §6adlı kullanıcı sunucumuza katıldı, merhaba de!" ;
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));

        DiscordBot.getJda().getPresence().setActivity(Activity.playing(Bukkit.getOnlinePlayers().size() + " kişi"));

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        TextChannel channel = DiscordBot.getJda().getTextChannelById(MinecraftPlugin.getInstance().configManager.getConfig().getString("discord.messages_log"));

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(198, 54, 54));
        embed.setAuthor(p.getName() + " adlı kullanıcı çıkış yaptı", null, "https://mc-heads.net/avatar/" + p.getUniqueId() + "/100");
        channel.sendMessageEmbeds(embed.build()).queue();

        e.setQuitMessage(ChatColor.GOLD + p.getDisplayName() + ChatColor.YELLOW + " adlı kullanıcı sunucumuzdan ayrıldı!");

        DiscordBot.getJda().getPresence().setActivity(Activity.playing(Bukkit.getOnlinePlayers().size() + " kişi"));
    }
}
