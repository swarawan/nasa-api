package id.swarawan.asteroid.api;

import id.swarawan.asteroid.model.response.BaseResponse;
import id.swarawan.asteroid.model.response.NeoFeedResponse;
import id.swarawan.asteroid.model.response.NeoLookupResponse;
import id.swarawan.asteroid.model.response.NeoSentryResponse;
import id.swarawan.asteroid.service.neofeed.NeoFeedService;
import id.swarawan.asteroid.service.neofeed.NeoSentryService;
import id.swarawan.asteroid.service.neolookup.NeoLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(
        value = "/nasa/neolookup",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class NeoLookupController {

    private final NeoLookupService neoLookupService;

    @Autowired
    public NeoLookupController(NeoLookupService neoLookupService, NeoSentryService neoSentryService) {
        this.neoLookupService = neoLookupService;
    }

    @GetMapping("{reference_id}")
    public ResponseEntity<Object> getNeoFeed(@PathVariable("reference_id") String referenceId) {
        NeoLookupResponse data = neoLookupService.getNeoLookup(referenceId);
        BaseResponse<Object> response = BaseResponse.builder()
                .success(true)
                .data(data)
                .message("Success")
                .build();
        return ResponseEntity.ok(response);
    }
}
