package pl.lodz.p.user.core.services.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.core.domain.user.Role;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
@PropertySource("classpath:application.properties")
public class JwtTokenProvider {

    @Value("${app.jwt_secret}")
    private String secret;

    @Value("${app.jwt_expiration}")
    private long expiration;

    public String generateToken(String login, Role role) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .subject(login)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String getLogin(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getRole(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        Jws<Claims> claimsJws = Jwts.parser()//co i czemu to jest deprecated wszystko
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return claimsJws.getBody().get("role", String.class);
    }


    public boolean validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}