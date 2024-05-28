package id.swarawan.asteroid.api;

import id.swarawan.asteroid.model.response.BaseResponse;
import id.swarawan.asteroid.model.response.NeoFeedResponse;
import id.swarawan.asteroid.service.neofeed.NeoFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(
        value = "/nasa",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class NasaController {

    private final NeoFeedService neoFeedService;

    @Autowired
    public NasaController(NeoFeedService neoFeedService) {
        this.neoFeedService = neoFeedService;
    }

    @GetMapping("/neofeed")
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
}
