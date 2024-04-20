package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import tech.bonda.cft.service.GetPlaylistService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlists")
public class GetPlaylistController {
    private final GetPlaylistService getPlaylistService;

    @GetMapping("/{playlistId}")
    public Playlist getPlaylist(@PathVariable String playlistId, @RequestParam(required = false) String fields, @RequestParam(required = false) String market, @RequestParam(required = false) String additionalTypes) {

        if (fields != null) {
            return getPlaylistService.getPlaylist(playlistId, fields);
        }
        return getPlaylistService.getPlaylist(playlistId);
    }

    @GetMapping("/test/{playlistId}")
    public List<String> getPlaylistTracks(@PathVariable String playlistId) {
        return getPlaylistService.getTrackIds(playlistId);
    }

}
