package com.eventx.api.controller;

import com.eventx.api.dto.ChangePasswordRequestDto;
import com.eventx.api.dto.LoginRequestDto;
import com.eventx.api.dto.LoginResponseDto;
import com.eventx.api.dto.UpdateProfileRequestDto;
import com.eventx.api.dto.UserRequestDto;
import com.eventx.api.dto.UserResponseDto;
import com.eventx.api.dto.UsernameLoginRequestDto;
import com.eventx.api.service.AuthService;
import com.eventx.api.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/utenti")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll(@RequestParam(required = false) String cerca) {
        return ResponseEntity.ok(userService.findAll(cerca));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.loginByEmail(request));
    }

    @PostMapping("/login/username")
    public ResponseEntity<LoginResponseDto> loginByUsername(@Valid @RequestBody UsernameLoginRequestDto request) {
        return ResponseEntity.ok(authService.loginByUsername(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        userService.deleteById(id);
    }

    @PostMapping("/profilo")
    public ResponseEntity<UserResponseDto> updateProfile(@Valid @RequestBody UpdateProfileRequestDto request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    @PatchMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@PathVariable String id,
                               @Valid @RequestBody ChangePasswordRequestDto request) {
        userService.changePassword(id, request);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> findByEmail(@PathVariable @Email(message = "email non è un indirizzo valido") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }
}



