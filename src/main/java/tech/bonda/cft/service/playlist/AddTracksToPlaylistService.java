package tech.bonda.cft.service.playlist;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static tech.bonda.cft.util.ApiUtil.getSpotifyApi;

/**
 * Service for adding tracks to a Spotify playlist.
 * This service handles the addition of tracks in chunks due to Spotify's API limitations on the number of tracks
 * that can be added in a single request.
 */
@Service
@RequiredArgsConstructor
public class AddTracksToPlaylistService {
    // Defines the maximum number of track URIs that can be sent in a single API call to Spotify.
    private static final int TRACK_URIS_CHUNK_SIZE = 90;

    /**
     * Adds an array of track URIs to a specified playlist.
     *
     * @param userId     The ID of the user who owns the playlist.
     * @param playlistId The ID of the playlist to which tracks will be added.
     * @param trackUris  An array of Spotify track URIs to add to the playlist.
     */
    public void addTracksToPlaylist(String userId, String playlistId, String[] trackUris) {
        SpotifyApi spotifyApi = getSpotifyApi(userId);

        // Split the array of track URIs into smaller arrays to comply with API limitations.
        List<String[]> trackUrisList = splitArray(trackUris);

        int position = 0; // Keeps track of the position where tracks should be added in the playlist.
        try {
            for (String[] trackUrisChunk : trackUrisList) {
                spotifyApi
                        .addItemsToPlaylist(playlistId, trackUrisChunk)
                        .position(position)
                        .build()
                        .execute();

                // Update the position for the next chunk of tracks.
                position += trackUrisChunk.length;
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to add tracks to playlist", e);
        }
    }

    /**
     * Splits a large array of track URIs into smaller arrays, each of a fixed maximum length.
     *
     * @param array The original array of track URIs.
     * @return A list of smaller arrays, each containing a portion of the original array's elements.
     */
    private List<String[]> splitArray(String[] array) {
        // Calculate the number of chunks needed.
        int numOfChunks = (int) Math.ceil((double) array.length / TRACK_URIS_CHUNK_SIZE);
        List<String[]> arrayOfArrays = new ArrayList<>(numOfChunks);

        for (int i = 0; i < numOfChunks; i++) {
            int start = i * TRACK_URIS_CHUNK_SIZE;
            int length = Math.min(array.length - start, TRACK_URIS_CHUNK_SIZE);

            String[] temp = new String[length];
            System.arraycopy(array, start, temp, 0, length);
            arrayOfArrays.add(temp);
        }

        return arrayOfArrays;
    }

}
