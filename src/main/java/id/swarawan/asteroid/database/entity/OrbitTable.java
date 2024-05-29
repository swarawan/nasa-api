package id.swarawan.asteroid.database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "orbit_data")
@DynamicInsert
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrbitTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "orbit_id")
    private String orbitId;

    @Column(name = "orbit_determination_date")
    private String orbitDeterminationDate;

    @Column(name = "first_observation_date")
    private LocalDate firstObservationDate;

    @Column(name = "last_observation_date")
    private LocalDate lastObservationDate;

    @Column(name = "data_arc_in_days")
    private Integer dataArcInDays;

    @Column(name = "observations_used")
    private Integer observationsUsed;

    @Column(name = "orbit_uncertainty")
    private Double orbitUncertainty;

    @Column(name = "minimum_orbit_intersection")
    private Double minimum_orbitIntersection;

    @Column(name = "jupiter_tisserand_invariant")
    private Double jupiterTisserandInvariant;

    @Column(name = "epoch_osculation")
    private Double epochOsculation;

    @Column(name = "eccentricity")
    private Double eccentricity;

    @Column(name = "semi_major_axis")
    private Double semiMajorAxis;

    @Column(name = "inclination")
    private Double inclination;

    @Column(name = "ascending_node_longitude")
    private Double ascendingNodeLongitude;

    @Column(name = "orbital_period")
    private Double orbitalPeriod;

    @Column(name = "perihelion_distance")
    private Double perihelionDistance;

    @Column(name = "perihelion_argument")
    private Double perihelionArgument;

    @Column(name = "aphelion_distance")
    private Double aphelionDistance;

    @Column(name = "perihelion_time")
    private Double perihelionTime;

    @Column(name = "mean_anomaly")
    private Double meanAnomaly;

    @Column(name = "mean_motion")
    private Double meanMotion;

    @Column(name = "equinox")
    private String equinox;

    @Column(name = "orbit_class_type")
    private String orbitClassType;

    @Column(name = "orbit_class_description")
    private String orbitClassDescription;

    @Column(name = "orbit_class_range")
    private String orbitClassRange;
}
