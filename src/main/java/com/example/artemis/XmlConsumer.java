package com.example.artemis;

import javax.jms.*;
import javax.naming.Context;

public class XmlConsumer {

    private final DatabaseService db = new DatabaseService();


    public void receive(int maxMessages) throws Exception {
        Context context = JndiHelper.getContext();
        ConnectionFactory factory = JndiHelper.getConnectionFactory(context);
        Queue queue = JndiHelper.getQueue(context);

        try (Connection connection = factory.createConnection()) {
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(queue);

            System.out.println("⏳ Waiting for XML message...");
            System.out.println("📦 Will stop after " + maxMessages + " stored message(s).");

            int count = 0;
            while (count < maxMessages) {

                Message msg = consumer.receive();

                if (msg instanceof TextMessage textMessage) {

                    String xml = textMessage.getText();

                    // 🔑 Get duplicate ID from header
                    String messageId = msg.getStringProperty("_AMQ_DUPL_ID");

                    if (messageId == null) {
                        messageId = "NO_ID_" + System.currentTimeMillis();
                    }
                    boolean inserted = db.saveMessage(messageId, xml);

                    if (inserted) {
                        count++;
                        System.out.println("✅ Stored new message: " + messageId + " (" + count + " total)");
                    } else {
                        System.out.println("🚫 Duplicate ignored: " + messageId);
                    }
                }
            }
            System.out.println("✅ Reached maxMessages=" + maxMessages + ", stopping consumer.");
        }
    }
}
