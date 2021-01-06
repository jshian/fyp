package com.dl2.fyp.controller;

import com.dl2.fyp.domain.JwtAuthenticationRequest;
import com.dl2.fyp.domain.JwtAuthenticationResponse;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${jwt.route.authentication.path}")
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHeadString;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "${jwt.route.authentication.login}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest jwtAuthenticationRequest) throws AuthenticationException {
        final String token = authService.login(jwtAuthenticationRequest.getToken());
        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(tokenHeadString+token));
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }

    /**
    @RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
    public User register(@RequestBody User addedUser) throws AuthenticationException{
        return authService.register(addedUser);
    }
    **/

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }


}
