package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.bonda.cft.service.security.SpotifyLoginService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final SpotifyLoginService spotifyLoginService;

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login() {
        String uri = spotifyLoginService.loginUri();
        return ResponseEntity.ok(Map.of("uri", uri));
    }

    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> callback(@RequestParam String code) {
        Map<String, String> response = spotifyLoginService.authorization(code);
        return ResponseEntity.ok(response);
    }
}
