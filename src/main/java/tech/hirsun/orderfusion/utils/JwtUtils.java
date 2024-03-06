package tech.hirsun.orderfusion.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    // the key used to sign the JWT, must >= 256 bits
    private static String signKey = "NuwhbujHwsvJpwq2peJGkw23ejTmhqoqh2tydkei9izheoo9";
    private static Long expirePeriod = 86400000L; // 24 hours

    /**
     * Generate a JWT token
     * @param claims What is stored in the JWT part 2 load payload
     * @return JWT token
     */
    public static String createJWT(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expirePeriod))
                .signWith(SignatureAlgorithm.HS256, signKey)
                .compact();
        return jwt;
    }

    /**
     * Parse the JWT token
     * @param jwt JWT token
     * @return The payload of the JWT token
     */
    public static Map<String, Object> parseJWT(String jwt) {
        Map<String, Object> claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}
