package id.swarawan.asteroid.model.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.swarawan.asteroid.model.api.data.item.OrbitApiItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrbitalApiData {

    @JsonProperty(value = "orbit_id")
    private String orbitId;

    @JsonProperty(value = "orbit_determination_date")
    private LocalDateTime orbitDeterminationDate;

    @JsonProperty(value = "first_observation_date")
    private LocalDate firstObservationDate;

    @JsonProperty(value = "last_observation_date")
    private LocalDate lastObservationDate;

    @JsonProperty(value = "data_arc_in_days")
    private Integer dataArcInDays;

    @JsonProperty(value = "observations_used")
    private Integer observationsUsed;

    @JsonProperty(value = "orbit_uncertainty")
    private String orbitUncertainty;

    @JsonProperty(value = "minimum_orbit_intersection")
    private String minimum_orbitIntersection;

    @JsonProperty(value = "jupiter_tisserand_invariant")
    private String jupiterTisserandInvariant;

    @JsonProperty(value = "epoch_osculation")
    private String epochOsculation;

    @JsonProperty(value = "eccentricity")
    private String eccentricity;

    @JsonProperty(value = "semi_major_axis")
    private String semiMajorAxis;

    @JsonProperty(value = "inclination")
    private String inclination;

    @JsonProperty(value = "ascending_node_longitude")
    private String ascendingNodeLongitude;

    @JsonProperty(value = "orbital_period")
    private String orbitalPeriod;

    @JsonProperty(value = "perihelion_distance")
    private String perihelionDistance;

    @JsonProperty(value = "perihelion_argument")
    private String perihelionArgument;

    @JsonProperty(value = "aphelion_distance")
    private String aphelionDistance;

    @JsonProperty(value = "perihelion_time")
    private String perihelionTime;

    @JsonProperty(value = "mean_anomaly")
    private String meanAnomaly;

    @JsonProperty(value = "mean_motion")
    private String meanMotion;

    @JsonProperty(value = "equinox")
    private String equinox;

    @JsonProperty(value = "orbit_class")
    OrbitApiItem orbitClass;
}
