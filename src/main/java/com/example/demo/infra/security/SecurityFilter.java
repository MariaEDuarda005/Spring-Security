package com.example.demo.infra.security;

import com.example.demo.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter { // em toda a requisição uma vez eu vou passar esse filtro

    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        System.out.println("TESTE DO TOKEN" + token);
        if (token != null){
            var login = tokenService.validateToken(token);
            UserDetails user = repository.findByLogin(login);

            var authentication =  new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); // todas as informações que o spring security vai precisar para fazer as validações

            // fez a verificação e salvou no contexto da autenticação esse usuario, caso não encontre token não salva nada e so chama o proximo filtro, quando chega no proximo filtro ele vai procurar a role para ver se o usuario vai estar autenticado, não cvai encontrar e retorna o 403
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response); // chamando o proximo filtro
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        // se for igual a null retorna null, não tem nenhum token na requisição
        if (authHeader == null) return null;
        // substitui o Bearar que vai vir sempre nas requisições padroes para o espaço vazio para pegar só o valor do token no final
        return authHeader.replace("Bearar ", "");
    }
}
