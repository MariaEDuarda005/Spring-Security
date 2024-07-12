package com.example.demo.doman.user;

public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private String role;

    // criar um construtor que recebe uma string que é o role
    UserRole(String role){
        this.role = role;
    }

    // pegar o que o enum representa, e retorna a role que representa
    public String getRole(){
        return role;
    }
}
