package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import tech.bonda.cft.models.payload.dto.PlaylistAnalysisDto;
import tech.bonda.cft.service.AudioAnalysisService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audio-analysis")
public class AudioAnalysisController {
    private final AudioAnalysisService audioAnalysisService;

    @GetMapping("/track")
    public ResponseEntity<AudioAnalysis> getAudioAnalysis(@RequestParam(name = "user_id") String userId,
                                                          @RequestParam(name = "track_id") String trackId) {
        return ResponseEntity.ok(audioAnalysisService.getAudioAnalysis(userId, trackId));
    }

    @GetMapping("/playlist")
    public ResponseEntity<PlaylistAnalysisDto> getAudioAnalysisForTracks(@RequestParam(name = "user_id") String userId,
                                                                         @RequestParam(name = "playlist_id") String playlistId) {
        return ResponseEntity.ok(audioAnalysisService.getAudioAnalysisForPlaylist(userId, playlistId));
    }
}
