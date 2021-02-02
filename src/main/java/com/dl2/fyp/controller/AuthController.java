package com.dl2.fyp.controller;

import com.dl2.fyp.domain.JwtAuthenticationRequest;
import com.dl2.fyp.domain.JwtAuthenticationResponse;
import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.service.AuthService;
import com.dl2.fyp.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("${jwt.route.authentication.path}")
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHeadString;

    @Autowired
    private AuthService authService;

    @PostMapping("${jwt.route.authentication.login}")
    public Result createAuthenticationToken(@RequestBody JwtAuthenticationRequest jwtAuthenticationRequest) throws AuthenticationException, IOException {
        final String token = authService.login(jwtAuthenticationRequest.getToken());
        // Return the token
        return ResultUtil.success(new JwtAuthenticationResponse(tokenHeadString+token));
    }

    @GetMapping("${jwt.route.authentication.refresh}")
    public Result refreshAndGetAuthenticationToken(HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResultUtil.error(HttpStatus.BAD_REQUEST,null);
        } else {
            return ResultUtil.success(new JwtAuthenticationResponse(refreshedToken));
        }
    }


    @PostMapping("${jwt.route.authentication.register}")
    public Result register(@RequestBody User user) throws AuthenticationException{
        return ResultUtil.success(authService.register(user));
    }


}
