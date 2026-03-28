package com.example.artemis;

import javax.jms.*;
import javax.naming.Context;
import java.util.UUID;

public class XmlProducer {

    public void send(String xmlMessage) throws Exception {

        Context context = JndiHelper.getContext();
        ConnectionFactory factory = JndiHelper.getConnectionFactory(context);
        Queue queue = JndiHelper.getQueue(context);

        try (Connection connection = factory.createConnection()) {

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);

            // Same duplicate ID for all messages (to simulate duplicates)
            String duplicateId = null;

            for (int i = 1; i <= 10; i++) {
                duplicateId = UUID.randomUUID().toString();
                TextMessage message = session.createTextMessage(xmlMessage);

                // 🔑 Artemis duplicate detection header
                message.setStringProperty("_AMQ_DUPL_ID", duplicateId);

                producer.send(message);

                System.out.println("✅ Sent message #" + i + " with duplicateId=" + duplicateId);
            }
        }
    }
}
