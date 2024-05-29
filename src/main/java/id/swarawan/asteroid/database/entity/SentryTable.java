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
@Table(name = "sentry_data")
@DynamicInsert
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentryTable {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spk_id", nullable = false)
    private String spkId;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "sentry_id")
    private String sentryId;

    @Column(name = "name")
    private String name;

    @Column(name = "year_range_min")
    private Integer yearRangeMin;

    @Column(name = "year_range_max")
    private Integer yearRangeMax;

    @Column(name = "potential_impact")
    private Integer potentialImpact;

    @Column(name = "impact_probability")
    private String impactProbability;

    @Column(name = "v_infinite")
    private Double vInfinite;

    @Column(name = "absolute_magnitude")
    private Double absoluteMagnitude;

    @Column(name = "estimated_diameter")
    private Double estimatedDiameter;

    @Column(name = "palermo_scale_ave")
    private Double palermoScaleAve;

    @Column(name = "palermo_scale_max")
    private Double palermoScaleMax;

    @Column(name = "torino_scale")
    private Integer torinoScale;

    @Column(name = "last_obs")
    private LocalDate lastObs;

    @Column(name = "last_obs_jd")
    private Double lastObsJd;

    @Column(name = "is_active_sentry_object")
    private Integer isActiveSentryObject;

    @Column(name = "impact_details_url")
    private String impactDetailsUrl;

    @Column(name = "orbital_element_details_url")
    private String orbitalElementDetailsUrl;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
