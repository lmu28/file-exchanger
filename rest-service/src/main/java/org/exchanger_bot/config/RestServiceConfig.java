package org.exchanger_bot.config;


import org.exchanger_bot.utils.CryptoTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestServiceConfig {
    @Value("${salt}")
    private String salt;

    @Bean
    public CryptoTools getCryptoTool() {
        return new CryptoTools(salt);
    }



}
