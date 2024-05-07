package tech.bonda.cft.util;

import se.michaelthelin.spotify.SpotifyApi;
import tech.bonda.cft.repositories.UserRepository;

import static tech.bonda.cft.config.ContextProvider.getApplicationContext;

public class ApiUtil {
//    private static SpotifyApi spotifyApi;

    private ApiUtil() {
    }

//    public static SpotifyApi getSpotifyApi() {
//        if (spotifyApi == null) {
//            AccessTokenService accessTokenService = getApplicationContext().getBean(AccessTokenService.class);
//            spotifyApi = accessTokenService.getSpotifyApi();
//        }
//
//        return spotifyApi;
//    }

    public static SpotifyApi getSpotifyApi(String userId) {
        UserRepository userRepository = getApplicationContext().getBean(UserRepository.class);
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return new SpotifyApi.Builder()
                .setAccessToken(user.getAccessToken())
                .build();
    }
}