package id.swarawan.asteroid.service.neofeed;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.database.entity.AsteroidTable;
import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.service.AsteroidDbService;
import id.swarawan.asteroid.database.service.CloseApproachDbService;
import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import id.swarawan.asteroid.model.api.NeoLookupApiResponse;
import id.swarawan.asteroid.model.response.item.CloseApproachItem;
import id.swarawan.asteroid.model.response.item.DiameterItem;
import id.swarawan.asteroid.model.response.item.NeoFeedItem;
import id.swarawan.asteroid.model.response.NeoFeedResponse;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class NeoFeedService {

    private final NasaApiService nasaApiService;
    private final NeoSentryService neoSentryService;
    private final AsteroidDbService asteroidDbService;
    private final CloseApproachDbService closeApproachDbService;

    @Autowired
    public NeoFeedService(NasaApiService nasaApiService,
                          NeoSentryService neoSentryService,
                          AsteroidDbService asteroidDbService,
                          CloseApproachDbService closeApproachDbService) {
        this.nasaApiService = nasaApiService;
        this.neoSentryService = neoSentryService;
        this.asteroidDbService = asteroidDbService;
        this.closeApproachDbService = closeApproachDbService;
    }

    public List<NeoFeedResponse> getNeoFeed(LocalDate startDate, LocalDate endDate) throws BadRequestException {
        validateRequest(startDate, endDate);
        List<NeoFeedResponse> result = new ArrayList<>();
        Set<LocalDate> dates = collectDates(startDate, endDate);
        Set<LocalDate> emptyDates = new HashSet<>();

        dates.forEach(date -> {
            List<AsteroidTable> dataTable = asteroidDbService.findByApproachDate(date);
            if (dataTable.isEmpty()) {
                emptyDates.add(date);
            } else {
                result.add(NeoFeedResponse.builder()
                        .date(date)
                        .asteroids(collectFeeds(dataTable))
                        .build());
            }
        });

        if (!emptyDates.isEmpty()) {
            startDate = emptyDates.stream().findFirst().get();
            for (LocalDate date : emptyDates) {
                endDate = date;
            }
            NeoFeedApiResponse neoFeedApiResponse = nasaApiService.getNeoFeedApi(startDate, endDate);
            Map<LocalDate, List<NeoLookupApiResponse>> nearEarthObjects = neoFeedApiResponse.getNearEarthObjects();
            if (!nearEarthObjects.isEmpty()) {
                asteroidDbService.save(nearEarthObjects);
            }

            emptyDates.forEach(date -> {
                List<AsteroidTable> dataTable = asteroidDbService.findByApproachDate(date);
                result.add(NeoFeedResponse.builder()
                        .date(date)
                        .asteroids(collectFeeds(dataTable))
                        .build());
            });
        }
        return result.stream().sorted(Comparator.comparing(NeoFeedResponse::getDate)).toList();
    }

//    public List<NeoFeedResponse> getNeoFeed(LocalDate startDate, LocalDate endDate) throws BadRequestException {
//        validateRequest(startDate, endDate);
//        NeoFeedApiResponse neoFeedApiResponse = nasaApiService.getNeoFeedApi(startDate, endDate);
//        if (neoFeedApiResponse.getNearEarthObjects().isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        asteroidDbService.save(neoFeedApiResponse.getNearEarthObjects());
//        Set<LocalDate> dates = collectDates(startDate, endDate);
//
//        return dates.stream().map(date -> {
//            List<AsteroidTable> asteroids = dataTable.stream().filter(dt -> dt.getApproachDate().isEqual(date)).toList();
//            return NeoFeedResponse.builder()
//                    .date(date)
//                    .asteroids(collectFeeds(asteroids))
//                    .build();
//
//        }).toList();
//    }

    private Set<LocalDate> collectDates(LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> result = new HashSet<>();
        LocalDate tempDate = startDate;
        while (tempDate.isBefore(endDate) || tempDate.equals(endDate)) {
            result.add(tempDate);
            tempDate = tempDate.plusDays(1);
        }
        return result;
    }

    private List<NeoFeedItem> collectFeeds(List<AsteroidTable> asteroids) {
        return asteroids.stream().map(data -> {
            NeoFeedItem.NeoFeedItemBuilder builder = NeoFeedItem.builder()
                    .id(data.getReferenceId())
                    .name(data.getName())
                    .jplUrl(data.getNasaJplUrl())
                    .absoluteMagnitude(data.getAbsoluteMagnitude())
                    .isHazardAsteroid(data.getIsHazardPotential())
                    .isSentryObject(data.getIsSentryObject());

            if (data.getIsSentryObject()) {
                builder.sentryData(neoSentryService.getNeoSentry(data.getReferenceId()));
            }

            builder.estimatedDiameterKm(DiameterItem.builder()
                    .diameterMin(data.getDiameterKmMin())
                    .diameterMax(data.getDiameterKmMax())
                    .build());

            builder.estimatedDiameterMiles(DiameterItem.builder()
                    .diameterMin(data.getDiameterMilesMin())
                    .diameterMax(data.getDiameterMilesMax())
                    .build());

            builder.estimatedDiameterFeet(DiameterItem.builder()
                    .diameterMin(data.getDiameterFeetMin())
                    .diameterMax(data.getDiameterFeetMax())
                    .build());

            List<CloseApproachTable> closeApproachTables = closeApproachDbService.findByReferenceId(data.getReferenceId());
            List<CloseApproachItem> closeApproachItems = closeApproachTables.stream().map(closeApproach -> CloseApproachItem.builder()
                    .approachDate(closeApproach.getApproachDate())
                    .approachDateFull(closeApproach.getApproachDateFull())
                    .orbitBody(closeApproach.getOrbitingBody())
                    .velocityKps(closeApproach.getVelocityKps())
                    .velocityKph(closeApproach.getVelocityKph())
                    .velocityMph(closeApproach.getVelocityMph())
                    .distanceAstronomical(closeApproach.getDistanceAstronomical())
                    .distanceLunar(closeApproach.getDistanceLunar())
                    .distanceKilometers(closeApproach.getDistanceKilometers())
                    .distanceMiles(closeApproach.getDistanceMiles())
                    .build()).toList();
            builder.closeApproaches(closeApproachItems);

            return builder.build();
        }).toList();
    }

    private void validateRequest(LocalDate startDate, LocalDate endDate) throws BadRequestException {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            throw new BadRequestException("Start / end date is required");
        } else if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date cannot more than end date");
        } else if (endDate.isAfter(startDate.plusDays(7))) {
            throw new BadRequestException("The feed date limit is only 7 days");
        }
    }
}