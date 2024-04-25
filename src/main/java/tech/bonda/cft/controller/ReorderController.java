package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bonda.cft.service.ReorderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reorder")
public class ReorderController {
    private final ReorderService reorderService;

    @GetMapping("/tempo/{playlistId}")
    public void reorderPlaylist(@PathVariable String playlistId) {
        reorderService.orderByTempo(playlistId);
    }
}
