package id.swarawan.asteroid.database.repository;

import id.swarawan.asteroid.database.entity.AsteroidTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsteroidDataRepository extends JpaRepository<AsteroidTable, Long> {

    @Query(nativeQuery = true,
            value = "SELECT EXISTS(SELECT 1 FROM asteroid_data WHERE approach_date = :date AND deleted_at is null)")
    long existsByDate(@Param("date") String date);

    @Query(nativeQuery = true,
            value = "SELECT * FROM asteroid_data WHERE reference_id = :reference_id")
    AsteroidTable findByReferenceId(@Param("reference_id") String referenceId);

    @Query(nativeQuery = true,
            value = "SELECT * FROM asteroid_data WHERE approach_date = :date AND deleted_at is null")
    List<AsteroidTable> findByApproachDate(@Param("date") LocalDate date);

    @Query(nativeQuery = true,
            value = "SELECT * FROM asteroid_data WHERE deleted_at is null order by diameter_km_max DESC LIMIT :limit")
    List<AsteroidTable> findTopDiameter(@Param("limit") long limit);
}
