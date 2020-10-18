package ir.mrzahmadi.jso.Utils.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Service
public class JwtUtil {

    private final String SECRET = "MY_SECRET_KEY";

    private ArrayList<String> ignoreJwtUrls = new ArrayList<>();

    public JwtUtil() {
        initSkipUrls();
    }

    private void initSkipUrls() {
        ignoreJwtUrls.add("/api/auth/login");
        ignoreJwtUrls.add("/api/auth/verify-otp");
        ignoreJwtUrls.add("/api/auth/refresh-token");
    }

    public String generateAccessToken(String phoneNumber, long expiration) {
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setExpiration(new Date(expiration))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String generateRefreshToken(String phoneNumber, long expiration) {
        String exString = String.valueOf(expiration);
        String audience = exString.substring(0,exString.length()-2);
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setAudience(audience)
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public long generateAccessTokenExpirationDate() {
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

    public String getAudience(String token) {
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().getAudience();
    }

    public Boolean validateAccessToken(String token, UserDetails userDetails) {
        final String username = getPhoneNumber(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean validateRefreshToken(String token, long expiration) {
        String exString = String.valueOf(expiration);
        String validAudience = exString.substring(0,exString.length()-2);
        String audience = getAudience(token);
        return audience.equals(validAudience);
    }

    public ArrayList<String> getIgnoreJwtUrls() {
        return ignoreJwtUrls;
    }
}
