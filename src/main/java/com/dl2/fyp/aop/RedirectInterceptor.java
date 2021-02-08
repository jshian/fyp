package com.dl2.fyp.aop;

import com.dl2.fyp.entity.User;
import com.dl2.fyp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = userService.find(0L);//preserve id=0 for test -> should add the record manually
        request.setAttribute("user",user);
        String uri = request.getRequestURI();
        response.sendRedirect(request.getRequestURI().replace("/auth/admin",""));
        return true;
    }
}
