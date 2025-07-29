package com.zolqid.zolesle.events.minecraft;


import com.zolqid.zolesle.DiscordBot;
import com.zolqid.zolesle.MinecraftPlugin;
import com.zolqid.zolesle.util.KodUretici;
import com.zolqid.zolesle.util.KodVeritabani;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class HesapEsleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String prefix = ChatColor.translateAlternateColorCodes('&', MinecraftPlugin.getInstance().configManager.getConfig().getString("prefix"));
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "Bu komut sadece oyuncular için.");
            return true;
        }

        Player player = (Player) sender;

        String uuid = player.getUniqueId().toString();
        String name = player.getName().toString();

        String discord_id = KodVeritabani.mcEslenmis(uuid);
        if (discord_id != null && !discord_id.isEmpty() && KodVeritabani.eslenmisMi(discord_id)) {
            User user = DiscordBot.getJda().getUserById(discord_id);
            System.out.println(user);

            if (user != null) {
                player.sendMessage(prefix + " Hesabın zaten §c" + user.getEffectiveName() + "§7 isimli oyun hesabın ile eşlenmiş.");
            } else {
                player.sendMessage(prefix + " Hesabın zaten bir Discord hesabıyla eşlenmiş.");
            }
            return true;
        }

        if (KodVeritabani.mckodVarMi(uuid)) {
            String code = KodVeritabani.kodGetir(uuid);
            player.sendMessage(prefix + " Zaten bir kod almışsın: §c" + code);
            return true;
        }

        String kod = KodUretici.uret();

        KodVeritabani.kaydet(player.getUniqueId(), kod, name);

        player.sendMessage(prefix + " Discord hesabınızı eşlemek için kodun: §c" + kod);

        return true;
    }
}
