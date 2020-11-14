package com.dl2.fyp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//for test
@RestController
@RequestMapping("/api")
public class MethodProtectedController {

    // only admin can request /protectedadmin
    @RequestMapping(value = "/protectedadmin", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getProtectedAdmin() {
        return ResponseEntity.ok("Greetings from admin protected method!");
    }

    //only user can request /protectedadmin
    @RequestMapping(value = "/protecteduser", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getProtectedUser() {
        return ResponseEntity.ok("Greetings from user protected method!");
    }
}
