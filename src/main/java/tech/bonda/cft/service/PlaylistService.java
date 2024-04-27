package tech.bonda.cft.service;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import tech.bonda.cft.util.ApiUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    public Playlist getPlaylist(String playlistId) {
        SpotifyApi spotifyApi = ApiUtil.getSpotifyApi();
        final String fields = "collaborative, description, external_urls, followers(total), href, id, images(url), name, owner(id), public, snapshot_id, type, uri";
        try {
            var builder = spotifyApi
                    .getPlaylist(playlistId)
                    .fields(fields)
                    .build();
            Playlist playlist = builder.execute();
            PlaylistTrack[] trackArr = getPlaylistsItems(playlistId).toArray(new PlaylistTrack[0]);

            Paging<PlaylistTrack> tracks = new Paging.Builder<PlaylistTrack>()
                    .setItems(trackArr)
                    .build();

            Field segmentsField = Playlist.class.getDeclaredField("tracks");
            segmentsField.setAccessible(true);
            segmentsField.set(playlist, tracks);

            return playlist;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get playlist: " + playlistId, e);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set playlist tracks", e);
        }
    }

    private List<PlaylistTrack> getPlaylistsItems(String playlistId) {
        SpotifyApi spotifyApi = ApiUtil.getSpotifyApi();
        List<PlaylistTrack> tracks = new ArrayList<>();
        int offset = 0;
        int limit = 50;

        try {
            while (true) {
                var builder = spotifyApi
                        .getPlaylistsItems(playlistId)
                        .fields("items(track())")
                        .offset(offset)
                        .limit(limit)
                        .build();

                Paging<PlaylistTrack> playlistTrackPaging = builder.execute();
                if (playlistTrackPaging.getItems().length > 0) {
                    tracks.addAll(Arrays.asList(playlistTrackPaging.getItems()));
                    offset += limit;
                } else {
                    break;
                }
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return tracks;
    }

}
