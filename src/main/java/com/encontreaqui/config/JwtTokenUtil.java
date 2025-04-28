package com.encontreaqui.config;

import io.jsonwebtoken.*;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // Gera um token JWT para o usuário, agora incluindo o id do usuário como claim
    public String generateToken(Long id, String name, String email, String role) {
        return Jwts.builder()
                .setSubject(email)                            // Define o email como subject
                .claim("id", id)                              // Inclui o id do usuário
                .claim("name", name)                          // Adiciona o nome como claim
                .claim("role", role)                          // Adiciona o role como claim
                .setIssuedAt(new Date())                      // Data de emissão
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Data de expiração
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // Assina o token com a chave secreta
                .compact();
    }

    // Extrai o email (subject) do token JWT
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                   .setSigningKey(jwtSecret)
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }
    
    // Extrai o id do usuário a partir do claim "id" do token
    public String getUserIdFromJwtToken(String token) {
        return Jwts.parser()
                   .setSigningKey(jwtSecret)
                   .parseClaimsJws(token)
                   .getBody()
                   .get("id", String.class);
    }

    // Valida o token JWT e retorna se ele é válido ou não
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }
}
