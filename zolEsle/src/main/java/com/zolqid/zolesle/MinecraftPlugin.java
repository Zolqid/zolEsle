package com.zolqid.zolesle;

import com.zolqid.zolesle.config.ConfigManager;
import com.zolqid.zolesle.events.minecraft.HesapEsleCommand;
import com.zolqid.zolesle.events.minecraft.HesapKaldirCommand;
import com.zolqid.zolesle.events.minecraft.MessageListener;
import com.zolqid.zolesle.events.minecraft.PlayerJoinListener;
import com.zolqid.zolesle.util.DatabaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class MinecraftPlugin extends JavaPlugin {

    private static MinecraftPlugin instance;
    public static ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();
        configManager = new ConfigManager(this);

        // MySQL Bağlantısı Başlat
        DatabaseManager.connect(
                configManager.getConfig().getString("mysql.host"),
                getConfig().getInt("mysql.port"),
                configManager.getConfig().getString("mysql.database"),
                configManager.getConfig().getString("mysql.username"),
                configManager.getConfig().getString("mysql.password")
        );

        // Komut kaydet
        getCommand("hesapesle").setExecutor(new HesapEsleCommand());
        getCommand("hesapkaldir").setExecutor(new HesapKaldirCommand());
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new MessageListener(), this);

        getLogger().info("HesapEsle eklentisi aktif!");

        // Discord botunu başlat
        String token = configManager.getConfig().getString("discord.token");
        try {
            DiscordBot.startBot(token);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendStartEmbed();
        DiscordBot.loadCommands();
    }

    @Override
    public void onDisable() {
        sendStopEmbed();
        DatabaseManager.disconnect();
    }

    public static MinecraftPlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager(String s) {
        return configManager;
    }

    private void sendStartEmbed(){
        TextChannel channel = DiscordBot.getJda().getTextChannelById(configManager.getConfig().getString("discord.messages_log"));
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(2, 253, 27));
        embed.setAuthor("Sunucu başlatıldı!", null, DiscordBot.getJda().getSelfUser().getAvatarUrl());

        channel.sendMessageEmbeds(embed.build()).queue();


    }

    private void sendStopEmbed(){
        TextChannel channel = DiscordBot.getJda().getTextChannelById(configManager.getConfig().getString("discord.messages_log"));
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.RED);
        embed.setAuthor("Sunucu kapatıldı!", null, DiscordBot.getJda().getSelfUser().getAvatarUrl());

        channel.sendMessageEmbeds(embed.build()).queue();

    }

    private void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }
}
