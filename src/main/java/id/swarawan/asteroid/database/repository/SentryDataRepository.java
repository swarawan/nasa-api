package id.swarawan.asteroid.database.repository;

import id.swarawan.asteroid.database.entity.SentryTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentryDataRepository extends JpaRepository<SentryTable, Long> {

    @Query(nativeQuery = true,
    value = "SELECT * FROM sentry_data WHERE spk_id = :spk_id")
    SentryTable findBySpkId(@Param("spk_id") String spkId);
}
