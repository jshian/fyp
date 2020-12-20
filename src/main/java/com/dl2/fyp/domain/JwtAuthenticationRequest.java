package com.dl2.fyp.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = 8420107889454644763L;
    private String username;
    private String password;


    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }
}
