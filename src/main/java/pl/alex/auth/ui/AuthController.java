package pl.alex.auth.ui;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/login")
public class AuthController {

    static final Map<String, String> tokens = new ConcurrentHashMap<>();
    private SecretKey key;

    @PostConstruct
    public void init() {
        String secretKey = "alex-secret:d3f4232d-ff8a-44b7-9a39-7b041a8018de";
        key = Keys.hmacShaKeyFor(secretKey.getBytes());

    }
    @PostMapping
    public String login(@RequestBody AuthRequest authRequest) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("body", authRequest);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setSubject(authRequest.login())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        tokens.put(authRequest.login(), jwt);

        return jwt;

    }
}
