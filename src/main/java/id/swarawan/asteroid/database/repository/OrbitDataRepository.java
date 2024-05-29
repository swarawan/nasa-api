package id.swarawan.asteroid.database.repository;

import id.swarawan.asteroid.database.entity.OrbitTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrbitDataRepository extends JpaRepository<OrbitTable, Long> {

    @Query(nativeQuery = true,
    value = "SELECT * FROM orbit_data WHERE reference_id = :referenceId")
    OrbitTable findByReference(@Param("referenceId") String reference);
}
