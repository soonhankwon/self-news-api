package com.douunderstandapi.user.controller;

import com.douunderstandapi.common.security.impl.UserDetailsImpl;
import com.douunderstandapi.user.dto.request.UserAddRequest;
import com.douunderstandapi.user.dto.response.UserAddResponse;
import com.douunderstandapi.user.dto.response.UserDeleteResponse;
import com.douunderstandapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PatchMapping("/delete")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        UserDeleteResponse response = userService.deleteUser(email);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
