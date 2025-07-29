package com.zolqid.zolesle.events.discord;

import com.zolqid.zolesle.util.KodVeritabani;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Map;

public class SlashCommandHandler extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("sorgula")) {

            String user_id = event.getOption("kullanıcı").getAsUser().getId();
            String user = event.getOption("kullanıcı").getAsMember().getAsMention();

            Map<String, String> codeData = KodVeritabani.esleVeriGetir(user_id);
            if (KodVeritabani.eslenmisMi(user_id)) {
                event.reply(String.format("%s adlı kullanıcının hesabı **%s** isimli Minecraft hesabı ile eşlenmiş!", user, codeData.get("name"))).setEphemeral(true).queue();

            } else {
                event.reply(user + " adlı kullanıcın hesabı bir Minecraft hesabı ile eşlenmemiş!").setEphemeral(true).queue();
            }

        }

        if (event.getName().equals("hesapeslemesaj")) {
            event.deferReply().setEphemeral(true).queue();
            TextChannel channel = event.getChannel().asTextChannel();

            channel.sendMessage("Minecraft hesabınız ile Discord hesabınızı eşlemek için butona tıklayın!")
                    .setActionRow(
                            Button.primary("hesap_esle_button", "Hesabını Doğrula")
                    ).queue();
            event.getHook().editOriginal("Mesaj başarıyla gönderildi.").queue();
        }

        if (event.getName().equals("boostödül")) {
            event.deferReply().setEphemeral(true).queue();
            TextChannel channel = event.getChannel().asTextChannel();

            channel.sendMessage("Boost ödülünü almak için tıkla!")
                    .setActionRow(
                            Button.primary("odul_al", "Ödülünü Al")
                    ).queue();
            event.getHook().editOriginal("Mesaj başarıyla gönderildi.").queue();
        }
    }
}

