package tech.bonda.cft.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ReorderService {

    private final AudioAnalysisService audioAnalysisService;

    private static final double CROSSFADE_DURATION = 12; // seconds


}
