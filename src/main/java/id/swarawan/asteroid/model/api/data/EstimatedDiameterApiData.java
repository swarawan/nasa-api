package id.swarawan.asteroid.model.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.swarawan.asteroid.model.api.data.item.EstimatedDiameterApiItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstimatedDiameterApiData {

    @JsonProperty(value = "kilometers")
    private EstimatedDiameterApiItem kilometers;

    @JsonProperty(value = "meters")
    private EstimatedDiameterApiItem meters;

    @JsonProperty(value = "miles")
    private EstimatedDiameterApiItem miles;

    @JsonProperty(value = "feet")
    private EstimatedDiameterApiItem feet;
}
