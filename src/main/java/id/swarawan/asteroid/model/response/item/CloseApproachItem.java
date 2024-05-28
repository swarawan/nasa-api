package id.swarawan.asteroid.model.response.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.swarawan.asteroid.model.api.data.item.MissDistanceItem;
import id.swarawan.asteroid.model.api.data.item.RelativeVelocityItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloseApproachItem {
    @JsonProperty(value = "date")
    private String approachDate;

    @JsonProperty(value = "velocity_kps")
    private Double velocityKps;

    @JsonProperty(value = "velocity_kph")
    private Double velocityKph;

    @JsonProperty(value = "velocity_mph")
    private Double velocityMph;

    @JsonProperty(value = "distance_astronomical")
    private Double distanceAstronomical;

    @JsonProperty(value = "distance_lunar")
    private Double distanceLunar;

    @JsonProperty(value = "distance_kilometers")
    private Double distanceKilometers;

    @JsonProperty(value = "distance_miles")
    private Double distanceMiles;

    @JsonProperty(value = "orbiting_body")
    private String orbitBody;
}
