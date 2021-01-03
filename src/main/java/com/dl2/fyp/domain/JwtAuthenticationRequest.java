package com.dl2.fyp.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = 8420107889454644763L;
    private String token;


    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String token) {
        this.setToken(token);
    }
}
