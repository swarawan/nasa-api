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
public class EstimatedDiameterApiItem {

    @JsonProperty(value = "estimated_diameter_min")
    private Double min;

    @JsonProperty(value = "estimated_diameter_max")
    private Double max;
}