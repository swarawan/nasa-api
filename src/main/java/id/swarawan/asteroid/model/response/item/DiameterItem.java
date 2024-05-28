package id.swarawan.asteroid.model.response.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiameterItem {

    @JsonProperty(value = "diameter_min")
    private Double diameterMin;

    @JsonProperty(value = "diameter_max")
    private Double diameterMax;
}
