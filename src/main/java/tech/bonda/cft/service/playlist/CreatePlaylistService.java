package tech.bonda.cft.service.playlist;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

import java.io.IOException;

import static tech.bonda.cft.util.ApiUtil.getSpotifyApi;

@Service
@RequiredArgsConstructor
public class CreatePlaylistService {
    public Playlist createPlaylist(String userId, String playlistName, String description, boolean isPublic) {
        SpotifyApi spotifyApi = getSpotifyApi(userId);
        try {
            return spotifyApi
                    .createPlaylist(userId, playlistName)
                    .description(description)
                    .public_(isPublic)
                    .build()
                    .execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to create playlist", e);
        }
    }
}
