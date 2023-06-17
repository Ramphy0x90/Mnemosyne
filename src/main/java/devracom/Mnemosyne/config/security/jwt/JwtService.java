package devracom.Mnemosyne.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "46294A404E635266556A586E5A7234753778214125442A472D4B6150645367566B59703373357638792F423F4528482B4D6251655468576D5A7134743777397A";

    public String generateToken(UserDetails userDetails) {
        Date creationDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 24);

        return Jwts.builder()
                .claim("authorities", userDetails.getAuthorities())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(creationDate)
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        String username = getUsername(jwtToken);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
    }

    public boolean isTokenExpired(String jwtToken) {
        return getExpiration(jwtToken).before(new Date());
    }

    public String getUsername(String jwtToken) {
        return getClaim(jwtToken, Claims::getSubject);
    }

    public Date getExpiration(String jwtToken) {
        return getClaim(jwtToken, Claims::getExpiration);
    }

    private Claims getClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private <T> T getClaim(String jwtToken, Function<Claims, T> claimResolver) {
        Claims claims = getClaims(jwtToken);
        return claimResolver.apply(claims);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
