package tech.bonda.cft.service;

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
public class PlaylistService {
    private static final int TRACKS_LIMIT = 50;
    private static final int TRACK_URIS_CHUNK_SIZE = 90;

    public Playlist getPlaylist(String userId, String playlistId) {
        SpotifyApi spotifyApi = getSpotifyApi(userId);
        final String fields = "collaborative, description, external_urls, followers(total), href, id, images(url), name, owner(id), public, snapshot_id, type, uri"; //without tracks
        try {
            var builder = spotifyApi
                    .getPlaylist(playlistId)
                    .fields(fields)
                    .build();
            Playlist playlist = builder.execute();
            PlaylistTrack[] trackArr = getPlaylistsItems(userId, playlistId)
                    .toArray(new PlaylistTrack[0]);

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

    private List<PlaylistTrack> getPlaylistsItems(String userId, String playlistId) {
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
        return tracks;
    }

    public Playlist createPlaylist(String userId, String playlistName, String description, boolean isPublic) {
        SpotifyApi spotifyApi = getSpotifyApi(userId);
        try {
            var builder = spotifyApi
                    .createPlaylist(userId, playlistName)
                    .description(description)
                    .public_(isPublic)
                    .build();

            return builder.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to create playlist", e);
        }
    }

    public void addTracksToPlaylist(String userId, String playlistId, String[] trackUris) {
        SpotifyApi spotifyApi = getSpotifyApi(userId);

        List<String[]> trackUrisList = splitArray(trackUris);

        int position = 0;
        try {
            for (String[] trackUrisChunk : trackUrisList) {
                var builder = spotifyApi
                        .addItemsToPlaylist(playlistId, trackUrisChunk)
                        .position(position)
                        .build();

                position += trackUrisChunk.length;

                builder.execute();
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to add tracks to playlist", e);
        }
    }

    private List<String[]> splitArray(String[] array) {
        int numOfChunks = (int) Math.ceil((double) array.length / PlaylistService.TRACK_URIS_CHUNK_SIZE);
        List<String[]> arrayOfArrays = new ArrayList<>(numOfChunks);

        for (int i = 0; i < numOfChunks; i++) {
            int start = i * PlaylistService.TRACK_URIS_CHUNK_SIZE;
            int length = Math.min(array.length - start, PlaylistService.TRACK_URIS_CHUNK_SIZE);

            String[] temp = new String[length];
            System.arraycopy(array, start, temp, 0, length);
            arrayOfArrays.add(temp);
        }

        return arrayOfArrays;
    }
}