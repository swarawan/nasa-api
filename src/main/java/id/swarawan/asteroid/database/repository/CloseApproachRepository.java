package id.swarawan.asteroid.database.repository;

import id.swarawan.asteroid.database.entity.AsteroidTable;
import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.entity.SentryTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CloseApproachRepository extends JpaRepository<CloseApproachTable, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM close_approach WHERE asteroid_id = :asteroidId")
    List<CloseApproachTable> findByAsteroid(@Param("asteroidId") Long asteroidId);
}