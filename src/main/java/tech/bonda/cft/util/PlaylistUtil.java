package tech.bonda.cft.util;

import se.michaelthelin.spotify.model_objects.specification.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistUtil {
    public static List<String> getTrackIds(Playlist playlist) {
        List<String> tracksIds = new ArrayList<>();
        for (var track : playlist.getTracks().getItems()) {
            tracksIds.add(track.getTrack().getId());
        }
        return tracksIds;
    }
}
