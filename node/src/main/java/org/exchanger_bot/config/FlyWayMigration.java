//package org.exchanger_bot.config;
//
//import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FlyWayMigration {
//
//    @Bean
//    public FlywayMigrationStrategy flywayMigrationStrategy() {
//        return flyway -> {
//            flyway.repair();
//            flyway.migrate();
//            System.out.println("Migration**********************************");
//        };
//    }
//}