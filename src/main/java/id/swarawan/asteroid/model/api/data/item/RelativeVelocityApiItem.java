package id.swarawan.asteroid.model.api.data.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelativeVelocityApiItem {

    @JsonProperty(value = "kilometers_per_second")
    private String kilometerPerSecond;

    @JsonProperty(value = "kilometers_per_hour")
    private String kilometerPerHour;

    @JsonProperty(value = "miles_per_hour")
    private String milesPerSecond;
}