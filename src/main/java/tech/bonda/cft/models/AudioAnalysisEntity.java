package tech.bonda.cft.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AudioAnalysisEntity {
    @Id
    private String trackId;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String audioAnalysisJson;
}
