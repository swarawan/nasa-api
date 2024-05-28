package id.swarawan.asteroid.database.repository;

import id.swarawan.asteroid.database.entity.AsteroidData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsteroidDataRepository extends JpaRepository<AsteroidData, Long> {
}
