package tech.bonda.cft.models.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
public class PlaylistFeaturesDto implements Serializable {

    Playlist playlist;
    Map<String, AudioFeatures> audioFeaturesMap;
}
