package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import tech.bonda.cft.models.payload.request.PlaylistCreateDto;
import tech.bonda.cft.service.PlaylistFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistFacade playlistFacade;

    @PostMapping("/")
    public Playlist createPlaylist(@RequestBody PlaylistCreateDto data) {
        return playlistFacade.createPlaylist(data);
    }

    @GetMapping("/{playlistId}")
    public Playlist getPlaylist(@PathVariable String playlistId, @RequestParam String userId) {
        return playlistFacade.getPlaylist(userId, playlistId);
    }

    @PutMapping("/{playlistId}")
    public void addTracksToPlaylist(@PathVariable String playlistId, @RequestParam String userId, @RequestBody String[] trackUris) {
        playlistFacade.addTracksToPlaylist(userId, playlistId, trackUris);
    }

    @DeleteMapping("/{playlistId}")
    public void unfollowPlaylist(@PathVariable String playlistId, @RequestParam String userId) {
        playlistFacade.unfollowPlaylist(userId, playlistId);
    }
}
