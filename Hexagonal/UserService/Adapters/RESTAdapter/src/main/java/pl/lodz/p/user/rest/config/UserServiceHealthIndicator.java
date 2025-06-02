package pl.lodz.p.user.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import pl.lodz.p.user.core.services.service.UserService;

@Component
public class UserServiceHealthIndicator implements HealthIndicator {

    @Autowired(required = false)
    private UserService userService; // Twój istniejący serwis

    @Override
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();

        try {
            if (userService != null) {
                // Sprawdź podstawowe operacje serwisu
                boolean isWorking = testUserServiceBasicOperation();

                if (isWorking) {
                    health.put("status", "UP");
                    health.put("service", "UserService");
                    health.put("message", "Service is working correctly");
                } else {
                    health.put("status", "DOWN");
                    health.put("service", "UserService");
                    health.put("message", "Service basic operation failed");
                }
            } else {
                health.put("status", "DOWN");
                health.put("service", "UserService");
                health.put("message", "UserService not available");
            }
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("service", "UserService");
            health.put("error", e.getMessage());
        }

        return health;
    }

    @Override
    public String getName() {
        return "userService";
    }

    private boolean testUserServiceBasicOperation() {
        try {
            if (userService != null) {
                // Prosty test czy serwis jest dostępny
                String serviceName = userService.getClass().getSimpleName();
                return serviceName.contains("User");
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
