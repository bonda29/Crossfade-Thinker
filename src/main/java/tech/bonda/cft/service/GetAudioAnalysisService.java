package tech.bonda.cft.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class GetAudioAnalysisService {
    private final GetAccessTokenService getAccessTokenService;
    private final WebClient webClient = WebClient.builder()
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .build();

    private SpotifyApi spotifyApi;

    @SneakyThrows
    public AudioAnalysis getAudioAnalysis(String trackId) {
        return spotifyApi.getAudioAnalysisForTrack(trackId)
                .build()
                .execute();
    }

    @PostConstruct
    public void getPlaylist() {
        spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(getAccessTokenService.getAccessToken())
                .build();
    }

}
