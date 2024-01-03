package com.douunderstandapi.user.presentation;

import com.douunderstandapi.user.application.UserService;
import com.douunderstandapi.user.domain.dto.request.UserAddRequest;
import com.douunderstandapi.user.domain.dto.response.UserAddResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserAddResponse> addUser(@RequestBody UserAddRequest request) {
        UserAddResponse response = userService.addUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
