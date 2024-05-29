package id.swarawan.asteroid.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import id.swarawan.asteroid.model.response.item.CloseApproachItem;
import id.swarawan.asteroid.model.response.item.DiameterItem;
import id.swarawan.asteroid.model.response.item.OrbitalItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleAsteroidResponse {

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "nasa_jpl_url")
    private String jplUrl;

    @JsonProperty(value = "absolute_magnitude")
    private Double absoluteMagnitude;

    @JsonProperty(value = "estimated_diameter_km")
    private DiameterItem estimatedDiameterKm;

    @JsonProperty(value = "estimated_diameter_miles")
    private DiameterItem estimatedDiameterMiles;

    @JsonProperty(value = "estimated_diameter_feet")
    private DiameterItem estimatedDiameterFeet;

    @JsonProperty(value = "close_approaches")
    private List<CloseApproachItem> closeApproaches;

    @JsonProperty(value = "orbit_data")
    private OrbitalItem orbitData;

    @JsonProperty(value = "is_hazard_asteroid")
    private Boolean isHazardAsteroid;

    @JsonProperty(value = "is_sentry_object")
    private Boolean isSentryObject;

    @JsonProperty(value = "sentry_data")
    private SentryResponse sentryData;
}
