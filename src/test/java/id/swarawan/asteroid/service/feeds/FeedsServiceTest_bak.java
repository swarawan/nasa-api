package id.swarawan.asteroid.service.feeds;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.database.entity.AsteroidTable;
import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.service.AsteroidDbService;
import id.swarawan.asteroid.database.service.CloseApproachDbService;
import id.swarawan.asteroid.model.response.SentryResponse;
import id.swarawan.asteroid.model.response.SingleAsteroidResponse;
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
import java.util.*;

// TODO: Will revisit for later

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FeedsServiceTest_bak {

    @Mock
    private NasaApiService nasaApiService;

    @Mock
    private AsteroidDbService asteroidDbService;

    @Mock
    private CloseApproachDbService closeApproachDbService;

    @InjectMocks
    private FeedsService feedsService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getNeoFeed_missingStartDate_returnException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                feedsService.findAllFeeds(null, LocalDate.now()));
        Assertions.assertEquals("Start / end date is required", exception.getMessage());
    }

    @Test
    public void getNeoFeed_missingEndDate_throwException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                feedsService.findAllFeeds(LocalDate.now(), null));
        Assertions.assertEquals("Start / end date is required", exception.getMessage());
    }

    @Test
    public void getNeoFeed_exceedLimitDays_throwException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                feedsService.findAllFeeds(LocalDate.now(), LocalDate.now().plusDays(10)));
        Assertions.assertEquals("The feed date limit is only 7 days", exception.getMessage());
    }

    @Test
    public void getNeoFeed_startDateIsAfterEndDate_throwException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                feedsService.findAllFeeds(LocalDate.now().plusDays(2), LocalDate.now()));
        Assertions.assertEquals("Start date cannot more than end date", exception.getMessage());
    }


//    @Test
//    public void getNeoFeed_emptyApiResponse_resultOkEmptyData() {
//        NeoFeedApiResponse nasaResponse = NeoFeedApiResponse.builder()
//                .elementCount(0)
//                .nearEarthObjects(Collections.emptyMap())
//                .build();
//
//        List<AsteroidTable> asteroidTables = Collections.singletonList(AsteroidTable.builder()
//                        .
//                .build());
//
//        Mockito.when(nasaApiService.getNeoFeedApi(Mockito.any(), Mockito.any())).thenReturn(nasaResponse);
//
//        List<NeoFeedResponse> actualResponse = neoFeedService.getNeoFeed(LocalDate.now(), LocalDate.now());
//        Assertions.assertTrue(actualResponse.isEmpty());
//    }

    @Test
    public void collectFeeds_success() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        LocalDate sampleDate = LocalDate.now();
        String sampleDateFull = "2024-Jan-01 01:01:01";
        Double sampleRange = 1.1;
        AsteroidTable sampleData = AsteroidTable.builder()
                .id(1L)
                .referenceId("1")
                .name("Name test")
                .nasaJplUrl("https://google.com")
                .absoluteMagnitude(5.5)
                .diameterKmMin(sampleRange)
                .diameterKmMax(sampleRange)
                .diameterMilesMin(sampleRange)
                .diameterMilesMax(sampleRange)
                .diameterFeetMin(sampleRange)
                .diameterFeetMax(sampleRange)
                .isHazardPotential(true)
                .isSentryObject(true)
                .build();

        SentryResponse sampleSentry = SentryResponse.builder()
                .spkId("001")
                .fullName("OBS")
                .build();
        List<AsteroidTable> sampleItem = Collections.singletonList(sampleData);
        List<CloseApproachTable> sampleCloseApproach = Collections.singletonList(CloseApproachTable.builder()
                .approachDate(sampleDate)
                .approachDateFull(sampleDateFull)
                .orbitingBody("Earth")
                .velocityKps(12.1)
                .velocityKph(12.2)
                .velocityMph(12.3)
                .distanceAstronomical(12.1)
                .distanceLunar(12.1)
                .distanceKilometers(12.1)
                .distanceMiles(12.1)
                .build());

        Mockito.when(closeApproachDbService.findByReferenceId(Mockito.anyString())).thenReturn(sampleCloseApproach);
        Method collectFeedsMethod = feedsService.getClass().getDeclaredMethod("collectFeeds", List.class);
        collectFeedsMethod.setAccessible(true);

        List<SingleAsteroidResponse> actual = (List<SingleAsteroidResponse>) collectFeedsMethod.invoke(feedsService, sampleItem);

        Assertions.assertEquals(actual.size(), sampleItem.size());
        Assertions.assertEquals(actual.get(0).getId(), sampleItem.get(0).getReferenceId());
        Assertions.assertEquals(actual.get(0).getName(), sampleItem.get(0).getName());
        Assertions.assertEquals(actual.get(0).getJplUrl(), sampleItem.get(0).getNasaJplUrl());
        Assertions.assertEquals(actual.get(0).getAbsoluteMagnitude(), sampleItem.get(0).getAbsoluteMagnitude());
        Assertions.assertEquals(actual.get(0).getIsHazardAsteroid(), sampleItem.get(0).getIsHazardPotential());
        Assertions.assertEquals(actual.get(0).getIsSentryObject(), sampleItem.get(0).getIsSentryObject());
        Assertions.assertEquals(actual.get(0).getSentryData().getSpkId(), sampleSentry.getSpkId());
        Assertions.assertEquals(actual.get(0).getSentryData().getFullName(), sampleSentry.getFullName());

        Assertions.assertEquals(actual.get(0).getEstimatedDiameterKm().getDiameterMin(), sampleItem.get(0).getDiameterKmMin());
        Assertions.assertEquals(actual.get(0).getEstimatedDiameterKm().getDiameterMax(), sampleItem.get(0).getDiameterKmMax());
        Assertions.assertEquals(actual.get(0).getEstimatedDiameterMiles().getDiameterMin(), sampleItem.get(0).getDiameterMilesMin());
        Assertions.assertEquals(actual.get(0).getEstimatedDiameterMiles().getDiameterMax(), sampleItem.get(0).getDiameterMilesMax());
        Assertions.assertEquals(actual.get(0).getEstimatedDiameterFeet().getDiameterMin(), sampleItem.get(0).getDiameterFeetMin());
        Assertions.assertEquals(actual.get(0).getEstimatedDiameterFeet().getDiameterMax(), sampleItem.get(0).getDiameterFeetMax());

        Assertions.assertEquals(actual.get(0).getCloseApproaches().size(), sampleCloseApproach.size());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getApproachDate(),
                sampleCloseApproach.get(0).getApproachDate());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getApproachDateFull(),
                sampleCloseApproach.get(0).getApproachDateFull());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getVelocityKph(),
                sampleCloseApproach.get(0).getVelocityKph());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getVelocityKps(),
                sampleCloseApproach.get(0).getVelocityKps());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getVelocityMph(),
                sampleCloseApproach.get(0).getVelocityMph());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getDistanceAstronomical(),
                sampleCloseApproach.get(0).getDistanceAstronomical());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getDistanceLunar(),
                sampleCloseApproach.get(0).getDistanceLunar());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getDistanceKilometers(),
                sampleCloseApproach.get(0).getDistanceKilometers());
        Assertions.assertEquals(
                actual.get(0).getCloseApproaches().get(0).getDistanceMiles(),
                sampleCloseApproach.get(0).getDistanceMiles());
    }

