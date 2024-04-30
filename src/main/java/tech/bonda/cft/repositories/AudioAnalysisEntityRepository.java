package tech.bonda.cft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bonda.cft.models.AudioAnalysisEntity;

@Repository
public interface AudioAnalysisEntityRepository extends JpaRepository<AudioAnalysisEntity, String> {
}