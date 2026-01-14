package com.biblioteca_icpi.dto;

public class LoginResponseDTO {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginResponseDTO(String token) {
        this.token = token;
    }
}
