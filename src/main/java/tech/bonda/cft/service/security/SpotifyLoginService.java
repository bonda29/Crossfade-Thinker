package tech.bonda.cft.service.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import tech.bonda.cft.models.User;
import tech.bonda.cft.service.UserService;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpotifyLoginService {

    private final UserService userService;
    @Value("${spotify.redirect.uri}")
    private String redirectUri;
    @Value("${spotify.client.id}")
    private String clientId;
    @Value("${spotify.client.secret}")
    private String clientSecret;
    private SpotifyApi spotifyApi;

    @PostConstruct
    public void init() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(SpotifyHttpManager.makeUri(redirectUri))
                .build();
    }

    public String loginUri() {
        final URI uri = spotifyApi.authorizationCodeUri()
//          .state("x4xkmn9pu3j6ukrs8n")
                .scope("""
                            playlist-modify-private,
                            playlist-modify-public,
                            playlist-read-private,
                            user-library-modify,
                            user-read-private,
                            user-read-email
                        """)
                .show_dialog(true)
                .build()
                .execute();

        return uri.toString();
    }

    public Map<String, String> authorization(String code) {
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = spotifyApi.authorizationCode(code)
                    .build()
                    .execute();

            String accessToken = authorizationCodeCredentials.getAccessToken();
            String refreshToken = authorizationCodeCredentials.getRefreshToken();

            User user = userService.getOrCreateUserByTokens(accessToken, refreshToken);

            return Map.of("user_id", user.getId(),
                    "access_token", user.getAccessToken(),
                    "refresh_token", user.getRefreshToken());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }
}
