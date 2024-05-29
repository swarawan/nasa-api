package id.swarawan.asteroid.model.response.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.swarawan.asteroid.model.api.data.item.OrbitApiItem;
import jakarta.persistence.Column;
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
public class OrbitalItem {

    @JsonProperty(value = "orbit_id")
    private String orbitId;

    @JsonProperty(value = "orbit_determination_date")
    private String orbitDeterminationDate;

    @JsonProperty(value = "first_observation_date")
    private LocalDate firstObservationDate;

    @JsonProperty(value = "last_observation_date")
    private LocalDate lastObservationDate;

    @JsonProperty(value = "data_arc_in_days")
    private Integer dataArcInDays;

    @JsonProperty(value = "observations_used")
    private Integer observationsUsed;

    @JsonProperty(value = "orbit_uncertainty")
    private Double orbitUncertainty;

    @JsonProperty(value = "minimum_orbit_intersection")
    private Double minimum_orbitIntersection;

    @JsonProperty(value = "jupiter_tisserand_invariant")
    private Double jupiterTisserandInvariant;

    @JsonProperty(value = "epoch_osculation")
    private Double epochOsculation;

    @JsonProperty(value = "eccentricity")
    private Double eccentricity;

    @JsonProperty(value = "semi_major_axis")
    private Double semiMajorAxis;

    @JsonProperty(value = "inclination")
    private Double inclination;

    @JsonProperty(value = "ascending_node_longitude")
    private Double ascendingNodeLongitude;

    @JsonProperty(value = "orbital_period")
    private Double orbitalPeriod;

    @JsonProperty(value = "perihelion_distance")
    private Double perihelionDistance;

    @JsonProperty(value = "perihelion_argument")
    private Double perihelionArgument;

    @JsonProperty(value = "aphelion_distance")
    private Double aphelionDistance;

    @JsonProperty(value = "perihelion_time")
    private Double perihelionTime;

    @JsonProperty(value = "mean_anomaly")
    private Double meanAnomaly;

    @JsonProperty(value = "mean_motion")
    private Double meanMotion;

    @JsonProperty(value = "equinox")
    private String equinox;

    @JsonProperty(value = "orbit_class_type")
    private String orbitClassType;

    @JsonProperty(value = "orbit_class_description")
    private String orbitClassDescription;

    @JsonProperty(value = "orbit_class_range")
    private String orbitClassRange;
}
