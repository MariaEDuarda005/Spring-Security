package com.example.demo.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.doman.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        // autenticação é STATELESS, não guarda em nenhum lugar as seções que estão ativas, guarda as informações no token que vai ficar transitando entre o cliente e servidor
        try {
            // algoritmo de geração de token que vai utilizar
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api") // emissor do token, pode ser qualquer nome para identificar a aplicação
                    .withSubject(user.getLogin()) // usuario que vai receber o token
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm); // fazer a assinatura e a geração final

            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generation token", exception);
        }
    }

    // metodo para validar se o token está valido
    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api") // quem foi o emissor
                    .build()
                    .verify(token) // descriptografou o token
                    .getSubject(); // pegou o subject que já tinha salvo
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant generateExpirationDate(){
        // pegou o tempo e adicionou 2 horas, e colocou no time zone de brasilia
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
