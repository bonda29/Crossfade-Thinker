package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
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
    public Map<String, String> login() {
        return spotifyLoginService.loginUri();
    }

    @GetMapping("/callback")
    public Map<String, String> callback(@RequestParam String code) {
        return spotifyLoginService.authorization(code);
    }
}
