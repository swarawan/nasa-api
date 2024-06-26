package id.swarawan.asteroid.service.nasa;

import id.swarawan.asteroid.config.openfeign.OpenFeignConfig;
import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import id.swarawan.asteroid.model.api.NeoLookupApiResponse;
import id.swarawan.asteroid.model.api.NeoSentryApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "nasa-api", url = "https://api.nasa.gov", configuration = OpenFeignConfig.class)
public interface NasaApi {

    @GetMapping("/neo/rest/v1/feed")
    NeoFeedApiResponse getNeoFeedApi(@RequestParam("api_key") String key,
                                     @RequestParam("start_date") String startDate,
                                     @RequestParam("end_date") String endDate);

    @GetMapping("/neo/rest/v1/neo/sentry/{reference_id}")
    NeoSentryApiResponse getNeoSentry(@RequestParam("api_key") String key,
                                      @PathVariable("reference_id") String referenceId);

    @GetMapping("/neo/rest/v1/neo/{reference_id}")
    NeoLookupApiResponse getNeoLookup(@RequestParam("api_key") String key,
                                      @PathVariable("reference_id") String referenceId);
}
