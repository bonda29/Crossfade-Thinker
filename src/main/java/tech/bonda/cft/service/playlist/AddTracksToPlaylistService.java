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

@Service
@RequiredArgsConstructor
public class AddTracksToPlaylistService {
    private static final int TRACK_URIS_CHUNK_SIZE = 90;

    public void addTracksToPlaylist(String userId, String playlistId, String[] trackUris) {
        SpotifyApi spotifyApi = getSpotifyApi(userId);

        List<String[]> trackUrisList = splitArray(trackUris);

        int position = 0;
        try {
            for (String[] trackUrisChunk : trackUrisList) {
                spotifyApi
                        .addItemsToPlaylist(playlistId, trackUrisChunk)
                        .position(position)
                        .build()
                        .execute();

                position += trackUrisChunk.length;
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to add tracks to playlist", e);
        }
    }

    private List<String[]> splitArray(String[] array) {
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
