package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bonda.cft.service.GetAccessTokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class GetAccessTokenController {
    private final GetAccessTokenService getAccessTokenService;

    @GetMapping("/")
    public String getAccessToken() {
        return getAccessTokenService.getAccessToken();
    }
}
