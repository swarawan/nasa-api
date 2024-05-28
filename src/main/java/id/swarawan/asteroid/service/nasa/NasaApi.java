package id.swarawan.asteroid.service.nasa;

import id.swarawan.asteroid.config.openfeign.OpenFeignConfig;
import id.swarawan.asteroid.model.api.NeoFeedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "nasaApi", url = "http://api.nasa.gov", configuration = OpenFeignConfig.class)
public interface NasaApi {

    @GetMapping("/neo/rest/v1/feed")
    NeoFeedResponse getNeoFeedApi(@RequestParam("api_key") String key,
                                  @RequestParam("start_date") LocalDate startDate,
                                  @RequestParam("end_date") LocalDate endDate);
}
