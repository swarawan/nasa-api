package id.swarawan.asteroid.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultiAsteroidResponse {

    @JsonProperty(value = "date")
    private LocalDate date;

    @JsonProperty(value = "asteroids")
    private List<SingleAsteroidResponse> asteroids;
}
