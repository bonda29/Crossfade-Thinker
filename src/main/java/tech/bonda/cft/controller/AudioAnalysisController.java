package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import tech.bonda.cft.models.payload.dto.PlaylistAnalysisDto;
import tech.bonda.cft.service.AudioAnalysisService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audio-analysis")
public class AudioAnalysisController {
    private final AudioAnalysisService audioAnalysisService;

    @GetMapping("/{trackId}")
    public AudioAnalysis getAudioAnalysis(@PathVariable String trackId) {
        return audioAnalysisService.getAudioAnalysis(trackId, true);
    }

    @GetMapping("/")
    public PlaylistAnalysisDto getAudioAnalysisForTracks(@RequestParam String playlistId) {
        return audioAnalysisService.getAudioAnalysisForTracks(playlistId);
    }
}
