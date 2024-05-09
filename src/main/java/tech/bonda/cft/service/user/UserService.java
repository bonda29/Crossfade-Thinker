package tech.bonda.cft.service.user;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Image;
import tech.bonda.cft.models.User;
import tech.bonda.cft.repositories.UserRepository;

import java.io.IOException;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByAccessToken(String accessToken) {
        return userRepository.findByAccessToken(accessToken).orElse(null);
    }

    public User getOrCreateUserByTokens(String accessToken, String refreshToken) {
        var spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        se.michaelthelin.spotify.model_objects.specification.User spotifyUser = null;
        try {
            spotifyUser = spotifyApi.getCurrentUsersProfile()
                    .build()
                    .execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get user", e);
        }

        var finalSpotifyUser = spotifyUser;
        User user = userRepository.findById(spotifyUser.getId()).orElseGet(() -> {
            User newUser = new User();
            newUser.setId(finalSpotifyUser.getId());
            return newUser;
        });

        user.setUsername(spotifyUser.getDisplayName());
        user.setEmail(spotifyUser.getEmail());
        user.setTotalFollowers(spotifyUser.getFollowers().getTotal());
        user.setImages(Arrays.stream(spotifyUser.getImages())
                .map(Image::getUrl)
                .collect(toList()));
        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);

        return userRepository.save(user);
    }
}
