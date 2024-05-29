package id.swarawan.asteroid.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.swarawan.asteroid.model.api.data.CloseApproachApiData;
import id.swarawan.asteroid.model.api.data.EstimatedDiameterApiData;
import id.swarawan.asteroid.model.api.data.OrbitalApiData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NeoLookupApiResponse {

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "neo_reference_id")
    private String referenceId;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "nasa_jpl_url")
    private String nasaJplUrl;

    @JsonProperty(value = "absolute_magnitude_h")
    private Double absoluteMagnitude;

    @JsonProperty(value = "estimated_diameter")
    private EstimatedDiameterApiData estimatedDiameter;

    @JsonProperty(value = "is_potentially_hazardous_asteroid")
    private Boolean isHazardousAsteroid;

    @JsonProperty(value = "close_approach_data")
    private List<CloseApproachApiData> closestApproaches;

    @JsonProperty(value = "orbital_data")
    private List<OrbitalApiData> orbitalData;

    @JsonProperty(value = "is_sentry_object")
    private Boolean isSentryObject;

    @JsonProperty(value = "sentry_data")
    private String sentryData;
}
