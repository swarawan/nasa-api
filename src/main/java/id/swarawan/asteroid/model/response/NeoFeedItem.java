package id.swarawan.asteroid.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NeoFeedItem {

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "nasa_jpl_url")
    private String jplUrl;

    @JsonProperty(value = "absolute_magnitude")
    private Double absoluteMagnitude;

    @JsonProperty(value = "is_hazard_asteroid")
    private Boolean isHazardAsteroid;

    @JsonProperty(value = "is_sentry_object")
    private Boolean isSentryObject;
}
