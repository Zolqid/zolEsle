package com.zolqid.zolesle.events.discord;

import com.zolqid.zolesle.MinecraftPlugin;
import com.zolqid.zolesle.util.KodVeritabani;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageReceivedHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String ticaretKanalId = MinecraftPlugin.getInstance().configManager.getConfig().getString("discord.trade_channel");
        if (event.getChannel().getId().equals(ticaretKanalId)) {

            if (event.getAuthor().isBot()) return;
            String discordId = event.getAuthor().getId();
            if (!KodVeritabani.eslenmisMi(discordId)) {
                event.getMessage().delete().queue();
                event.getChannel().sendMessage("❌ Hey " + event.getMember().getAsMention() +  " bu kanalda mesaj gönderebilmek için Minecraft hesabını eşlemen gerekiyor.").queue(msg -> {
                    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                    scheduler.schedule(() -> msg.delete().queue(), 5, TimeUnit.SECONDS);
                }, error -> {
                    System.out.println("Ticaret - Engel mesajı silinenemedi! " + error.getMessage());
                });
                return;
            }

            String content = event.getMessage().getContentRaw();
            event.getMessage().delete().queue();

            TextChannel channel = (TextChannel) event.getChannel();
            Map<String, String> codeData = KodVeritabani.esleVeriGetir(discordId);

            String avatar_url = "https://mc-heads.net/avatar/" + codeData.get("minecraft_uuid") + "/100";

            try {
                channel.createWebhook(codeData.get(("name")))
                        .setAvatar(Icon.from(new URL(avatar_url).openStream()))
                        .queue(webhook -> {
                            webhook.sendMessage(content).queue();

                            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                            scheduler.schedule(() -> webhook.delete().queue(), 2, TimeUnit.SECONDS);
                        }, error -> {
                            event.getChannel().sendMessage("❌ Webhook oluşturulamadı: " + error.getMessage()).queue();
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
