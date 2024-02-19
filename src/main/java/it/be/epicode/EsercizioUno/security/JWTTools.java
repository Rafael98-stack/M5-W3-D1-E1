package it.be.epicode.EsercizioUno.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import it.be.epicode.EsercizioUno.entities.User;
//import riccardogulin.u5d11.exceptions.UnauthorizedException;

import java.util.Date;

@Service
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(User user) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // Data di emissione del token (IAT)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Data di scadenza del token (Expiration Date)
                .subject(String.valueOf(user.getId())) // Subject ovvero a chi appartiene il token
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // Firmo il token
                .compact();
    }
    }
