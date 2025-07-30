package com.zolqid.zolesle.events.discord;

import com.zolqid.zolesle.MinecraftPlugin;
import com.zolqid.zolesle.util.KodVeritabani;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalInteraction;

import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

public class ButtonHandler extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("hesap_esle_button")) {

            String id = event.getMember().getId().toString();
            Map<String, String> codeData = KodVeritabani.esleVeriGetir(id);
            if (KodVeritabani.eslenmisMi(id)) {
                event.reply("Discord hesabın zaten `" + codeData.get("name") + "` isimli oyun hesabın ile eşlenmiş.").setEphemeral(true).queue();
                return;
            }

            TextInput input = TextInput.create("kod", "Kodu Gir", TextInputStyle.SHORT)
                    .setMinLength(10)
                    .setMaxLength(10)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("hesap-esleme", "Hesap Eşleme")
                    .addActionRows(net.dv8tion.jda.api.interactions.components.ActionRow.of(input))
                    .build();

            event.replyModal(modal).queue();
        }

        if (event.getComponentId().equals("odul_al")) {

            String discordId = event.getUser().getId();
            Member member = event.getGuild().getMemberById(discordId);
            if (member == null || member.getTimeBoosted() == null) {
                event.reply("🚫 Bu ödülü almak için sunucumuza boost basmış olman gerekiyor.").setEphemeral(true).queue();
                return;
            }
            if (!KodVeritabani.eslenmisMi(discordId)) {
                event.reply("❌ Bu ödülü almak için Minecraft hesabınla eşleşmelisin.").setEphemeral(true).queue();
                return;
            }

            Timestamp last = KodVeritabani.getLastClaim(discordId);
            long lastReward = (last != null) ? last.getTime() : 0L;  // Eğer null ise 0 al
            long now = System.currentTimeMillis();
            long oneWeek = 7L * 24 * 60 * 60 * 1000;

            if (now - lastReward < oneWeek) {
                long kalan = (lastReward + oneWeek) - now;

                long gün = kalan / (1000 * 60 * 60 * 24);
                long saat = (kalan / (1000 * 60 * 60)) % 24;
                long dakika = (kalan / (1000 * 60)) % 60;
                long saniye = (kalan / 1000) % 60;

                String toplamSüre = gün + " gün, " + saat + " saat, " + dakika + " dakika, " + saniye + " saniye";

                event.reply(String.format("⏳ Haftalık ödülünü zaten aldın. Bir sonraki ödülünü **%s** sonra alabilirsin.", toplamSüre))
                        .setEphemeral(true).queue();
                return;
            }

            Map<String, String> codeData = KodVeritabani.esleVeriGetir(discordId);
            String uuid = codeData.get("minecraft_uuid");

            Player player = Bukkit.getPlayer(UUID.fromString(uuid));

            if (player == null || !player.isOnline()) {
                event.reply("❌ Ödülü alabilmek için Minecraft sunucusunda çevrim içi olmanız gerekiyor.").setEphemeral(true).queue();
                return;
            }
            String playerName = KodVeritabani.esleVeriGetir(discordId).get("name");

            Bukkit.getScheduler().runTask(MinecraftPlugin.getInstance(), () -> {
                for (String cmd : MinecraftPlugin.getInstance().configManager.getConfig().getStringList("odul.komutlar")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", playerName));
                }
            });

            if (player != null && player.isOnline()) {
                player.sendTitle("§6BAŞARILI", "Boost Ödülünü Başarıyla Aldın!", 10, 70, 20);
            }

            KodVeritabani.setLastClaim(discordId);
            event.reply("🎉 Minecraft sunucusunda ödülünü aldın, iyi oyunlar!").setEphemeral(true).queue();
        }
    }
}

