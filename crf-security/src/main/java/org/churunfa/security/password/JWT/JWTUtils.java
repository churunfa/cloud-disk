package org.churunfa.security.password.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 */
@Component
public class JWTUtils {

    private static final String secretKey = "eyJyZnQiOmZhbHNlLCJqdGkiOiJmNWRmOTlhNDAwNTg0NGVlYTYxNzU1Y2MzNmFkZmQyOSIsImlzcyI6ImF1dGgiLCJzdWIiOiJkNzQ2M2U1ZjU2ZjY0MGI1OTk2OWFhMTk4OTNhNmZhNCIsImF1ZCI6WyIqIl0sImlhdCI6MTYyNTExMjkxNSwibmJmIjoxNjI1MTEyOTE1L";

    public String createJWT(Map<String, Object> map, Date expiration) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuedAt(new Date())
                .setClaims(map)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey);
        return jwtBuilder.compact();
    }

    public String createJWT(Map<String, Object> map, Date expiration, String secretKey) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuedAt(new Date())
                .setClaims(map)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey);
        return jwtBuilder.compact();
    }

    public String createJWT(Map<String, Object> map) {
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 20);
        System.out.println(map);
        JwtBuilder jwtBuilder = Jwts.builder()
                .setHeaderParam("type","JWT")
                .setIssuedAt(new Date())
                .setClaims(map)
                .claim("iat", new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secretKey);
        return jwtBuilder.compact();
    }

    public String createJWT(Map<String, Object> map, String secretKey) {
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 60 * 15);
        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuedAt(new Date())
                .setClaims(map)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secretKey);
        return jwtBuilder.compact();
    }

    public Claims verifyJwt(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims verifyJwt(String token, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}