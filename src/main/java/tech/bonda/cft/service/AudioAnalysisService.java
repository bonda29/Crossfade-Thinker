package tech.bonda.cft.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import tech.bonda.cft.util.ApiUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static java.util.stream.Collectors.toConcurrentMap;
import static tech.bonda.cft.util.ApiUtil.getSpotifyApi;

@Service
@RequiredArgsConstructor
public class AudioAnalysisService {
    //    private final Logger logger = Logger.getLogger(AudioAnalysisService.class.getName());
    private final AccessTokenService accessTokenService;
    private final WebClient webClient;
//    private SpotifyApi spotifyApi;
//
//    @PostConstruct
//    public void init() {
//        spotifyApi = new SpotifyApi.Builder()
//                .setAccessToken(accessTokenService.getAccessToken())
//                .build();
//    }

    public Map<String, AudioAnalysis> getAudioAnalysisForTracks(String playlistId) {
        List<String> trackIds = getTrackIds(playlistId);

        return getAudioAnalysis(trackIds);
    }

    public List<String> getTrackIds(String playlistId) {
        String token = accessTokenService.getAccessToken();
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "?fields=tracks(items(track(id))";

        String response = webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        JsonArray itemsArray = jsonObject.getAsJsonObject("tracks").getAsJsonArray("items");

        List<String> tracks = new ArrayList<>();

        for (int i = 0; i < itemsArray.size(); i++) {
            JsonObject trackObject = itemsArray.get(i).getAsJsonObject().getAsJsonObject("track");
            String id = trackObject.get("id").getAsString();

            tracks.add(id);
        }

        return tracks;
    }

    public synchronized AudioAnalysis getAudioAnalysis(String trackId) {
        try {
            var analysis = getSpotifyApi().getAudioAnalysisForTrack(trackId)
                    .build()
                    .execute();

            // Clear the segments field to reduce the size of the response
            Field segmentsField = AudioAnalysis.class.getDeclaredField("segments");
            segmentsField.setAccessible(true);
            segmentsField.set(analysis, null);

            return analysis;

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get audio analysis for track: " + trackId, e);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to clear segments field ", e);
        }
    }

    public Map<String, AudioAnalysis> getAudioAnalysis(List<String> trackIds) {
        ForkJoinPool customThreadPool = new ForkJoinPool(10); // Customize the level of parallelism

        Map<String, AudioAnalysis> result;
        try {
            result = customThreadPool.submit(() ->
                    trackIds.parallelStream()
                            .collect(toConcurrentMap(
                                    trackId -> trackId,
                                    this::getAudioAnalysis,

                                    // In case of key collision, keep the existing value
                                    (existing, replacement) -> existing
                            ))
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get audio analysis for tracks", e);
        }

        return result;
    }
}