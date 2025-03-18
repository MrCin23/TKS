package pl.lodz.p.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

@Component
@PropertySource("classpath:application.properties")
public class JwsProvider {

    @Value("${app.jws_secret}")
    private String secret;

    public String generateJws(String uuid, String username) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        System.out.println();
        return Jwts.builder()
                .claim("uuid", uuid)
                .claim("username", username)
                .signWith(key).compact();
    }

    public boolean validateJws(String jws, String uuid, String username) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jws.replaceAll("^.|.$", "")).getPayload();
            String uuid1 = claims.get("uuid", String.class);
            String username1 = claims.get("username", String.class);

            System.out.println(uuid + "\n" + uuid1 + "\n" + username + "\n" + username1);

            return uuid.equals(uuid1) && username.equals(username1);
        } catch (Exception e) {
            return false;
        }
    }
}