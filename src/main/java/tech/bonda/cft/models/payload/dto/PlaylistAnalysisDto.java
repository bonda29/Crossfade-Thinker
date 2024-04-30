package tech.bonda.cft.models.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
public class PlaylistAnalysisDto implements Serializable {
    private Playlist playlist;
    private Map<String, AudioAnalysis> audioAnalysisMap;
}
