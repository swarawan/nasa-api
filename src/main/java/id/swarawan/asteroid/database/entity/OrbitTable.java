package id.swarawan.asteroid.database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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

    @Column(name = "orbit_id")
    private String orbitId;

    @Column(name = "orbit_determination_date")
    private String orbitDeterminationDate;

    @Column(name = "first_observation_date")
    private String firstObservationDate;

    @Column(name = "last_observation_date")
    private String lastObservationDate;

    @Column(name = "data_arc_in_days")
    private float dataArcInDays;

    @Column(name = "observations_used")
    private float observationsUsed;

    @Column(name = "orbit_uncertainty")
    private String orbitUncertainty;

    @Column(name = "minimum_orbit_intersection")
    private String minimum_orbitIntersection;

    @Column(name = "jupiter_tisserand_invariant")
    private String jupiterTisserandInvariant;

    @Column(name = "epoch_osculation")
    private String epochOsculation;

    @Column(name = "eccentricity")
    private String eccentricity;

    @Column(name = "semi_major_axis")
    private String semiMajorAxis;

    @Column(name = "inclination")
    private String inclination;

    @Column(name = "ascending_node_longitude")
    private String ascendingNodeLongitude;

    @Column(name = "orbital_period")
    private String orbitalPeriod;

    @Column(name = "perihelion_distance")
    private String perihelionDistance;

    @Column(name = "perihelion_argument")
    private String perihelionArgument;

    @Column(name = "aphelion_distance")
    private String aphelionDistance;

    @Column(name = "perihelion_time")
    private String perihelionTime;

    @Column(name = "mean_anomaly")
    private String meanAnomaly;

    @Column(name = "mean_motion")
    private String meanMotion;

    @Column(name = "equinox")
    private String equinox;
}
