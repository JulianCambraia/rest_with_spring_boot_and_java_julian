package br.com.juliancambraia.controller;

import br.com.juliancambraia.data.vo.security.AccountCredentialVO;
import br.com.juliancambraia.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {
    public static final String INVALID_CLIENT_REQUEST = "Invalid client request!";
    @Autowired
    AuthServices authServices;

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping(value = "/signin")
    public ResponseEntity signin(@RequestBody AccountCredentialVO data) {
        if (checkIfParamsIsNotNull(data)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_CLIENT_REQUEST);
        }
        var token = authServices.signin(data);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_CLIENT_REQUEST);
        }
        return token;
    }

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {

        if (checkIfParamsIsNotNull(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_CLIENT_REQUEST);
        }
        var token = authServices.refreshToken(username, refreshToken);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_CLIENT_REQUEST);
        }
        return token;
    }

    private static boolean checkIfParamsIsNotNull(String username, String refreshToken) {
        return refreshToken == null || refreshToken.isBlank()
                || username == null || username.isBlank();
    }

    private static boolean checkIfParamsIsNotNull(AccountCredentialVO data) {
        return data == null || data.getUsername() == null || data.getUsername().isBlank()
                || data.getPassword() == null || data.getPassword().isBlank();
    }
}
