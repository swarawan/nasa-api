package id.swarawan.asteroid.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asteroid_data")
@DynamicInsert
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsteroidTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "sentry_id")
    @OneToOne(fetch = FetchType.LAZY)
    private SentryTable sentry;

    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    @Column(name = "approach_date")
    private LocalDate approachDate;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nasa_jpl_url")
    private String nasaJplUrl;

    @Column(name = "absolute_magnitude")
    private Double absoluteMagnitude;

    @Column(name = "diameter_km_min")
    private Double diameterKmMin;

    @Column(name = "diameter_km_max")
    private Double diameterKmMax;

    @Column(name = "diameter_miles_min")
    private Double diameterMilesMin;

    @Column(name = "diameter_miles_max")
    private Double diameterMilesMax;

    @Column(name = "diameter_feet_min")
    private Double diameterFeetMin;

    @Column(name = "diameter_feet_max")
    private Double diameterFeetMax;

    @Column(name = "is_hazard_potential")
    private Boolean isHazardPotential;

    @Column(name = "is_sentry_object")
    private Boolean isSentryObject;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
