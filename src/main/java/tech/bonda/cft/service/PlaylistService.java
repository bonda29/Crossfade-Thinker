package tech.bonda.cft.service;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import tech.bonda.cft.util.ApiUtil;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    public Playlist getPlaylist(String playlistId, String fields) {
        try {
            SpotifyApi spotifyApi = ApiUtil.getSpotifyApi();

            var builder = spotifyApi.getPlaylist(playlistId);
            if (fields != null && !fields.isEmpty()) {
                builder.fields(String.join(",", fields));
            }
            return builder.build().execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get playlist: " + playlistId, e);
        }
    }

}
