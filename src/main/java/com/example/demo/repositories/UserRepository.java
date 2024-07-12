package com.example.demo.repositories;

import com.example.demo.doman.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    // ele entende que tem que fazer uma query na coluna login com o parametro passado
    UserDetails findByLogin(String login); // importante colocar que ele vai ser um userDetais pois vai ser retornado pelo spring security depois
}
