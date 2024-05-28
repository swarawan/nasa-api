package id.swarawan.asteroid.service.nasa;

import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NasaApiServiceTest {

    @Mock
    private NasaApi nasaApi;

    @InjectMocks
    private NasaApiService nasaApiService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getNeoFeed_success() throws Exception {
        NeoFeedApiResponse sampleResponse = NeoFeedApiResponse.builder()
                .elementCount(1)
                .build();

        Mockito.when(nasaApi.getNeoFeedApi(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .then(answer -> sampleResponse);

        NeoFeedApiResponse actualResponse = nasaApiService.getNeoFeedApi("abcd1234", LocalDate.now(), LocalDate.now());
        Assertions.assertEquals(actualResponse.getElementCount(), sampleResponse.getElementCount());
    }

    @Test
    public void test_getNeoFeed_failure() throws Exception {
        Mockito.when(nasaApi.getNeoFeedApi(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error on requesting an API"));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            nasaApiService.getNeoFeedApi("abcd1234", LocalDate.now(), LocalDate.now());
        });
    }

}