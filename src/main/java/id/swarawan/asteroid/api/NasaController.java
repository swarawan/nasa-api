package id.swarawan.asteroid.api;

import id.swarawan.asteroid.model.response.BaseResponse;
import id.swarawan.asteroid.model.response.FeedResponse;
import id.swarawan.asteroid.model.response.NeoLookupResponse;
import id.swarawan.asteroid.service.feeds.FeedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(
        value = "/nasa/feed",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class NasaController {

    private final FeedsService feedsService;

    @Autowired
    public NasaController(FeedsService feedsService) {
        this.feedsService = feedsService;
    }

    @GetMapping
    public ResponseEntity<Object> getNeoFeed(
            @RequestParam("start_date") LocalDate startDate,
            @RequestParam("end_date") LocalDate endDate) {

        List<FeedResponse> data = feedsService.findAllFeeds(startDate, endDate);
        BaseResponse<Object> response = BaseResponse.builder()
                .success(true)
                .data(data)
                .message("Success")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{reference_id}")
    public ResponseEntity<Object> getLookup(@PathVariable("reference_id") String referenceId) {
        NeoLookupResponse data = feedsService.findSingleFeed(referenceId);
        BaseResponse<Object> response = BaseResponse.builder()
                .success(true)
                .data(data)
                .message("Success")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{reference_id}")
    public ResponseEntity<Object> deleteFeed(@PathVariable("reference_id") String referenceId) {
        feedsService.delete(referenceId);
        BaseResponse<Object> response = BaseResponse.builder()
                .success(true)
                .message("Deleted")
                .build();
        return ResponseEntity.ok(response);
    }
}
