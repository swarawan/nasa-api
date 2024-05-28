package id.swarawan.asteroid.model.api.data.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstimatedDiameterItem {

    @JsonProperty(value = "estimated_diameter_min")
    private Double min;

    @JsonProperty(value = "estimated_diameter_max")
    private Double max;
}