package com.douunderstandapi.user.dto.response;

import com.douunderstandapi.user.domain.User;

public record UserPasswordUpdateResponse(
        Long id,
        String email,
        Boolean isAllowedNotification
) {
    public static UserPasswordUpdateResponse from(User user) {
        return new UserPasswordUpdateResponse(
                user.getId(),
                user.getEmail(),
                user.getIsAllowedNotification());
    }
}
