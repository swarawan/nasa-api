package id.swarawan.asteroid.api;

import id.swarawan.asteroid.enums.Modes;
import id.swarawan.asteroid.model.response.BaseResponse;
import id.swarawan.asteroid.model.response.MultiAsteroidResponse;
import id.swarawan.asteroid.model.response.SingleAsteroidResponse;
import id.swarawan.asteroid.service.feeds.FeedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(
        value = "/nasa/asteroids",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class NasaController {

    private final FeedsService feedsService;

    @Autowired
    public NasaController(FeedsService feedsService) {
        this.feedsService = feedsService;
    }

    @GetMapping
    public ResponseEntity<Object> findAsteroids(
            @RequestParam("start_date") LocalDate startDate,
            @RequestParam(value = "end_date", required = false) LocalDate endDate) {
        if (Objects.isNull(endDate)) {
            endDate = startDate.plusDays(7);
        }
        List<MultiAsteroidResponse> data = feedsService.findAllFeeds(startDate, endDate);
        BaseResponse<Object> response = BaseResponse.builder()
                .success(true)
                .data(data)
                .message("Success")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{reference_id}")
    public ResponseEntity<Object> findSingleAsteroid(@PathVariable("reference_id") String referenceId) {
        SingleAsteroidResponse data = feedsService.findSingleFeed(referenceId);
        BaseResponse<Object> response = BaseResponse.builder()
                .success(true)
                .data(data)
                .message("Success")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("top{number}/by/{modes}")
    public ResponseEntity<Object> findTopAsteroids(@PathVariable("number") long number,
                                                   @PathVariable("modes") Modes modes) {
        List<SingleAsteroidResponse> data = feedsService.topN(number, modes);
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
