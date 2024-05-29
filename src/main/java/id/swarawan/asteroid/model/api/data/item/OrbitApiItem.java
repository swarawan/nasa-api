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
public class OrbitApiItem {
    @JsonProperty(value = "orbit_class_type")
    private String orbitClassType;

    @JsonProperty(value = "orbit_class_description")
    private String orbitClassDescription;

    @JsonProperty(value = "orbit_class_range")
    private String orbitClassRange;
}
