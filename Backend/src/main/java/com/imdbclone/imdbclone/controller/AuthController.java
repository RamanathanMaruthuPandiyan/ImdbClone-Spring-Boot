package com.imdbclone.imdbclone.controller;

import com.imdbclone.imdbclone.component.KeycloakClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private KeycloakClient keycloakClient;

    @GetMapping("/config")
    public ResponseEntity<KeycloakClient>getAuthConfig(){
        return ResponseEntity.ok(keycloakClient);
    }

}
