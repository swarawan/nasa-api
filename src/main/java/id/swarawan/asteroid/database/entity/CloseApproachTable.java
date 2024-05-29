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
@Table(name = "close_approach")
@DynamicInsert
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloseApproachTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "approach_date")
    private LocalDate approachDate;

    @Column(name = "approach_date_full")
    private String approachDateFull;

    @Column(name = "approach_date_epoch")
    private Long approachDateEpoch;

    @Column(name = "velocity_kps")
    private Double velocityKps;

    @Column(name = "velocity_kph")
    private Double velocityKph;

    @Column(name = "velocity_mph")
    private Double velocityMph;

    @Column(name = "distance_astronomical")
    private Double distanceAstronomical;

    @Column(name = "distance_lunar")
    private Double distanceLunar;

    @Column(name = "distance_kilometers")
    private Double distanceKilometers;

    @Column(name = "distance_miles")
    private Double distanceMiles;

    @Column(name = "orbiting_body")
    private String orbitingBody;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
