package tech.bonda.cft.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class GetPlaylistService {
    private final GetAccessTokenService getAccessTokenService;
    private final WebClient webClient = WebClient.builder()
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .build();

    private SpotifyApi spotifyApi;

    @SneakyThrows
    public Playlist getPlaylist(String playlistId) {
        GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(playlistId)
//          .fields("tracks.items(track(id))")
//          .market(CountryCode.SE)
//          .additionalTypes("track")
                .build();

        return getPlaylistRequest.execute();
    }

    @SneakyThrows
    public Playlist getPlaylist(String playlistId, String fields) {
        GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(playlistId)
                .fields(fields)
//          .market(CountryCode.SE)
//          .additionalTypes("track")
                .build();

        return getPlaylistRequest.execute();
    }

    @SneakyThrows
    public List<String> getTrackIds(String playlistId) {
        String token = getAccessTokenService.getAccessToken();
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "?fields=tracks(items(track(id))";

        String response = webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        JsonArray itemsArray = jsonObject.getAsJsonObject("tracks").getAsJsonArray("items");

        List<String> tracks = new ArrayList<>();

        for (int i = 0; i < itemsArray.size(); i++) {
            JsonObject trackObject = itemsArray.get(i).getAsJsonObject().getAsJsonObject("track");
            String id = trackObject.get("id").getAsString();

            tracks.add(id);
        }

        return tracks;
    }

    @PostConstruct
    public void getPlaylist() {
        spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(getAccessTokenService.getAccessToken())
                .build();
    }

}
