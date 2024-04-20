package tech.bonda.cft.service;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

@Service
public class GetAccessTokenService {

    @Value("${spotify.client.id}")
    private String clientId;
    @Value("${spotify.client.secret}")
    private String clientSecret;
    private static ClientCredentialsRequest clientCredentialsRequest;

    @SneakyThrows
    public String getAccessToken() {
        final var clientCredentials = clientCredentialsRequest.execute();

        return clientCredentials.getAccessToken();
    }

    @PostConstruct
    public void init() {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();

        clientCredentialsRequest = spotifyApi.clientCredentials()
                .build();
    }
}
