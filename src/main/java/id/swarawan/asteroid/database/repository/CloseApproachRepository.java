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
            value = "SELECT * FROM close_approach WHERE reference_id = :reference_id")
    List<CloseApproachTable> findByReferenceId(@Param("reference_id") String referenceId);

    @Query(nativeQuery = true,
            value = "SELECT EXISTS(SELECT * FROM close_approach WHERE reference_id = :reference_id AND approach_date_epoch = :epoch)")
    long existByReferenceIdAndEpoch(@Param("reference_id") String referenceId, @Param("epoch") Long epoch);
}
