package tech.bonda.cft.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import tech.bonda.cft.models.AudioAnalysisEntity;
import tech.bonda.cft.models.payload.dto.PlaylistAnalysisDto;
import tech.bonda.cft.repositories.AudioAnalysisEntityRepository;
import tech.bonda.cft.service.security.AccessTokenService;

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
    private final AudioAnalysisEntityRepository audioAnalysisEntityRepository;
    private final AccessTokenService accessTokenService;
    private final PlaylistService playlistService;
    private final WebClient webClient;


    public PlaylistAnalysisDto getAudioAnalysisForTracks(String playlistId) {
        var playlist = playlistService.getPlaylist(playlistId);
        List<String> trackIds = getTrackIds(playlist);

        Map<String, AudioAnalysis> audioAnalysisMap = getAudioAnalysis(trackIds);

        return new PlaylistAnalysisDto(playlist, audioAnalysisMap);

    }

    public synchronized AudioAnalysis getAudioAnalysis(String trackId, boolean includeSegments) {
        var audioAnalysisEntity = audioAnalysisEntityRepository.findById(trackId);
        if (audioAnalysisEntity.isPresent()) {
            return new Gson().fromJson(audioAnalysisEntity.get().getAudioAnalysisJson(), AudioAnalysis.class);
        }
        try {
            var analysis = getSpotifyApi().getAudioAnalysisForTrack(trackId)
                    .build()
                    .execute();

            if (!includeSegments) {
                // Clear the segments field to reduce the size of the response
                Field segmentsField = AudioAnalysis.class.getDeclaredField("segments");
                segmentsField.setAccessible(true);
                segmentsField.set(analysis, null);
            }

            AudioAnalysisEntity entity = new AudioAnalysisEntity(trackId, new Gson().toJson(analysis));
            audioAnalysisEntityRepository.save(entity);

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
                                    trackId -> getAudioAnalysis(trackId, false),

                                    // In case of key collision, keep the existing value
                                    (existing, replacement) -> existing
                            ))
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get audio analysis for tracks", e);
        }

        return result;
    }

    private List<String> getTrackIds(Playlist playlist) {
        List<String> tracksIds = new ArrayList<>();
        for (var track : playlist.getTracks().getItems()) {
            tracksIds.add(track.getTrack().getId());
        }
        return tracksIds;
    }
}