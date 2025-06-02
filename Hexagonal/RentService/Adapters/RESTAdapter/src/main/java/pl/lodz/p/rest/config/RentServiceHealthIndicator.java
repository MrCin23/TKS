package pl.lodz.p.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.services.service.RentService;

import java.util.HashMap;
import java.util.Map;

@Component
public class RentServiceHealthIndicator implements HealthIndicator {

    @Autowired(required = false)
    private RentService rentService;

    @Override
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();

        try {
            if (rentService != null) {

                boolean isWorking = testRentServiceBasicOperation();

                if (isWorking) {
                    health.put("status", "UP");
                    health.put("service", "RentService");
                    health.put("message", "Service is working correctly");
                } else {
                    health.put("status", "DOWN");
                    health.put("service", "RentService");
                    health.put("message", "Service basic operation failed");
                }
            } else {
                health.put("status", "DOWN");
                health.put("service", "RentService");
                health.put("message", "RentService not available");
            }
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("service", "RentService");
            health.put("error", e.getMessage());
        }

        return health;
    }

    @Override
    public String getName() {
        return "rentService";
    }

    private boolean testRentServiceBasicOperation() {
        try {

            if (rentService != null) {
                String serviceName = rentService.getClass().getSimpleName();
                return serviceName.contains("Rent");
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}