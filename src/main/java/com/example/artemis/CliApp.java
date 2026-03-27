package com.example.artemis;

import java.nio.file.Files;
import java.nio.file.Path;

public class CliApp {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: send <file> | receive");
            return;
        }

        switch (args[0]) {
            case "send":
                String xml = Files.readString(Path.of(args[1]));
                new XmlProducer().send(xml);
                break;
            case "receive":
                new XmlConsumer().receive();
                break;
        }
    }
}
