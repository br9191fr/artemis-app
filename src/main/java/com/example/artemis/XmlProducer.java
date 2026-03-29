package com.example.artemis;

import javax.jms.*;
import javax.naming.Context;
import java.security.SecureRandom;
import java.util.UUID;

public class XmlProducer {

    public void send(String xmlMessage) throws Exception {

        Context context = JndiHelper.getContext();
        ConnectionFactory factory = JndiHelper.getConnectionFactory(context);
        Queue queue = JndiHelper.getQueue(context);

        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        try (Connection connection = factory.createConnection()) {

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);

            // Same duplicate ID for all messages (to simulate duplicates)
            String currentId = UUID.randomUUID().toString();;

            for (int i = 1; i <= 10; i++) {
                if (random.nextInt(100) < 70) {
                    currentId = UUID.randomUUID().toString();
                    System.out.println("✅ Sent message #" + i + " with currentId=" + currentId);
                }
                TextMessage message = session.createTextMessage(xmlMessage);
                // 🔑 Artemis duplicate detection header
                message.setStringProperty("_AMQ_DUPL_ID", currentId);

                producer.send(message);


            }
        }
    }
}
