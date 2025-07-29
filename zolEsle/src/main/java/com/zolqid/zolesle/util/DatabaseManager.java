package com.zolqid.zolesle.util;

import com.zolqid.zolesle.MinecraftPlugin;

import java.sql.*;

public class DatabaseManager {

    private static Connection connection;

    public static void connect(String host, int port, String database, String user, String password) {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";


        try {
            connection = DriverManager.getConnection(url, user, password);
            MinecraftPlugin.getInstance().getLogger().info("MySQL baglantisi saglandi.");

            try (Statement stmt = connection.createStatement()) {

                stmt.execute( "CREATE TABLE IF NOT EXISTS hesap_esleme (" +
                        "minecraft_uuid VARCHAR(36) PRIMARY KEY," +
                        "discord_id VARCHAR(20) NOT     NULL," +
                        "code VARCHAR(10) NOT NULL," +
                        "name VARCHAR(32) NOT NULL," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ");");

                stmt.execute("CREATE TABLE IF NOT EXISTS boost_oduller (\n" +
                        "  discord_id VARCHAR(32) PRIMARY KEY,\n" +
                        "  last_claim TIMESTAMP\n" +
                        ");");

                System.out.println("Tablolar babarıyla kuruldu veya zaten mevcut.");
            } catch (SQLException e) {
                System.err.println("Tablo kurulumunda hata olustu!");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            MinecraftPlugin.getInstance().getLogger().severe("MySQL baglanti hatasi: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                MinecraftPlugin.getInstance().getLogger().info("MySQL baglantisi kapatildi");
            }
        } catch (SQLException e) {
            MinecraftPlugin.getInstance().getLogger().severe("MySQL baglanti kapatma hatasi " + e.getMessage());
        }
    }
}
