//package tech.bonda.cft.service.reorder;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysisTrack;
//import tech.bonda.cft.service.analysis.AudioAnalysisService;
//import tech.bonda.cft.service.PlaylistFacade;
//
//@Service
//@RequiredArgsConstructor
//public class CompatabilityCalculator {
//    private final AudioAnalysisService audioAnalysisService;
//    private final PlaylistFacade playlistFacade;
//
//    /**
//     * value between 0 and 1 representing the compatability of two tracks
//     * 0 - track are incompatible
//     * 1 - tracks are identical
//     *
//     * @param track1 the first track
//     * @param track2 the second track
//     * @return a double representing the compatability of the two tracks (0-1)
//     */
//    public Double calculateCompatability(AudioAnalysisTrack track1, AudioAnalysisTrack track2) {
//        double tempoDiff = Math.abs(track1.getTempo() - track2.getTempo());
//        double keyDiff = Math.abs(track1.getKey() - track2.getKey());
//        double timeSigDiff = Math.abs(track1.getTimeSignature() - track2.getTimeSignature());
//        double modeDiff = track1.getMode() == track2.getMode() ? 0.0 : 1.0;
//        double loudnessDiff = Math.abs(track1.getLoudness() - track2.getLoudness());
//
//        // Define weights for each attribute
//        double tempoWeight = 0.2;
//        double keyWeight = 0.2;
//        double timeSigWeight = 0.2;
//        double modeWeight = 0.2;
//        double loudnessWeight = 0.2;
//
//        double sum = (tempoDiff * tempoWeight) +
//                (keyDiff * keyWeight) +
//                (timeSigDiff * timeSigWeight) +
//                (modeDiff * modeWeight) +
//                (loudnessDiff * loudnessWeight);
//
//        double normalizedTempoDiff = tempoDiff / sum;
//        double normalizedKeyDiff = keyDiff / sum;
//        double normalizedTimeSigDiff = timeSigDiff / sum;
//        double normalizedModeDiff = modeDiff / sum;
//        double normalizedLoudnessDiff = loudnessDiff / sum;
//
//        double compatibility = 1 - (normalizedTempoDiff + normalizedKeyDiff + normalizedTimeSigDiff + normalizedModeDiff + normalizedLoudnessDiff);
//
//        return compatibility;
//    }
//}
