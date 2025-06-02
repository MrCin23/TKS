package pl.lodz.p.rest.controller;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Writer;

@RestController("/metrics")
public class MetricsController {

    @Autowired
    private PrometheusMeterRegistry prometheusMeterRegistry;

    @GetMapping
    public void scrape(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(TextFormat.CONTENT_TYPE_004);

        try (Writer writer = response.getWriter()) {
            TextFormat.write004(writer, prometheusMeterRegistry.getPrometheusRegistry().metricFamilySamples());
        }
    }
}

