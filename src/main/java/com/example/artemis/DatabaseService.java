package com.example.artemis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseService {

    private static final String DEFAULT_URL = "jdbc:postgresql://172.28.160.1:6432/messages";
    private static final String DEFAULT_USER = "bruno";
    private static final String DEFAULT_PASSWORD = "alto";

    private final HikariDataSource ds;

    public DatabaseService() {
        String url = getConfig("DB_URL", DEFAULT_URL);
        String user = getConfig("DB_USER", DEFAULT_USER);
        String password = getConfig("DB_PASSWORD", DEFAULT_PASSWORD);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(2000);

        this.ds = new HikariDataSource(config);
    }

    private static String getConfig(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(key);
        }
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public boolean saveMessage(String messageId, String xml) {
        String sql = """
            INSERT INTO received_messages (message_id, xml_content)
            VALUES (?, ?)
            ON CONFLICT (message_id) DO NOTHING
        """;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, messageId);
            ps.setString(2, xml);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            if ("28P01".equals(e.getSQLState())) {
                System.err.println("Database authentication failed. Check DB_URL/DB_USER/DB_PASSWORD, PgBouncer userlist.txt, and PostgreSQL credentials.");
            }
            e.printStackTrace();
            return false;
        }
    }
}