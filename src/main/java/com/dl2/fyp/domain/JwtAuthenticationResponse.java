package com.dl2.fyp.domain;

import java.io.Serializable;

public class JwtAuthenticationResponse implements Serializable {
    private static final long serialVersionUID = 6912125798411404978L;
    private final String token;   //token return to client-end

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

}
