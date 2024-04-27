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

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpotifyLoginService {

    @Value("${spotify.redirect.uri}")
    private String redirectUri;
    @Value("${spotify.client.id}")
    private String clientId;
    @Value("${spotify.client.secret}")
    private String clientSecret;
    private SpotifyApi spotifyApi;

    public Map<String, String> loginUri() {
        final URI uri = spotifyApi.authorizationCodeUri()
//          .state("x4xkmn9pu3j6ukrs8n")
                .scope("playlist-modify-private, playlist-modify-public, user-library-modify, playlist-read-private, user-read-private, user-read-email")
                .show_dialog(true)
                .build()
                .execute();

        return Map.of("uri", uri.toString());
    }

    @PostConstruct
    public void init() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(SpotifyHttpManager.makeUri(redirectUri))
                .build();
    }

    public Map<String, String> authorization(String code) {
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = spotifyApi.authorizationCode(code)
                    .build()
                    .execute();

            return Map.of("token", authorizationCodeCredentials.getAccessToken(),
                    "refreshToken", authorizationCodeCredentials.getRefreshToken(),
                    "expiresIn", String.valueOf(authorizationCodeCredentials.getExpiresIn()));
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }
}
