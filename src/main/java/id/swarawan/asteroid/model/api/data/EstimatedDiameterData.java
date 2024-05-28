package id.swarawan.asteroid.model.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.swarawan.asteroid.model.api.data.item.EstimatedDiameterItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstimatedDiameterData {

    @JsonProperty(value = "kilometers")
    private EstimatedDiameterItem kilometers;

    @JsonProperty(value = "meters")
    private EstimatedDiameterItem meters;

    @JsonProperty(value = "miles")
    private EstimatedDiameterItem miles;

    @JsonProperty(value = "feet")
    private EstimatedDiameterItem feet;
}
