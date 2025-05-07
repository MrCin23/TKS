package pl.lodz.p.user.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @RequestMapping("/ping")
    @ResponseBody
    public String ping() {
        return "pong";
    }
}