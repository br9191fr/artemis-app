package com.example.artemis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseService {

    private static final String DEFAULT_URL = "jdbc:postgresql://127.0.0.1:5432/messages";
    private static final String DEFAULT_USER = "bruno";
    private static final String DEFAULT_PASSWORD = "alto";

    private static final String URL = getConfig("DB_URL", DEFAULT_URL);
    private static final String USER = getConfig("DB_USER", DEFAULT_USER);
    private static final String PASSWORD = getConfig("DB_PASSWORD", DEFAULT_PASSWORD);

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

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, messageId);
            ps.setString(2, xml);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            if ("28P01".equals(e.getSQLState())) {
                System.err.println("Database authentication failed. Check DB_USER/DB_PASSWORD and PostgreSQL user credentials.");
            }
            e.printStackTrace();
            return false;
        }
    }
}
