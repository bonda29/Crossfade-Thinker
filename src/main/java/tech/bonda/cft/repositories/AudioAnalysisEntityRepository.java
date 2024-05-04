package tech.bonda.cft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bonda.cft.models.AudioAnalysisEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AudioAnalysisEntityRepository extends JpaRepository<AudioAnalysisEntity, String> {
}