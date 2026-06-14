package com.github.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailResponse {

    private UserResponse localUser;
    private RemoteUserSnapshot remoteUser;
    private Long activeUserCount;
}
