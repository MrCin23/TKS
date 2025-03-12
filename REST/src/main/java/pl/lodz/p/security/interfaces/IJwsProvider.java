package pl.lodz.p.security.interfaces;

public interface IJwsProvider {
    String generateJws(String uuid, String username);
    boolean validateJws(String jws, String uuid, String username);
}
