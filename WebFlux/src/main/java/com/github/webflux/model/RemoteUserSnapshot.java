package com.github.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RemoteUserSnapshot {

    private Long id;
    private String username;
    private String email;
}
