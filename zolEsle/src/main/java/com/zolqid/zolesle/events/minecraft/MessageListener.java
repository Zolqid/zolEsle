package com.zolqid.zolesle.events.minecraft;

import com.zolqid.zolesle.DiscordBot;
import com.zolqid.zolesle.MinecraftPlugin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String tarih = sdf.format(new Date());

        TextChannel channel = DiscordBot.getJda().getTextChannelById(MinecraftPlugin.getInstance().configManager.getConfig().getString("discord.messages_log"));
        channel.sendMessage("\uD83D\uDCAC | **" + p.getName() + "** » " + e.getMessage()).queue();

    }
}
