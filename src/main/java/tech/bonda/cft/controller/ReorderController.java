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
    public String reorderPlaylist(@RequestParam String userId, @PathVariable String playlistId) {
        return reorderService.orderByTempo(userId, playlistId);
    }
}
