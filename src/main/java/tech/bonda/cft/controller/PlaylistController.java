package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{playlist_id}")
    public ResponseEntity<Playlist> getPlaylist(@PathVariable(name = "playlist_id") String playlistId,
                                                @RequestParam(name = "user_id") String userId) {
        return ResponseEntity.ok(playlistFacade.getPlaylist(userId, playlistId));
    }

    @PutMapping("/{playlist_id}")
    public ResponseEntity<String> addTracksToPlaylist(@PathVariable(name = "playlist_id") String playlistId,
                                                      @RequestParam(name = "user_id") String userId,
                                                      @RequestBody String[] trackUris) {
        playlistFacade.addTracksToPlaylist(userId, playlistId, trackUris);
        return ResponseEntity.ok("Tracks added successfully");
    }

    @DeleteMapping("/{playlist_id}")
    public ResponseEntity<String> unfollowPlaylist(@PathVariable(name = "playlist_id") String playlistId,
                                                   @RequestParam(name = "user_id") String userId) {
        playlistFacade.unfollowPlaylist(userId, playlistId);
        return ResponseEntity.ok("Playlist unfollowed successfully");
    }
}
