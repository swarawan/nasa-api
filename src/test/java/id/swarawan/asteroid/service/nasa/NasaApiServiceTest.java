package id.swarawan.asteroid.service.nasa;

import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import id.swarawan.asteroid.model.api.NeoSentryApiResponse;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

class NasaApiServiceTest {

    @Mock
    private NasaApi nasaApi;

    @InjectMocks
    private NasaApiService nasaApiService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getNeoFeed_success_returnData() {
        NeoFeedApiResponse sampleResponse = NeoFeedApiResponse.builder()
                .elementCount(1)
                .build();

        ReflectionTestUtils.setField(nasaApiService, "nasaApiKey", "abcd1234");
        Mockito.when(nasaApi.getNeoFeedApi(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .then(answer -> sampleResponse);

        Optional<NeoFeedApiResponse> actualResponse = nasaApiService.getNeoFeedApi(LocalDate.now(), LocalDate.now());
        Assertions.assertTrue(actualResponse.isPresent());
        Assertions.assertEquals(actualResponse.get().getElementCount(), sampleResponse.getElementCount());
    }

    @Test
    public void test_getNeoFeed_failed_return400() {
        ReflectionTestUtils.setField(nasaApiService, "nasaApiKey", "abcd1234");
        Mockito.when(nasaApi.getNeoFeedApi(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error on requesting an API"));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            nasaApiService.getNeoFeedApi(LocalDate.now(), LocalDate.now());
        });

        Assertions.assertEquals(exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(exception.getReason(), "Error on requesting an API");
    }

    @Test
    public void getNeoSentry_success_returnData() {
        NeoSentryApiResponse sampleResponse = NeoSentryApiResponse.builder()
                .spkId("1")
                .fullName("OBS")
                .build();

        ReflectionTestUtils.setField(nasaApiService, "nasaApiKey", "abcd1234");
        Mockito.when(nasaApi.getNeoSentry(Mockito.anyString(), Mockito.anyString())).then(answer -> sampleResponse);

        Optional<NeoSentryApiResponse> actualResponse = nasaApiService.getNeoSentry("abcd1234");
        Assertions.assertTrue(actualResponse.isPresent());
        Assertions.assertEquals(actualResponse.get().getSpkId(), sampleResponse.getSpkId());
    }

    @Test
    public void getNeoSentry_failed_return404() {
        ReflectionTestUtils.setField(nasaApiService, "nasaApiKey", "abcd1234");
        Mockito.when(nasaApi.getNeoSentry(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));


        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            nasaApiService.getNeoSentry("abcd1234");
        });

        Assertions.assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
        Assertions.assertEquals(exception.getReason(), "Resource not found");
    }

}