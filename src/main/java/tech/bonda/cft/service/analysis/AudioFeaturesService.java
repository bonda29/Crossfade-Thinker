package tech.bonda.cft.service.analysis;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import tech.bonda.cft.models.AudioFeaturesEntity;
import tech.bonda.cft.models.payload.dto.PlaylistFeaturesDto;
import tech.bonda.cft.repositories.AudioFeaturesEntityRepository;
import tech.bonda.cft.service.PlaylistFacade;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static tech.bonda.cft.util.ApiUtil.getSpotifyApi;
import static tech.bonda.cft.util.PlaylistUtil.getTrackIds;

@Service
@RequiredArgsConstructor
public class AudioFeaturesService {
    private final AudioFeaturesEntityRepository audioFeaturesEntityRepository;
    private final PlaylistFacade playlistFacade;

    public PlaylistFeaturesDto getAudioFeaturesForPlaylist(String userId, String playlistId) {
        Playlist playlist = playlistFacade.getPlaylist(userId, playlistId);

        List<String> trackIds = getTrackIds(playlist);

        Map<String, AudioFeatures> audioFeaturesMap = getAudioFeaturesForTracks(userId, trackIds);

        return new PlaylistFeaturesDto(playlist, audioFeaturesMap);
    }

    public synchronized AudioFeatures getAudioFeatures(String userId, String trackId) {
        try {
            return getSpotifyApi(userId).getAudioFeaturesForTrack(trackId)
                    .build()
                    .execute();

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Failed to get audio features for track: " + trackId, e);
        }
    }

    private Map<String, AudioFeatures> getAudioFeaturesForTracks(String userId, List<String> trackIds) {
        // Retrieve all AudioFeaturesEntity objects from the database that have a track ID in the provided list
        List<AudioFeaturesEntity> audioFeaturesEntities = audioFeaturesEntityRepository.findAllById(trackIds);

        // Convert the retrieved AudioFeaturesEntity objects to AudioFeatures objects
        Map<String, AudioFeatures> audioFeaturesMap = new ConcurrentHashMap<>();
        Gson gson = new Gson();
        for (AudioFeaturesEntity entity : audioFeaturesEntities) {
            AudioFeatures audioFeatures = gson.fromJson(entity.getAudioFeaturesJson(), AudioFeatures.class);
            audioFeaturesMap.put(entity.getTrackId(), audioFeatures);
            trackIds.remove(entity.getTrackId());
        }

        // Get the audio features for the remaining tracks
        for (String trackId : trackIds) {
            AudioFeatures audioFeatures = getAudioFeatures(userId, trackId);
            audioFeaturesMap.put(trackId, audioFeatures);

            // Save the new AudioFeaturesEntity object to the database
            AudioFeaturesEntity entity = new AudioFeaturesEntity(trackId, gson.toJson(audioFeatures));
            audioFeaturesEntityRepository.save(entity);
        }

        return audioFeaturesMap;
    }
}