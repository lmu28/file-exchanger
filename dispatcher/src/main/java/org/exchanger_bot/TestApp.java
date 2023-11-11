package org.exchanger_bot;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.api.objects.Update;



@SpringBootApplication
public class TestApp {

    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }

//    @Bean
//    public ApplicationRunner runner(AmqpTemplate template) {
//        return args -> template.convertAndSend("myqueue", "foo");
//    }

//    @Bean
//    public Queue myQueue() {
//        return new Queue(TEXT_MESSAGE_UPDATE);
//    }

//   @RabbitListener(queues =TEXT_MESSAGE_UPDATE)
//    public void listen(Update in) {
//        System.out.println("----------------");
//        System.out.println(in);
//    }

}
