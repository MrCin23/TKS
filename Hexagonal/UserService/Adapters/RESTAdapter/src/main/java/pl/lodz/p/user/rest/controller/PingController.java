package pl.lodz.p.user.rest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@EnableMethodSecurity(prePostEnabled = true)
@RestController
public class PingController {
    @RequestMapping("/ping")
    @ResponseBody
    public String ping() {
        return "pong";
    }

    @GetMapping("/me")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String me() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}