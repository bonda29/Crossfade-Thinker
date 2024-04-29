package tech.bonda.cft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tech.bonda.cft.service.ReorderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reorder")
public class ReorderController {
    private final ReorderService reorderService;

    @GetMapping("/tempo/{playlistId}")
    public void reorderPlaylist(@PathVariable String playlistId, @RequestParam String userId) {
        reorderService.orderByTempo(playlistId, userId);
    }
}
