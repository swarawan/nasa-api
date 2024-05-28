package id.swarawan.asteroid.model.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloseApproachData {

    @JsonProperty(value = "close_approach_date")
    private String approachDate;

    @JsonProperty(value = "close_approach_date_full")
    private String approachDateFull;

    @JsonProperty(value = "epoch_date_close_approach")
    private Long approachDateEpoch;

    @JsonProperty(value = "relative_velocity")
    private RelativeVelocityItem relativeVelocity;

    @JsonProperty(value = "miss_distance")
    private MissDistanceItem missDistance;

    @JsonProperty(value = "orbiting_body")
    private String orbitBody;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RelativeVelocityItem {

        @JsonProperty(value = "kilometers_per_second")
        private String kilometerPerSecond;

        @JsonProperty(value = "kilometers_per_hour")
        private String kilometerPerHour;

        @JsonProperty(value = "miles_per_hour")
        private String milesPerSecond;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MissDistanceItem {

        @JsonProperty(value = "astronomical")
        private String astronomical;

        @JsonProperty(value = "lunar")
        private String lunar;

        @JsonProperty(value = "kilometers")
        private String kilometers;

        @JsonProperty(value = "miles")
        private String miles;
    }
}
