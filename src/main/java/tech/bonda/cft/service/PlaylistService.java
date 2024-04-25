package tech.bonda.cft.service;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import tech.bonda.cft.util.ApiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

/*
    public List<PlaylistTrack> getAllPlaylistTracks(String playlistId) {
        try {
            SpotifyApi spotifyApi = ApiUtil.getSpotifyApi();
            List<PlaylistTrack> allTracks = new ArrayList<>();
            int limit = 100;
            int offset = 0;
            se.michaelthelin.spotify.model_objects.specification.Paging<PlaylistTrack> paging;

            do {
                paging = spotifyApi.getPlaylistsTracks(playlistId)
                        .limit(limit)
                        .offset(offset)
                        .build()
                        .execute();

                allTracks.addAll(Arrays.asList(paging.getItems()));
                offset += limit;
            } while (offset < paging.getTotal());

            return allTracks;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get all tracks for playlist: " + playlistId, e);
        }
    }
*/

}
