package com.example.artemis;

import javax.jms.*;
import javax.naming.Context;

public class XmlConsumer {
    public void receive() throws Exception {
        Context context = JndiHelper.getContext();
        ConnectionFactory factory = JndiHelper.getConnectionFactory(context);
        Queue queue = JndiHelper.getQueue(context);

        try (Connection connection = factory.createConnection()) {
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(queue);

            Message msg = consumer.receive(5000);
            if (msg instanceof TextMessage) {
                System.out.println("Received XML:\n" + ((TextMessage) msg).getText());
            } else {
                System.out.println("No message received.");
            }
        }
    }
}
