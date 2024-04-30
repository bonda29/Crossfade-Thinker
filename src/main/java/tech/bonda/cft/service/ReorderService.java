package tech.bonda.cft.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReorderService {

    //    private static final double CROSSFADE_DURATION = 12; // seconds
    private final AudioAnalysisService audioAnalysisService;
    private final PlaylistService playlistService;

    public String orderByTempo(String playlistId, String userId) {
        var playlistAnalysisDto = audioAnalysisService.getAudioAnalysisForTracks(playlistId);

        var playlist = playlistAnalysisDto.getPlaylist();
        Map<String, AudioAnalysis> audioAnalysisMap = playlistAnalysisDto.getAudioAnalysisMap();

        Map<String, String> idToUriMap = new LinkedHashMap<>();
        for (PlaylistTrack track : playlist.getTracks().getItems()) {
            idToUriMap.put(track.getTrack().getId(), track.getTrack().getUri());
        }

        List<String> orderedTrackIds = audioAnalysisMap.entrySet().stream()
                .sorted(Comparator.comparing(analysis -> analysis.getValue().getTrack().getTempo()))
                .map(Map.Entry::getKey)
                .toList();

        String[] orderedTrackUris = orderedTrackIds.stream()
                .map(idToUriMap::get)
                .toArray(String[]::new);

        var newPlaylists = playlistService.createPlaylist(userId, playlist.getName() + " reordered", "Reordered by tempo", true);
        playlistService.addTracksToPlaylist(userId, newPlaylists.getId(), orderedTrackUris);

        return newPlaylists.getExternalUrls().getExternalUrls().get("spotify");
    }


}
