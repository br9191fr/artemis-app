package com.example.artemis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DatabaseService {

    private static final String URL = "jdbc:postgresql://localhost:5432/messages";
    private static final String USER = "user";
    private static final String PASSWORD = "alto";

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

            return rows > 0; // true = inserted, false = duplicate

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
