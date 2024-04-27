package tech.bonda.cft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bonda.cft.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}