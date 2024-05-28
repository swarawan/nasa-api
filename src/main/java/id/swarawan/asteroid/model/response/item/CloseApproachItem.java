package id.swarawan.asteroid.model.response.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloseApproachItem {
    @JsonProperty(value = "date")
    private LocalDate approachDate;

    @JsonProperty(value = "date_full")
    private String approachDateFull;

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
