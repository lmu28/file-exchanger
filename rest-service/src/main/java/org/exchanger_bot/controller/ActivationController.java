package org.exchanger_bot.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.service.UserActivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j
@RequestMapping("/user")
public class ActivationController {

    private final UserActivationService userActivationService;



    @RequestMapping(method = RequestMethod.GET, value = "/activation")
    public ResponseEntity<?> activate(@RequestParam("id") String id){
        boolean activated = userActivationService.activation(id);

        if (activated) return ResponseEntity.ok().build();
        //TODO предусмотреть возможность ввода от пользователя левого хэша Id
        return ResponseEntity.internalServerError().build();
    }


}
