package com.example.demo.service;

import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    // precisa ter uma instancia do repository para fazer a consulta com o banco de dados
    @Autowired
    UserRepository repository;

    @Override
    // toda vez que alguem tentar entrar na aplicação o spring security vai ter que ter uma forma de consultar essas usuarios
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }
}
