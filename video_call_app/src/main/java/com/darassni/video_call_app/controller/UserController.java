package com.darassni.video_call_app.controller;

import com.darassni.video_call_app.model.User;
import com.darassni.video_call_app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;


import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {
    private final UserService userService;
    private String username ;
    private String role ;

    // feat: add token (role) + username


    @GetMapping("/cof")
    public String setUsername(@RequestHeader("Authorization") String authorizationHeader) {
        List<String> userDetails = userService.getEmailFromAuthorizationHeader(authorizationHeader);
        this.username = userDetails.get(0);
        this.role= userDetails.get(1);
        log.info("*********User Email: {}, Roles: {}", this.username, this.role);
        return this.username;
    }


    @GetMapping("/username")
    public UseR getUserInfo() {
        return new UseR(this.username, this.role);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}
