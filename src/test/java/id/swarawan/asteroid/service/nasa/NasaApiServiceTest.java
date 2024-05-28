package id.swarawan.asteroid.service.nasa;

import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
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
    public void getNeoFeed_success() {
        NeoFeedApiResponse sampleResponse = NeoFeedApiResponse.builder()
                .elementCount(1)
                .build();

        ReflectionTestUtils.setField(nasaApiService, "nasaApiKey", "abcd1234");
        Mockito.when(nasaApi.getNeoFeedApi(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .then(answer -> sampleResponse);

        NeoFeedApiResponse actualResponse = nasaApiService.getNeoFeedApi(LocalDate.now(), LocalDate.now());
        Assertions.assertEquals(actualResponse.getElementCount(), sampleResponse.getElementCount());
    }

    @Test
    public void test_getNeoFeed_failure() {
        ReflectionTestUtils.setField(nasaApiService, "nasaApiKey", "abcd1234");
        Mockito.when(nasaApi.getNeoFeedApi(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error on requesting an API"));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            nasaApiService.getNeoFeedApi(LocalDate.now(), LocalDate.now());
        });

        Assertions.assertEquals(exception.getReason(), "Error on requesting an API");
    }

}