package ru.stk.eshop.utils;

import com.rabbitmq.client.*;
import org.aspectj.bridge.IMessage;

import java.io.IOException;

public class ReceiverApp {
    private final static String QUEUE_NAME = "shopcart";
    public static void receive() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("wait message");

        final DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody(), "UTF-8");
                System.out.println("MQ received: " + msg);
            }
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}
