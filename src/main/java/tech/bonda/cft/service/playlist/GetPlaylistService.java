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

/**
 * Service for retrieving detailed information about a Spotify playlist, including its tracks.
 * Due to Spotify's API limitations, tracks are fetched separately and then set into the Playlist object.
 */
@Service
@RequiredArgsConstructor
public class GetPlaylistService {
    private static final int TRACKS_LIMIT = 50; // Maximum number of tracks fetched per API call.

    /**
     * Retrieves a playlist by its ID, including all tracks within it.
     *
     * @param userId     The Spotify user ID of the requester.
     * @param playlistId The ID of the playlist to retrieve.
     * @return A Playlist object populated with its details and tracks.
     */
    public Playlist getPlaylist(String userId, String playlistId) {
        SpotifyApi spotifyApi = getSpotifyApi(userId);
        final String fields = """
                    collaborative, description,
                    external_urls, followers(total),
                    href, id, images(url),
                    name, owner(id), public,
                    snapshot_id, type, uri
                """; // Excludes tracks to be fetched separately.
        try {
            Playlist playlist = spotifyApi
                    .getPlaylist(playlistId)
                    .fields(fields)
                    .build()
                    .execute();

            PlaylistTrack[] trackArr = getPlaylistsItems(userId, playlistId);

            // Reflectively set the tracks into the playlist due to SDK limitations.
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
