package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import tech.bonda.cft.models.payload.dto.PlaylistFeaturesDto;
import tech.bonda.cft.service.analysis.AudioFeaturesService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audio-features")
public class AudioFeaturesController {
    private final AudioFeaturesService audioFeaturesService;

    @GetMapping("/track")
    public ResponseEntity<AudioFeatures> getAudioFeatures(@RequestParam(name = "user_id") String userId,
                                                          @RequestParam(name = "track_id") String trackId) {
        return ResponseEntity.ok(audioFeaturesService.getAudioFeatures(userId, trackId));
    }

    @GetMapping("/playlist")
    public ResponseEntity<PlaylistFeaturesDto> getAudioFeaturesForTracks(@RequestParam(name = "user_id") String userId,
                                                                         @RequestParam(name = "playlist_id") String playlistId) {
        return ResponseEntity.ok(audioFeaturesService.getAudioFeaturesForPlaylist(userId, playlistId));
    }
}