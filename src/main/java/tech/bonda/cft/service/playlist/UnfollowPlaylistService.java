package tech.bonda.cft.service.playlist;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;

import static tech.bonda.cft.util.ApiUtil.getSpotifyApi;

@Service
@RequiredArgsConstructor
public class UnfollowPlaylistService {
    public void deletePlaylist(String userId, String playlistId) {
        SpotifyApi spotifyApi = getSpotifyApi(userId);

        try {
            spotifyApi
                    .unfollowPlaylist(playlistId)
                    .build()
                    .execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to delete playlist", e);
        }
    }
}
