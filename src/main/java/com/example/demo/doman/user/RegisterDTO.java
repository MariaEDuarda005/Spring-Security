package com.example.demo.doman.user;

import lombok.AllArgsConstructor;

public record RegisterDTO(String login, String password, UserRole role) {

}
