package tech.bonda.cft.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReorderService {

    private static final double CROSSFADE_DURATION = 12; // seconds
    private final AudioAnalysisService audioAnalysisService;

    public void orderByTempo(String playlistId) {
        var audioAnalysisMap = audioAnalysisService.getAudioAnalysisForTracks(playlistId);
        var orderedTrackIds = sortByTempo(audioAnalysisMap);

        for (String trackId : orderedTrackIds) {
            Float tempo = audioAnalysisMap.get(trackId).getTrack().getTempo();
            int index = orderedTrackIds.indexOf(trackId);

            System.out.println("Track ID: " + trackId + " Tempo: " + tempo + " is " + index + " in the playlist");

        }


    }

/*    private Map<String, AudioAnalysis> sortByTempoMap(Map<String, AudioAnalysis> audioAnalysisMap) {
        return audioAnalysisMap.entrySet()
                .stream()
                .sorted(Comparator
                        .comparing(analysis -> (analysis.getValue()).getTrack().getTempo()))
                .collect(Collectors
                        .toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new)
                );
    }*/

    private List<String> sortByTempo(Map<String, AudioAnalysis> audioAnalysisMap) {
        return audioAnalysisMap.entrySet()
                .stream()
                .sorted(Comparator
                        .comparing(analysis -> (analysis.getValue()).getTrack().getTempo()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
