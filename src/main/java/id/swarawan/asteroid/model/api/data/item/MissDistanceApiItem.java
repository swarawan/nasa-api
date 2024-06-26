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
public class MissDistanceApiItem {

    @JsonProperty(value = "astronomical")
    private String astronomical;

    @JsonProperty(value = "lunar")
    private String lunar;

    @JsonProperty(value = "kilometers")
    private String kilometers;

    @JsonProperty(value = "miles")
    private String miles;
}