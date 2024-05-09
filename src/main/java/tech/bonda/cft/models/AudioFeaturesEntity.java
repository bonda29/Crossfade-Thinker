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
@Table(name = "audio_features")
public class AudioFeaturesEntity {
    @Id
    @Column(name = "track_id")
    private String trackId;

    @Lob
    @Column(name = "audio_features", columnDefinition = "LONGTEXT")
    private String audioFeaturesJson;
}
