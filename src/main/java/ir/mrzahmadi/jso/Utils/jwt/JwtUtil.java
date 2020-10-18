package ir.mrzahmadi.jso.Utils.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class JwtUtil {

    private final String SECRET = "MY_SECRET_KEY";

    public String generateToken(String phoneNumber, long expiration) {
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setExpiration(new Date(expiration))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public long generateExpirationDate() {
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.MONTH, 6);
        Date sixMonthLater = c.getTime();
        return sixMonthLater.getTime();
    }

    public String getPhoneNumber(String token) {
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Date getExpiration(String token) {
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().getExpiration();
    }

    public long getExpirationByTime(String token) {
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().getExpiration().getTime();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpiration(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getPhoneNumber(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
