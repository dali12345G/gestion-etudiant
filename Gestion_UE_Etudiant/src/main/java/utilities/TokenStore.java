package utilities;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Generates, stores and validates JWT tokens for the demo application.
 */
public class TokenStore {

    private static final long EXPIRATION_MILLIS = 30 * 60 * 1000; // 30 minutes
    private static final String SECRET_KEY = "GestionUESecretKey-ChangeMe";

    private static final TokenStore INSTANCE = new TokenStore();
    private final Map<String, TokenInfo> tokens = new ConcurrentHashMap<>();

    private TokenStore() {
    }

    public static TokenStore getInstance() {
        return INSTANCE;
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_MILLIS);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        tokens.put(token, new TokenInfo(username, expiryDate));
        return token;
    }

    public boolean isTokenValid(String token) {
        TokenInfo info = tokens.get(token);
        if (info == null) {
            return false;
        }

        if (info.getExpiration().before(new Date())) {
            tokens.remove(token);
            return false;
        }

        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            tokens.remove(token);
            return false;
        }
    }

    public void invalidate(String token) {
        tokens.remove(token);
    }

    public String getSecretKey() {
        return SECRET_KEY;
    }

    private static class TokenInfo {
        private final String username;
        private final Date expiration;

        private TokenInfo(String username, Date expiration) {
            this.username = username;
            this.expiration = expiration;
        }

        public String getUsername() {
            return username;
        }

        public Date getExpiration() {
            return expiration;
        }
    }
}


