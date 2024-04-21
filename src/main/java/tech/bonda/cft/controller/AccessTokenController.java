package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bonda.cft.service.AccessTokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class AccessTokenController {
    private final AccessTokenService accessTokenService;

    @GetMapping("/")
    public String getAccessToken() {
        return accessTokenService.getAccessToken();
    }
}