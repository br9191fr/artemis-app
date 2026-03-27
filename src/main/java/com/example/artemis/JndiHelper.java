package com.example.artemis;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;

public class JndiHelper {
    public static Context getContext() throws NamingException {
        return new InitialContext();
    }

    public static ConnectionFactory getConnectionFactory(Context context) throws NamingException {
        return (ConnectionFactory) context.lookup("myFactory");
    }

    public static Queue getQueue(Context context) throws NamingException {
        return (Queue) context.lookup("myQueue");
    }
}
