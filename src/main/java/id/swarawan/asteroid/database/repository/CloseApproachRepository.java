package id.swarawan.asteroid.database.repository;

import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.entity.SentryTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloseApproachRepository extends JpaRepository<CloseApproachTable, Long> {
}
