package id.swarawan.asteroid.api;

import id.swarawan.asteroid.model.response.BaseResponse;
import id.swarawan.asteroid.model.response.NeoFeedResponse;
import id.swarawan.asteroid.model.response.NeoSentryResponse;
import id.swarawan.asteroid.service.neofeed.NeoFeedService;
import id.swarawan.asteroid.service.neofeed.NeoSentryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(
        value = "/nasa/neofeed",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class NasaController {

    private final NeoFeedService neoFeedService;
    private final NeoSentryService neoSentryService;

    @Autowired
    public NasaController(NeoFeedService neoFeedService, NeoSentryService neoSentryService) {
        this.neoFeedService = neoFeedService;
        this.neoSentryService = neoSentryService;
    }

    @GetMapping
    public ResponseEntity<Object> getNeoFeed(
            @RequestParam("start_date") LocalDate startDate,
            @RequestParam("end_date") LocalDate endDate) {

        List<NeoFeedResponse> data = neoFeedService.getNeoFeed(startDate, endDate);
        BaseResponse<Object> response = BaseResponse.builder()
                .success(true)
                .data(data)
                .message("Success")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sentry/{reference_id}")
    public ResponseEntity<Object> getNeoSentry(@PathVariable("reference_id") String referenceId) {
        NeoSentryResponse data = neoSentryService.getNeoSentry(referenceId);
        BaseResponse<Object> response = BaseResponse.builder()
                .success(true)
                .data(data)
                .message("Success")
                .build();
        return ResponseEntity.ok(response);
    }
}
