package com.example.artemis;

import javax.jms.*;
import javax.naming.Context;

public class XmlProducer {
    public void send(String xmlMessage) throws Exception {
        Context context = JndiHelper.getContext();
        ConnectionFactory factory = JndiHelper.getConnectionFactory(context);
        Queue queue = JndiHelper.getQueue(context);

        try (Connection connection = factory.createConnection()) {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage(xmlMessage);
            producer.send(message);
            System.out.println("Sent XML:\n" + xmlMessage);
        }
    }
}
