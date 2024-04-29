package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import tech.bonda.cft.models.payload.request.PlaylistCreateDto;
import tech.bonda.cft.service.PlaylistService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    @GetMapping("/{playlistId}")
    public Playlist getPlaylist(@PathVariable String playlistId) {

        return playlistService.getPlaylist(playlistId);
    }

    @PostMapping("/")
    public Playlist createPlaylist(@RequestBody PlaylistCreateDto data) {
        return playlistService.createPlaylist(data.getUserId(), data.getName(), data.getDescription(), data.isPublic());
    }
}
