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
            value = "SELECT EXISTS(SELECT 1 FROM asteroid_data WHERE approach_date = :date)")
    long existsByDate(@Param("date") String date);

    @Query(nativeQuery = true,
            value = "SELECT * FROM asteroid_data WHERE approach_date between :start AND :end")
    List<AsteroidTable> getAllByRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
