package id.swarawan.asteroid.database.repository;

import id.swarawan.asteroid.database.entity.AsteroidTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AsteroidDataRepository extends JpaRepository<AsteroidTable, Long> {

    @Query(nativeQuery = true,
            value = "SELECT EXISTS(SELECT 1 FROM asteroid_data WHERE approach_date = :date)")
    boolean existsByDate(@Param("date") LocalDate date);
}
