package com.zolqid.zolesle;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class DiscordBot {

    private static JDA jda;

    public static void startBot(String token) throws InterruptedException {
        jda = JDABuilder.createDefault(token)
                .addEventListeners(
                        new com.zolqid.zolesle.events.discord.ButtonHandler(),
                        new com.zolqid.zolesle.events.discord.ModalHandler(),
                        new com.zolqid.zolesle.events.discord.SlashCommandHandler(),
                        new com.zolqid.zolesle.events.discord.MessageReceivedHandler()
                )
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_PRESENCES
                        )
                .build().awaitReady();
        System.out.println("Discord botu basariyla baslatildi!");

    }

    public static void loadCommands() {
        jda.upsertCommand("sorgula", "Kullanıcının hesabını sorgular.")
                .addOption(OptionType.USER, "kullanıcı", "Hedef Discord kullanıcısı", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)).queue();

        jda.upsertCommand("boostödül", "Boost ödül mesajını gönderir.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)).queue();

        jda.upsertCommand("hesapeslemesaj", "Hesap eşleme mesajını gönderir.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)).queue();
    }

    public static JDA getJda() {
        return jda;
    }
}
