package ru.stk.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.stk.eshop.entities.Greeting;
import ru.stk.eshop.entities.Message;

@Controller
public class WsController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message message) throws Exception {
        Thread.sleep(3000); // simulated delay
        return new Greeting( "новый товар" + message.getName());
    }

    public void sendMessage(String destination, Greeting message) {
        simpMessagingTemplate.convertAndSend(destination, message);
    }

}
