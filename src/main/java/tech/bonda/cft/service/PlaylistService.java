package tech.bonda.cft.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import tech.bonda.cft.util.ApiUtil;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final AccessTokenService accessTokenService;
//    private SpotifyApi spotifyApi;
//
//    @PostConstruct
//    public void init() {
//        spotifyApi = new SpotifyApi.Builder()
//                .setAccessToken(accessTokenService.getAccessToken())
//                .build();
//    }

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
