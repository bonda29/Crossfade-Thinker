package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import tech.bonda.cft.service.GetAudioAnalysisService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audio-analysis")
public class GetAudioAnalysisController {
    private final GetAudioAnalysisService getAudioAnalysisService;

    @GetMapping("/{trackId}")
    public AudioAnalysis getAudioAnalysis(@PathVariable String trackId) {
        return getAudioAnalysisService.getAudioAnalysis(trackId);
    }
}
