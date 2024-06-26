package tech.bonda.cft.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "audio_analysis")
public class AudioAnalysisEntity {
    @Id
    @Column(name = "track_id")
    private String trackId;

    @Lob
    @Column(name = "audio_analysis", columnDefinition = "LONGTEXT")
    private String audioAnalysisJson;
}
