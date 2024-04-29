package tech.bonda.cft.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReorderService {

    private static final double CROSSFADE_DURATION = 12; // seconds
    private final AudioAnalysisService audioAnalysisService;
    private final PlaylistService playlistService;

    public void orderByTempo(String playlistId, String userId) {
        Map<String, AudioAnalysis> audioAnalysisMap = audioAnalysisService.getAudioAnalysisForTracks(playlistId);
        List<String> orderedTrackIds = sortByTempo(audioAnalysisMap);
        String[] orderedTrackUris = orderedTrackIds.stream()
                .map(uri -> "spotify:track:" + uri)
                .toArray(String[]::new);

        Playlist playlist = playlistService.createPlaylist(userId, "Ordered by tempo", "Playlist ordered by tempo", false);
        playlistService.addTracksToPlaylist(userId, playlist.getId(), orderedTrackUris); //todo: something is wrong here


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
