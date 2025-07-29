package com.zolqid.zolesle.util;

import com.zolqid.zolesle.MinecraftPlugin;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KodVeritabani {

    public static void kaydet(UUID uuid, String code, String name) {
        try {
            Connection conn = DatabaseManager.getConnection();

            // Önce varsa eski kaydı sil
            String deleteSQL = "DELETE FROM hesap_esleme WHERE minecraft_uuid = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL);
            deleteStmt.setString(1, uuid.toString());
            deleteStmt.executeUpdate();

            // Yeni kod kaydı
            String insertSQL = "INSERT INTO hesap_esleme (minecraft_uuid, discord_id, code, name) VALUES (?, '', ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
            insertStmt.setString(1, uuid.toString());
            insertStmt.setString(2, code);
            insertStmt.setString(3, name);
            insertStmt.executeUpdate();

        } catch (SQLException e) {
            MinecraftPlugin.getInstance().getLogger().severe("Kod kaydetme hatası: " + e.getMessage());
        }
    }

    public static void sil(UUID uuid) {
        try {
            Connection conn = DatabaseManager.getConnection();

            // Varsa kaydı sil
            String deleteSQL = "DELETE FROM hesap_esleme WHERE minecraft_uuid = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL);
            deleteStmt.setString(1, uuid.toString());
            deleteStmt.executeUpdate();

        } catch (SQLException e) {
            MinecraftPlugin.getInstance().getLogger().severe("Kod kaydetme hatası: " + e.getMessage());
        }
    }

    public static boolean kodVarMi(String code) {
        try {
            Connection conn = DatabaseManager.getConnection();
            String sql = "SELECT * FROM hesap_esleme WHERE code = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            MinecraftPlugin.getInstance().getLogger().severe("Kod kontrol hatası: " + e.getMessage());
        }
        return false;
    }

    public static String mcEslenmis(String minecraftUUID) {
        try {
            Connection conn = DatabaseManager.getConnection();
            String sql = "SELECT discord_id FROM hesap_esleme WHERE minecraft_uuid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, minecraftUUID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("discord_id");
        } catch (SQLException e) {
            MinecraftPlugin.getInstance().getLogger().severe("Kod kontrol hatası: " + e.getMessage());
        }
        return null;
    }

    public static boolean mckodVarMi(String minecraft_uuid) {
        try {
            Connection conn = DatabaseManager.getConnection();
            String sql = "SELECT minecraft_uuid FROM hesap_esleme WHERE minecraft_uuid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, minecraft_uuid);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            MinecraftPlugin.getInstance().getLogger().severe("Kod kontrol hatası: " + e.getMessage());
        }
        return false;
    }

    public static boolean eslenmisMi(String discord_id) {
        try {
            Connection conn = DatabaseManager.getConnection();
            String sql = "SELECT discord_id FROM hesap_esleme WHERE discord_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, discord_id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            MinecraftPlugin.getInstance().getLogger().severe("Kod kontrol hatası: " + e.getMessage());
        }
        return false;
    }

    public static String kodGetir(String minecraftUUID) {
        try {
            Connection conn = DatabaseManager.getConnection();
            String sql = "SELECT code FROM hesap_esleme WHERE minecraft_uuid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, minecraftUUID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("code");
        } catch (SQLException e) {
            MinecraftPlugin.getInstance().getLogger().severe("Kod kontrol hatası: " + e.getMessage());
        }
        return null;
    }

    public static Map<String, String> esleVeriGetir(String discordId) {
        Map<String, String> info = new HashMap<>();
        String query = "SELECT minecraft_uuid, discord_id, name FROM hesap_esleme WHERE discord_id = ?";
        try {
            Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, discordId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                info.put("minecraft_uuid", rs.getString("minecraft_uuid"));
                info.put("discord_id", rs.getString("discord_id"));
                info.put("name", rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }


    public static void eslestirDiscordId(String code, String discordId) {
        try {
            Connection conn = DatabaseManager.getConnection();
            String sql = "UPDATE hesap_esleme SET discord_id = ? WHERE code = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, discordId);
            ps.setString(2, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            MinecraftPlugin.getInstance().getLogger().severe("Discord ID eşleme hatası: " + e.getMessage());
        }
    }

    public static Timestamp getLastClaim(String discordId) {
        try {
            Connection conn = DatabaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT last_claim FROM boost_oduller WHERE discord_id = ?");
            ps.setString(1, discordId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getTimestamp("last_claim");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void setLastClaim(String discordId) {
        try {
        Connection conn = DatabaseManager.getConnection() ;
            PreparedStatement ps = conn.prepareStatement("REPLACE INTO boost_oduller (discord_id, last_claim) VALUES (?, ?)");
            ps.setString(1, discordId);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
