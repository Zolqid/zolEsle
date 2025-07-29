package com.zolqid.zolesle.events.discord;


import com.zolqid.zolesle.DiscordBot;
import com.zolqid.zolesle.MinecraftPlugin;
import com.zolqid.zolesle.util.KodVeritabani;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class ModalHandler extends ListenerAdapter {

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("hesap-esleme")) {

            String kod = event.getValue("kod").getAsString();

            if (KodVeritabani.kodVarMi(kod)) {
                // Kod varsa discord ID ile eşle
                KodVeritabani.eslestirDiscordId(kod, event.getUser().getId());

                String id = event.getMember().getId().toString();
                Map<String, String> codeData = KodVeritabani.esleVeriGetir(id);
                event.reply(String.format("✅ Hesabın başarıyla **%s** isimli Minecraft hesabı ile eşlendi!", codeData.get("name"))).setEphemeral(true).queue();

                Player p = Bukkit.getPlayer(codeData.get("name"));
                if (p != null && p.isOnline()) {
                    String prefix = ChatColor.translateAlternateColorCodes('&', MinecraftPlugin.getInstance().configManager.getConfig().getString("prefix"));
                    p.sendMessage(prefix + " Hesabın başarıyla §c" + codeData.get("name") + " §7isimli Discord hesabı ile eşlendi");
                }

                if (KodVeritabani.eslenmisMi(id)) {
                    TextChannel channel = DiscordBot.getJda().getTextChannelById(MinecraftPlugin.getInstance().configManager.getConfig().getString("discord.messages_log"));
                    channel.sendMessage(String.format("\uD83D\uDC64 \uD83D\uDD12 **%s**, discord hesabını **%s** isimli Minecraft hesabı ile eşledi", event.getMember().getEffectiveName(), codeData.get("name"))).queue();
                }
            } else {
                event.reply("❌ Kod geçersiz veya süresi dolmuş!").setEphemeral(true).queue();
            }
        }
    }
}
