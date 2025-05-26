package pl.lodz.p.rest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableMethodSecurity(prePostEnabled = true)
public class PingController {
    @PreAuthorize("hasRole('CLIENT')")
    @RequestMapping("/ping")
    @ResponseBody
    public String ping() {
        return "pong";
    }
}