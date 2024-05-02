//package tech.bonda.cft.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import tech.bonda.cft.service.security.AccessTokenService;
//import tech.bonda.cft.service.security.SpotifyLoginService;
//
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/token")
//public class AccessTokenController {
//    private final AccessTokenService accessTokenService;
//    private final SpotifyLoginService spotifyLoginService;
//
//    @GetMapping("/")
//    public String getAccessToken() {
//        return accessTokenService.getAccessToken();
//    }
//
//    @GetMapping("/login")
//    public Map<String, String> login() {
//        return spotifyLoginService.loginUri();
//    }
//
//    @GetMapping("/callback")
//    public Map<String, String> callback(@RequestParam String code) {
//        return spotifyLoginService.authorization(code);
//    }
//}
