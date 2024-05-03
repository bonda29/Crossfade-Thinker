package tech.bonda.cft.service.playlist;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static tech.bonda.cft.util.ApiUtil.getSpotifyApi;

@Service
@RequiredArgsConstructor
public class GetPlaylistService {
    private static final int TRACKS_LIMIT = 50;

    public Playlist getPlaylist(String userId, String playlistId) {
        SpotifyApi spotifyApi = getSpotifyApi(userId);
        final String fields = """
                    collaborative, description,
                    external_urls, followers(total),
                    href, id, images(url),
                    name, owner(id), public,
                    snapshot_id, type, uri
                """; //without tracks
        try {
            Playlist playlist = spotifyApi
                    .getPlaylist(playlistId)
                    .fields(fields)
                    .build()
                    .execute();

            PlaylistTrack[] trackArr = getPlaylistsItems(userId, playlistId);

            setPlaylistTracks(playlist, trackArr);

            return playlist;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get playlist: " + playlistId, e);
        }
    }

    private PlaylistTrack[] getPlaylistsItems(String userId, String playlistId) {
        SpotifyApi spotifyApi = getSpotifyApi(userId);
        List<PlaylistTrack> tracks = new ArrayList<>();
        int offset = 0;

        try {
            while (true) {
                var builder = spotifyApi
                        .getPlaylistsItems(playlistId)
                        .fields("items(track())")
                        .offset(offset)
                        .limit(TRACKS_LIMIT)
                        .build();

                Paging<PlaylistTrack> playlistTrackPaging = builder.execute();
                if (playlistTrackPaging.getItems().length > 0) {
                    tracks.addAll(Arrays.asList(playlistTrackPaging.getItems()));
                    offset += TRACKS_LIMIT;
                } else {
                    break;
                }
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get playlist tracks", e);
        }
        return tracks.toArray(new PlaylistTrack[0]);
    }

    private void setPlaylistTracks(Playlist playlist, PlaylistTrack[] tracks) {
        try {
            Paging<PlaylistTrack> playlistTracks = new Paging.Builder<PlaylistTrack>()
                    .setItems(tracks)
                    .build();

            Field segmentsField = Playlist.class.getDeclaredField("tracks");
            segmentsField.setAccessible(true);
            segmentsField.set(playlist, playlistTracks);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set playlist tracks", e);
        }
    }

}
