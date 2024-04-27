package tech.bonda.cft.util;

import se.michaelthelin.spotify.SpotifyApi;
import tech.bonda.cft.service.security.AccessTokenService;

import static tech.bonda.cft.config.ContextProvider.getApplicationContext;

public class ApiUtil {
    private static SpotifyApi spotifyApi;

    private ApiUtil() {
    }

    public static SpotifyApi getSpotifyApi() {
        if (spotifyApi == null) {
            AccessTokenService accessTokenService = getApplicationContext().getBean(AccessTokenService.class);
            spotifyApi = accessTokenService.getSpotifyApi();
        }

        return spotifyApi;
    }
}