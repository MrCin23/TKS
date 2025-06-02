package pl.lodz.p.rest.controller;


import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.rest.aspect.Counted;
import pl.lodz.p.rest.config.HealthIndicator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.io.File;
import java.time.Instant;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/health")
@Counted
public class HealthController {


    @Autowired(required = false)
    private DataSource dataSource;

    @Autowired(required = false)
    private List<HealthIndicator> healthIndicators = new ArrayList<>();

    @GetMapping("/test")
    @ResponseBody
    @Counted
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.OK).body(":)");
    }

    @RequestMapping("/ping")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", getServiceName());
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(response);
    }

    @RequestMapping("")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> healthStatus = new HashMap<>();
        Map<String, Object> checks = new HashMap<>();

        boolean overallHealth = true;

        // Database health check
        if (dataSource != null) {
            Map<String, Object> dbHealth = checkDatabaseHealth();
            checks.put("database", dbHealth);
            overallHealth = overallHealth && "UP".equals(dbHealth.get("status"));
        }

        // Memory health check
        Map<String, Object> memoryHealth = checkMemoryHealth();
        checks.put("memory", memoryHealth);
        overallHealth = overallHealth && "UP".equals(memoryHealth.get("status"));

        // Disk space health check
        Map<String, Object> diskHealth = checkDiskHealth();
        checks.put("diskSpace", diskHealth);
        overallHealth = overallHealth && "UP".equals(diskHealth.get("status"));

        // Custom health indicators
        for (HealthIndicator indicator : healthIndicators) {
            Map<String, Object> customHealth = indicator.health();
            checks.put(indicator.getName(), customHealth);
            overallHealth = overallHealth && "UP".equals(customHealth.get("status"));
        }

        healthStatus.put("status", overallHealth ? "UP" : "DOWN");
        healthStatus.put("service", getServiceName());
        healthStatus.put("timestamp", Instant.now().toString());
        healthStatus.put("checks", checks);

        HttpStatus httpStatus = overallHealth ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(httpStatus).body(healthStatus);
    }

    private Map<String, Object> checkDatabaseHealth() {
        Map<String, Object> health = new HashMap<>();
        try {
            Connection connection = dataSource.getConnection();
            boolean isValid = connection.isValid(5); // 5 seconds timeout
            connection.close();

            health.put("status", isValid ? "UP" : "DOWN");
            health.put("database", "Connected");
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
        }
        return health;
    }

    private Map<String, Object> checkMemoryHealth() {
        Map<String, Object> health = new HashMap<>();

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        double memoryUsagePercent = (double) usedMemory / maxMemory * 100;

        health.put("status", memoryUsagePercent < 90 ? "UP" : "DOWN");
        health.put("used", usedMemory);
        health.put("free", freeMemory);
        health.put("total", totalMemory);
        health.put("max", maxMemory);
        health.put("usagePercent", Math.round(memoryUsagePercent * 100.0) / 100.0);

        return health;
    }

    private Map<String, Object> checkDiskHealth() {
        Map<String, Object> health = new HashMap<>();

        try {
            File root = new File("/");
            long totalSpace = root.getTotalSpace();
            long freeSpace = root.getFreeSpace();
            long usedSpace = totalSpace - freeSpace;

            double diskUsagePercent = (double) usedSpace / totalSpace * 100;

            health.put("status", diskUsagePercent < 95 ? "UP" : "DOWN");
            health.put("total", totalSpace);
            health.put("free", freeSpace);
            health.put("used", usedSpace);
            health.put("usagePercent", Math.round(diskUsagePercent * 100.0) / 100.0);
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
        }

        return health;
    }

    private String getServiceName() {
        // Możesz ustawić to przez properties lub environment variable
        return System.getProperty("service.name", "unknown-service");
    }
}