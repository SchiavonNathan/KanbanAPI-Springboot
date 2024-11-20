package com.example.kanbanJava.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private String secretKey = "seuSecretKey"; // Sua chave secreta

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Define o "sub" (subject) do JWT como o nome do usuário
                .setIssuedAt(new Date()) // Define a data de emissão do token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Define a expiração do token (10 horas)
                .signWith(SignatureAlgorithm.HS256, secretKey) // Assina o token usando o algoritmo HS256
                .compact(); // Cria o token JWT
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Extrai o username do token
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }

    private <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.resolve(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Verifica a assinatura usando a chave secreta
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @FunctionalInterface
    public interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }
}
