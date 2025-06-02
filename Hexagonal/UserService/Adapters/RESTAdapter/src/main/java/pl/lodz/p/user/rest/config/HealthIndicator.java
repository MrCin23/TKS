package pl.lodz.p.user.rest.config;

import java.util.Map;

public interface HealthIndicator {
    /**
     * Sprawdza stan zdrowia komponentu
     * @return Mapa z informacjami o stanie zdrowia
     */
    Map<String, Object> health();

    /**
     * Zwraca nazwę wskaźnika zdrowia
     * @return Nazwa wskaźnika
     */
    String getName();
}
