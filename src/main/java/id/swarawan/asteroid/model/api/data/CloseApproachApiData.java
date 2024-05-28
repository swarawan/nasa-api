package id.swarawan.asteroid.model.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.swarawan.asteroid.model.api.data.item.MissDistanceApiItem;
import id.swarawan.asteroid.model.api.data.item.RelativeVelocityApiItem;
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
public class CloseApproachApiData {

    @JsonProperty(value = "close_approach_date")
    private LocalDate approachDate;

    @JsonProperty(value = "close_approach_date_full")
    private LocalDateTime approachDateFull;

    @JsonProperty(value = "epoch_date_close_approach")
    private Long approachDateEpoch;

    @JsonProperty(value = "relative_velocity")
    private RelativeVelocityApiItem relativeVelocity;

    @JsonProperty(value = "miss_distance")
    private MissDistanceApiItem missDistance;

    @JsonProperty(value = "orbiting_body")
    private String orbitBody;
}
