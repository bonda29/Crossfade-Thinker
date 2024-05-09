package tech.bonda.cft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bonda.cft.models.AudioFeaturesEntity;

@Repository
public interface AudioFeaturesEntityRepository extends JpaRepository<AudioFeaturesEntity, String> {
}