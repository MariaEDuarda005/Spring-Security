package com.example.demo.controller;

import com.example.demo.doman.user.AuthenticationDTO;
import com.example.demo.doman.user.LoginResponseDTO;
import com.example.demo.doman.user.RegisterDTO;
import com.example.demo.doman.user.User;
import com.example.demo.infra.security.TokenService;
import com.example.demo.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth") // sempre que tiver o endpoint auth vai cair nesse authentication
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login") // http://localhost:8080/auth/login
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        // validar se a senha e o email existem no banco de dados
        // Cria um token de autenticação com os dados fornecidos (login e senha)
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        System.out.println(usernamePassword);
        // Autentica o token usando o AuthenticationManager
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // no login vamos retornar o token para que utilize nas proximas requisições
        // (User)auth.getPrincipal() -  principal é para pegar o objeto principal, quem ele é, e fazer um cash (User) do tipo usuario para que não retorne mais o erro
        var token = tokenService.generateToken((User)auth.getPrincipal());

        // Retorna uma resposta HTTP 200 OK
        return ResponseEntity.ok(new LoginResponseDTO(token));

        // depois de criar as função authenticationManager e passwordEncoder no SecurityConfigurations
        // ele já vai conseguir fazer a verificação da senha do usuario, usando o BCryptPasswordEncoder para criptografar a
        // senha que recebeu por parametro e comparar com o hash que ja tem no banco de dados
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        // primeiro verificar se o login que esta tentando ser cadastrado não existe no banco de dados
        // caso não exista pode realizar o cadastro
        if (this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        // caso não encontre ninguem no banco de dados com esse login
        String encrytedPassword = new BCryptPasswordEncoder().encode(data.password()); // dessa forma pega o hash da senha
        User newUser = new User(data.login(), encrytedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