//    @Test
//    public void getNeoFeed_resultData() {
//        LocalDate sampleDate = LocalDate.of(2023, 1, 1);
//        List<NeoFeedResponse> sampleResponse = Collections.singletonList(
//                NeoFeedResponse.builder()
//                        .date(sampleDate)
//                        .asteroids(Collections.singletonList(NeoFeedItem.builder()
//                                .id("001")
//                                .name("Asteroid-001")
//                                .build()))
//                        .build()
//        );
//
//        NeoFeedApiResponse nasaResponse = NeoFeedApiResponse.builder()
//                .elementCount(1)
//                .nearEarthObjects(Collections.singletonMap(
//                        sampleDate,
//                        Collections.singletonList(AsteroidObjectApiData.builder()
//                                .referenceId("001")
//                                .name("Asteroid-001")
//                                .build()))
//                ).build();
//
//        List<AsteroidTable> sampleDataTable = Collections.singletonList(
//                AsteroidTable.builder()
//                        .referenceId("001")
//                        .name("Asteroid-001")
//                        .approachDate(sampleDate)
//                        .build()
//        );
//
//        Mockito.when(nasaApiService.getNeoFeedApi(Mockito.any(), Mockito.any()))
//                .thenReturn(nasaResponse);
//        Mockito.doNothing().when(asteroidDbService).save(Mockito.any());
//
//        List<NeoFeedResponse> actualResponse = neoFeedService.getNeoFeed(sampleDate, sampleDate);
//
//        Assertions.assertEquals(actualResponse.get(0).getDate(), sampleResponse.get(0).getDate());
//        Assertions.assertEquals(
//                actualResponse.get(0).getAsteroids().get(0).getId(),
//                sampleResponse.get(0).getAsteroids().get(0).getId());
//    }
}