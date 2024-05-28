package id.swarawan.asteroid.model.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsteroidObjectData {

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
    private EstimatedDiameterData estimatedDiameter;

    @JsonProperty(value = "is_potentially_hazardous_asteroid")
    private Boolean isHazardousAsteroid;

    @JsonProperty(value = "close_approach_data")
    private List<CloseApproachData> closestApproaches;

    @JsonProperty(value = "is_sentry_object")
    private Boolean isSentryObject;
}
