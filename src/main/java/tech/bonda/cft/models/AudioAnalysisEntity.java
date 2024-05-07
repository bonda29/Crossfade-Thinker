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
    private String trackId;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String audioAnalysisJson;
}
