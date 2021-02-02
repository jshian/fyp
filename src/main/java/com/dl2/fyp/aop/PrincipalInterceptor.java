package com.dl2.fyp.aop;

import com.dl2.fyp.entity.User;
import com.dl2.fyp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrincipalInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = userService.findByFirebaseUid(request.getUserPrincipal().getName());
        request.setAttribute("user", user);
        if (user == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(),"Can't find user");
            return false;
        }
        response.sendRedirect(request.getRequestURI().replace("/auth",""));
        return true;
    }
}
