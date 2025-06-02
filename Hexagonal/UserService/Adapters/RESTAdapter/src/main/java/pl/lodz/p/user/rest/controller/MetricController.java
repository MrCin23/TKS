package pl.lodz.p.user.rest.controller;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.user.rest.aspect.Counted;

@AllArgsConstructor
@RestController
@RequestMapping("/metric")
@Counted
public class MetricController {

    private final PrometheusMeterRegistry registry;

    @GetMapping
    public String metrics() {
        return registry.scrape();
    }
}
