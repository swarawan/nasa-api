package id.swarawan.asteroid.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.swarawan.asteroid.model.response.item.NeoFeedItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NeoFeedResponse {

    @JsonProperty(value = "date")
    private String date;

    @JsonProperty(value = "asteroids")
    private List<NeoFeedItem> asteroids;
}
