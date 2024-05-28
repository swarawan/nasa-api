package id.swarawan.asteroid.model.api.data.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelativeVelocityItem {

    @JsonProperty(value = "kilometers_per_second")
    private String kilometerPerSecond;

    @JsonProperty(value = "kilometers_per_hour")
    private String kilometerPerHour;

    @JsonProperty(value = "miles_per_hour")
    private String milesPerSecond;
}