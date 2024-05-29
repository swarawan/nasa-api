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
            value = "SELECT * FROM close_approach WHERE reference_id = :reference_id AND deleted_at is null")
    List<CloseApproachTable> findByReferenceId(@Param("reference_id") String referenceId);

    @Query(nativeQuery = true,
            value = "SELECT max(id)                    as 'id'," +
                    "       reference_id," +
                    "       max(approach_date)         as 'approach_date'," +
                    "       max(approach_date_full)    as 'approach_date_full'," +
                    "       max(approach_date_epoch)   as 'approach_date_epoch'," +
                    "       max(velocity_kps)          as 'velocity_kps'," +
                    "       max(velocity_kph)          as 'velocity_kph'," +
                    "       max(velocity_mph)          as 'velocity_mph'," +
                    "       max(distance_astronomical) as 'distance_astronomical'," +
                    "       max(distance_lunar)        as 'distance_lunar'," +
                    "       max(distance_kilometers)   as 'distance_kilometers'," +
                    "       max(distance_miles)        as 'distance_miles'," +
                    "       max(orbiting_body)         as 'orbiting_body'" +
                    " FROM close_approach" +
                    " WHERE deleted_at is null" +
                    " group by reference_id" +
                    " order by distance_kilometers DESC" +
                    " LIMIT :limit")
    List<Object[]> findTopDistance(@Param("limit") long limit);

    @Query(nativeQuery = true,
            value = "SELECT EXISTS(SELECT * FROM close_approach " +
                    "WHERE reference_id = :reference_id " +
                    "AND approach_date_epoch = :epoch " +
                    "AND deleted_at is null)")
    long existByReferenceIdAndEpoch(@Param("reference_id") String referenceId, @Param("epoch") Long epoch);
}
