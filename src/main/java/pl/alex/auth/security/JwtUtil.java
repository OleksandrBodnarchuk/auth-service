package pl.alex.auth.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import javax.crypto.SecretKey;

final class JwtUtil {

  private static String secretKey;

  JwtUtil(String secretKey) {
    JwtUtil.secretKey = secretKey;
  }

  static TokenResponse prepareTokenResponse(String jwtSubject, Long expiration) {
    Date date = new Date(System.currentTimeMillis() + expiration);
    return new TokenResponse(prepareJwtToken(jwtSubject, date), date.getTime() / 1000);
  }

  private static String prepareJwtToken(String jwtSubject, Date expiration) {
    return Jwts.builder()
        .setClaims(new HashMap<>())
        .setSubject(jwtSubject)
        .setExpiration(expiration)
        .signWith(getEncodedSecret(), SignatureAlgorithm.HS256)
        .compact();
  }

  private static SecretKey getEncodedSecret() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  private record TokenResponse(String token, @JsonProperty("valid_until") Long validUntil) {

  }
}
