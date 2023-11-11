package org.exchanger_bot;


import lombok.extern.log4j.Log4j;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.api.objects.Update;


@SpringBootApplication
@Log4j
public class DispatcherApplication
{
    public static void main( String[] args )
    {
        ConfigurableApplicationContext a  = SpringApplication.run(DispatcherApplication.class);
    }





}
