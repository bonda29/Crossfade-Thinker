package tech.bonda.cft.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.logging.Logger;

@Getter
@Service
public class AccessTokenService {
    Logger logger = Logger.getLogger(AccessTokenService.class.getName());

    @Value("${spotify.client.id}")
    private String clientId;
    @Value("${spotify.client.secret}")
    private String clientSecret;
    @Getter
    private SpotifyApi spotifyApi;

    @PostConstruct
    public void init() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
        refreshToken();
    }

    public String getAccessToken() {
        try {
            String accessToken = spotifyApi.clientCredentials().build()
                    .execute()
                    .getAccessToken();
            spotifyApi.setAccessToken(accessToken);
            return accessToken;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }

    @Scheduled(fixedRate = 50 * 60 * 1000) // 50 minutes in milliseconds
    public void refreshToken() {
        try {
            String newAccessToken = spotifyApi.clientCredentials().build()
                    .execute()
                    .getAccessToken();
            spotifyApi.setAccessToken(newAccessToken);
            logger.info("Access token refreshed at " + System.currentTimeMillis() + " to " + spotifyApi.getAccessToken());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }

}