package tech.bonda.cft.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import tech.bonda.cft.models.payload.request.PlaylistCreateDto;
import tech.bonda.cft.service.playlist.AddTracksToPlaylistService;
import tech.bonda.cft.service.playlist.CreatePlaylistService;
import tech.bonda.cft.service.playlist.GetPlaylistService;
import tech.bonda.cft.service.playlist.UnfollowPlaylistService;

@Service
@RequiredArgsConstructor
public class PlaylistFacade {
    private final CreatePlaylistService createPlaylistService;
    private final GetPlaylistService getPlaylistService;
    private final AddTracksToPlaylistService addTracksToPlaylistService;
    private final UnfollowPlaylistService unfollowPlaylistService;

    public Playlist createPlaylist(PlaylistCreateDto data) {
        return createPlaylistService.createPlaylist(
                data.getUserId(),
                data.getName(),
                data.getDescription(),
                data.isPublic());
    }

    public Playlist getPlaylist(String userId, String playlistId) {
        return getPlaylistService.getPlaylist(userId, playlistId);
    }

    public void addTracksToPlaylist(String userId, String playlistId, String[] trackUris) {
        addTracksToPlaylistService.addTracksToPlaylist(userId, playlistId, trackUris);
    }

    public void unfollowPlaylist(String userId, String playlistId) {
        unfollowPlaylistService.deletePlaylist(userId, playlistId);
    }
}
