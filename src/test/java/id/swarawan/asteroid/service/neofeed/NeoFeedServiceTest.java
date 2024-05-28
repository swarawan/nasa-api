package id.swarawan.asteroid.service.neofeed;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.config.utility.AppUtils;
import id.swarawan.asteroid.database.service.AsteroidDbService;
import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import id.swarawan.asteroid.model.api.data.AsteroidObjectApiData;
import id.swarawan.asteroid.model.api.data.CloseApproachApiData;
import id.swarawan.asteroid.model.api.data.EstimatedDiameterApiData;
import id.swarawan.asteroid.model.api.data.item.EstimatedDiameterApiItem;
import id.swarawan.asteroid.model.api.data.item.MissDistanceApiItem;
import id.swarawan.asteroid.model.api.data.item.RelativeVelocityApiItem;
import id.swarawan.asteroid.model.response.NeoSentryResponse;
import id.swarawan.asteroid.model.response.item.NeoFeedItem;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NeoFeedServiceTest {

    @Mock
    private NasaApiService nasaApiService;

    @Mock
    private NeoSentryService neoSentryService;

    @Mock
    private AsteroidDbService asteroidDbService;

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
        Assertions.assertEquals("Start / end date is required", exception.getMessage());
    }

    @Test
    public void getNeoFeed_missingEndDate_throwException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                neoFeedService.getNeoFeed(LocalDate.now(), null));
        Assertions.assertEquals("Start / end date is required", exception.getMessage());
    }

    @Test
    public void getNeoFeed_exceedLimitDays_throwException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                neoFeedService.getNeoFeed(LocalDate.now(), LocalDate.now().plusDays(10)));
        Assertions.assertEquals("The feed date limit is only 7 days", exception.getMessage());
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
    public void collectFeeds_success() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        EstimatedDiameterApiItem sampleDiameter = EstimatedDiameterApiItem.builder().min(1.1).max(2.2).build();
        AsteroidObjectApiData sampleData = AsteroidObjectApiData.builder()
                .id("1")
                .referenceId("1")
                .name("Name test")
                .nasaJplUrl("https://google.com")
                .absoluteMagnitude(5.5)
                .estimatedDiameter(EstimatedDiameterApiData.builder()
                        .kilometers(sampleDiameter)
                        .meters(sampleDiameter)
                        .miles(sampleDiameter)
                        .feet(sampleDiameter)
                        .build())
                .closestApproaches(Arrays.asList(CloseApproachApiData.builder()
                        .approachDate(LocalDate.of(2024, 1, 1))
                        .approachDateFull(LocalDateTime.of(2024, 1, 1, 1, 1, 1))
                        .approachDateEpoch(564646464L)
                        .relativeVelocity(RelativeVelocityApiItem.builder()
                                .kilometerPerHour("12.1")
                                .kilometerPerSecond("12.2")
                                .milesPerSecond("12.3")
                                .build())
                        .missDistance(MissDistanceApiItem.builder()
                                .astronomical("12.1")
                                .lunar("12.1")
                                .kilometers("12.1")
                                .miles("12.1")
                                .build())
                        .orbitBody("Earth")
                        .build()))
                .isHazardousAsteroid(true)
                .isSentryObject(true)
                .sentryData("http:google.com")
                .build();

        NeoSentryResponse sampleSentry = NeoSentryResponse.builder()
                .spkId("001")
                .fullName("OBS")
                .build();
        List<AsteroidObjectApiData> sampleItem = Arrays.asList(sampleData);

        Mockito.when(neoSentryService.getNeoSentry(Mockito.anyString())).thenReturn(sampleSentry);
        Method collectFeedsMethod = neoFeedService.getClass().getDeclaredMethod("collectFeeds", List.class);
        collectFeedsMethod.setAccessible(true);

        List<NeoFeedItem> actual = (List<NeoFeedItem>) collectFeedsMethod.invoke(neoFeedService, sampleItem);

        Assertions.assertEquals(actual.size(), sampleItem.size());
        Assertions.assertEquals(actual.get(0).getId(), sampleItem.get(0).getId());
        Assertions.assertEquals(actual.get(0).getName(), sampleItem.get(0).getName());
        Assertions.assertEquals(actual.get(0).getJplUrl(), sampleItem.get(0).getNasaJplUrl());
        Assertions.assertEquals(actual.get(0).getAbsoluteMagnitude(), sampleItem.get(0).getAbsoluteMagnitude());
        Assertions.assertEquals(actual.get(0).getIsHazardAsteroid(), sampleItem.get(0).getIsHazardousAsteroid());
        Assertions.assertEquals(actual.get(0).getIsSentryObject(), sampleItem.get(0).getIsSentryObject());
        Assertions.assertEquals(actual.get(0).getSentryData().getSpkId(), sampleSentry.getSpkId());
        Assertions.assertEquals(actual.get(0).getSentryData().getFullName(), sampleSentry.getFullName());

        Assertions.assertEquals(actual.get(0).getEstimatedDiameterKm().getDiameterMin(), sampleItem.get(0).getEstimatedDiameter().getKilometers().getMin());
        Assertions.assertEquals(actual.get(0).getEstimatedDiameterKm().getDiameterMax(), sampleItem.get(0).getEstimatedDiameter().getKilometers().getMax());
        Assertions.assertEquals(actual.get(0).getEstimatedDiameterMiles().getDiameterMin(), sampleItem.get(0).getEstimatedDiameter().getMiles().getMin());
        Assertions.assertEquals(actual.get(0).getEstimatedDiameterMiles().getDiameterMax(), sampleItem.get(0).getEstimatedDiameter().getMiles().getMax());
        Assertions.assertEquals(actual.get(0).getEstimatedDiameterFeet().getDiameterMin(), sampleItem.get(0).getEstimatedDiameter().getFeet().getMin());
        Assertions.assertEquals(actual.get(0).getEstimatedDiameterFeet().getDiameterMax(), sampleItem.get(0).getEstimatedDiameter().getFeet().getMax());

        Assertions.assertEquals(actual.get(0).getCloseApproaches().size(), sampleItem.get(0).getClosestApproaches().size());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getApproachDate(),
                sampleItem.get(0).getClosestApproaches().get(0).getApproachDate());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getApproachDateFull(),
                sampleItem.get(0).getClosestApproaches().get(0).getApproachDateFull());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getVelocityKph(),
                AppUtils.toDouble(sampleItem.get(0).getClosestApproaches().get(0).getRelativeVelocity().getKilometerPerHour(), 0.0));
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getVelocityKps(),
                AppUtils.toDouble(sampleItem.get(0).getClosestApproaches().get(0).getRelativeVelocity().getKilometerPerSecond(), 0.0));
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getVelocityMph(),
                AppUtils.toDouble(sampleItem.get(0).getClosestApproaches().get(0).getRelativeVelocity().getMilesPerSecond(), 0.0));
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getDistanceAstronomical(),
                AppUtils.toDouble(sampleItem.get(0).getClosestApproaches().get(0).getMissDistance().getAstronomical(), 0.0));
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getDistanceLunar(),
                AppUtils.toDouble(sampleItem.get(0).getClosestApproaches().get(0).getMissDistance().getLunar(), 0.0));
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getDistanceKilometers(),
                AppUtils.toDouble(sampleItem.get(0).getClosestApproaches().get(0).getMissDistance().getKilometers(), 0.0));
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getDistanceMiles(),
                AppUtils.toDouble(sampleItem.get(0).getClosestApproaches().get(0).getMissDistance().getMiles(), 0.0));
    }

    @Test
    public void getNeoFeed_resultData() {
        List<NeoFeedResponse> sampleResponse = Arrays.asList(
                NeoFeedResponse.builder()
                        .date(LocalDate.of(2024, 1, 1))
                        .asteroids(Arrays.asList(NeoFeedItem.builder()
                                .id("001")
                                .name("Asteroid-001")
                                .build()))
                        .build()
        );

        NeoFeedApiResponse nasaResponse = NeoFeedApiResponse.builder()
                .elementCount(1)
                .nearEarthObjects(Collections.singletonMap(
                        LocalDate.of(2024, 1, 1),
                        Arrays.asList(AsteroidObjectApiData.builder()
                                .referenceId("001")
                                .name("Asteroid-001")
                                .build()))
                ).build();

        Mockito.when(nasaApiService.getNeoFeedApi(Mockito.any(), Mockito.any()))
                .thenReturn(nasaResponse);

        List<NeoFeedResponse> actualResponse = neoFeedService.getNeoFeed(LocalDate.now(), LocalDate.now());

        Assertions.assertEquals(actualResponse.get(0).getDate(), sampleResponse.get(0).getDate());
        Assertions.assertEquals(
                actualResponse.get(0).getAsteroids().get(0).getId(),
                sampleResponse.get(0).getAsteroids().get(0).getId());
    }
}