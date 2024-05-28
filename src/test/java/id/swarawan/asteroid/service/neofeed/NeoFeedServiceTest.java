package id.swarawan.asteroid.service.neofeed;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import id.swarawan.asteroid.model.api.data.AsteroidObjectData;
import id.swarawan.asteroid.model.response.NeoFeedItem;
import id.swarawan.asteroid.model.response.NeoFeedResponse;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NeoFeedServiceTest {

    @Mock
    private NasaApiService nasaApiService;

    @InjectMocks
    private NeoFeedService neoFeedService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getNeoFeed_missingStartDate_returnException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                neoFeedService.getNeoFeed(null, LocalDate.now()));
        Assertions.assertEquals("Start / end date cannot be null", exception.getMessage());
    }

    @Test
    public void getNeoFeed_missingEndDate_throwException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                neoFeedService.getNeoFeed(LocalDate.now(), null));
        Assertions.assertEquals("Start / end date cannot be null", exception.getMessage());
    }

    @Test
    public void getNeoFeed_startDateIsAfterEndDate_throwException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                neoFeedService.getNeoFeed(LocalDate.now().plusDays(2), LocalDate.now()));
        Assertions.assertEquals("Start date cannot more than end date", exception.getMessage());
    }


    @Test
    public void getNeoFeed_EmptyApiResponse_resultOkEmptyData() {
        NeoFeedApiResponse nasaResponse = NeoFeedApiResponse.builder()
                .elementCount(0)
                .nearEarthObjects(Collections.emptyMap())
                .build();

        Mockito.when(nasaApiService.getNeoFeedApi(Mockito.any(), Mockito.any()))
                .thenReturn(nasaResponse);

        List<NeoFeedResponse> actualResponse = neoFeedService.getNeoFeed(LocalDate.now(), LocalDate.now());
        Assertions.assertTrue(actualResponse.isEmpty());
    }

    @Test
    public void getNeoFeed_resultData() {
        List<NeoFeedResponse> sampleRespone = Collections.singletonList(
                NeoFeedResponse.builder()
                        .date("2024-01-01")
                        .asteroids(Collections.singletonList(NeoFeedItem.builder()
                                .id("001")
                                .name("Asteroid-001")
                                .build()))
                        .build()
        );

        NeoFeedApiResponse nasaResponse = NeoFeedApiResponse.builder()
                .elementCount(1)
                .nearEarthObjects(Collections.singletonMap(
                        "2024-01-01",
                        Arrays.asList(AsteroidObjectData.builder()
                                .referenceId("001")
                                .name("Asteroid-001")
                                .build()))
                ).build();

        Mockito.when(nasaApiService.getNeoFeedApi(Mockito.any(), Mockito.any()))
                .thenReturn(nasaResponse);

        List<NeoFeedResponse> actualResponse = neoFeedService.getNeoFeed(LocalDate.now(), LocalDate.now());

        Assertions.assertEquals(actualResponse.get(0).getDate(), sampleRespone.get(0).getDate());
        Assertions.assertEquals(
                actualResponse.get(0).getAsteroids().get(0).getId(),
                sampleRespone.get(0).getAsteroids().get(0).getId());
    }

}