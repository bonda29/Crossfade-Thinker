package tech.bonda.cft.service.playlist;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;

import static tech.bonda.cft.util.ApiUtil.getSpotifyApi;

/**
 * Service for unfollowing (deleting) a Spotify playlist.
 */
@Service
@RequiredArgsConstructor
public class UnfollowPlaylistService {
    /**
     * @param userId     The Spotify user ID.
     * @param playlistId The ID of the playlist to be unfollowed (deleted).
     */
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
