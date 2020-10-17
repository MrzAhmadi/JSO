package ir.mrzahmadi.jso.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;

@Service
public class JwtUtil {

    private final String SECRET = "MY_SECRET_KEY";

    public String generateToken(String phoneNumber){
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setExpiration(new Date(System.currentTimeMillis() + (60*60*24 * 1000)))
                .signWith(SignatureAlgorithm.HS256,SECRET)
                .compact();
    }

}
