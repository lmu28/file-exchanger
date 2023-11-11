//package org.exchanger_bot.controller;
//
//
//import lombok.RequiredArgsConstructor;
//import org.exchanger_bot.utils.dto.UserMailInfo;
//import org.exchanger_bot.service.MailService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/mail")
//public class MailController {
//
//    private final MailService mailService;
//
//
//
//
//    @PostMapping("/send")
//    public ResponseEntity<?> sendMessage(@RequestBody UserMailInfo userMailInfo){
//
//        mailService.sendMessage(userMailInfo);
//
//        //TODO добавить Advices для обработки разных ситуаций
//        return ResponseEntity.ok().build();
//
//    }
//}
