package id.swarawan.asteroid.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NeoFeedApiResponse {

    @JsonProperty(value = "element_count")
    private Integer elementCount;

    @JsonProperty(value = "near_earth_objects")
    private Map<LocalDate, List<NeoLookupApiResponse>> nearEarthObjects;
}
