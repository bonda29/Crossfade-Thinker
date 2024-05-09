package tech.bonda.cft.service.analysis;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import tech.bonda.cft.models.AudioAnalysisEntity;
import tech.bonda.cft.models.payload.dto.PlaylistAnalysisDto;
import tech.bonda.cft.repositories.AudioAnalysisEntityRepository;
import tech.bonda.cft.service.PlaylistFacade;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static tech.bonda.cft.util.ApiUtil.getSpotifyApi;
import static tech.bonda.cft.util.PlaylistUtil.getTrackIds;

@Service
@RequiredArgsConstructor
public class AudioAnalysisService {
    private final AudioAnalysisEntityRepository audioAnalysisEntityRepository;
    private final PlaylistFacade playlistFacade;

    public PlaylistAnalysisDto getAudioAnalysisForPlaylist(String userId, String playlistId) {
        Playlist playlist = playlistFacade.getPlaylist(userId, playlistId);

        List<String> trackIds = getTrackIds(playlist);

        Map<String, AudioAnalysis> audioAnalysisMap = getAudioAnalysisForTracks(userId, trackIds);

        return new PlaylistAnalysisDto(playlist, audioAnalysisMap);

    }

    public synchronized AudioAnalysis getAudioAnalysis(String userId, String trackId) {
        try {
            var analysis = getSpotifyApi(userId).getAudioAnalysisForTrack(trackId)
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

    private Map<String, AudioAnalysis> getAudioAnalysisForTracks(String userId, List<String> trackIds) {
        // Retrieve all AudioAnalysisEntity objects from the database that have a track ID in the provided list
        List<AudioAnalysisEntity> audioAnalysisEntities = audioAnalysisEntityRepository.findAllById(trackIds);

        // Convert the retrieved AudioAnalysisEntity objects to AudioAnalysis objects
        Map<String, AudioAnalysis> audioAnalysisMap = new ConcurrentHashMap<>();
        Gson gson = new Gson();
        for (AudioAnalysisEntity entity : audioAnalysisEntities) {
            AudioAnalysis audioAnalysis = gson.fromJson(entity.getAudioAnalysisJson(), AudioAnalysis.class);
            audioAnalysisMap.put(entity.getTrackId(), audioAnalysis);
            trackIds.remove(entity.getTrackId());
        }

        // Get the audio analysis for the remaining tracks
        for (String trackId : trackIds) {
            AudioAnalysis audioAnalysis = getAudioAnalysis(userId, trackId);
            audioAnalysisMap.put(trackId, audioAnalysis);

            // Save the new AudioAnalysisEntity object to the database
            AudioAnalysisEntity entity = new AudioAnalysisEntity(trackId, gson.toJson(audioAnalysis));
            audioAnalysisEntityRepository.save(entity);
        }

        return audioAnalysisMap;
    }
}