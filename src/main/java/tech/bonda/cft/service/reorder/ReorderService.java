package tech.bonda.cft.service.reorder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import tech.bonda.cft.models.payload.request.PlaylistCreateDto;
import tech.bonda.cft.service.analysis.AudioAnalysisService;
import tech.bonda.cft.service.PlaylistFacade;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReorderService {

    //    private static final double CROSSFADE_DURATION = 12; // seconds
    private final AudioAnalysisService audioAnalysisService;
    private final PlaylistFacade playlistFacade;

    public String orderByTempo(String userId, String playlistId) {
        var analysisDto = audioAnalysisService.getAudioAnalysisForPlaylist(userId, playlistId);

        Playlist playlist = analysisDto.getPlaylist();
        Map<String, AudioAnalysis> analysisMap = analysisDto.getAudioAnalysisMap();

        Map<String, String> idToUriMap = new LinkedHashMap<>();
        for (PlaylistTrack track : playlist.getTracks().getItems()) {
            idToUriMap.put(track.getTrack().getId(), track.getTrack().getUri());
        }

        List<String> orderedTrackIds = analysisMap.entrySet().stream()
                .sorted(Comparator.comparing(analysis -> analysis.getValue().getTrack().getTempo()))
                .map(Map.Entry::getKey)
                .toList();

        String[] orderedTrackUris = orderedTrackIds.stream()
                .map(idToUriMap::get)
                .toArray(String[]::new);

        Playlist newPlaylists = playlistFacade.createPlaylist(new PlaylistCreateDto(
                userId,
                playlist.getName() + " - ordered by tempo",
                "Ordered by tempo",
                true));

        playlistFacade.addTracksToPlaylist(
                userId,
                newPlaylists.getId(),
                orderedTrackUris);

        return newPlaylists.getExternalUrls().getExternalUrls().get("spotify");
    }
}
