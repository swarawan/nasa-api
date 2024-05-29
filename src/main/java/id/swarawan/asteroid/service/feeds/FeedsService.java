package id.swarawan.asteroid.service.feeds;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.config.exceptions.NotFoundException;
import id.swarawan.asteroid.database.entity.AsteroidTable;
import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.entity.OrbitTable;
import id.swarawan.asteroid.database.service.AsteroidDbService;
import id.swarawan.asteroid.database.service.CloseApproachDbService;
import id.swarawan.asteroid.database.service.OrbitDataDbService;
import id.swarawan.asteroid.enums.Modes;
import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import id.swarawan.asteroid.model.api.NeoLookupApiResponse;
import id.swarawan.asteroid.model.api.data.CloseApproachApiData;
import id.swarawan.asteroid.model.response.SingleAsteroidResponse;
import id.swarawan.asteroid.model.response.MultiAsteroidResponse;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class FeedsService {

    private final NasaApiService nasaApiService;
    private final AsteroidDbService asteroidDbService;
    private final CloseApproachDbService closeApproachDbService;
    private final OrbitDataDbService orbitDataDbService;
    private final FeedsHelper feedsHelper;

    @Autowired
    public FeedsService(NasaApiService nasaApiService,
                        AsteroidDbService asteroidDbService,
                        CloseApproachDbService closeApproachDbService,
                        OrbitDataDbService orbitDataDbService,
                        FeedsHelper feedsHelper) {
        this.nasaApiService = nasaApiService;
        this.asteroidDbService = asteroidDbService;
        this.closeApproachDbService = closeApproachDbService;
        this.orbitDataDbService = orbitDataDbService;
        this.feedsHelper = feedsHelper;
    }

    public List<MultiAsteroidResponse> findAllFeeds(LocalDate startDate, LocalDate endDate) throws BadRequestException {
        validateRequest(startDate, endDate);
        List<MultiAsteroidResponse> result = new ArrayList<>();
        Set<LocalDate> dates = feedsHelper.collectDates(startDate, endDate);
        Set<LocalDate> emptyDates = new HashSet<>();

        dates.forEach(date -> {
            List<AsteroidTable> dataTable = asteroidDbService.findByApproachDate(date);
            if (dataTable.isEmpty()) {
                emptyDates.add(date);
            } else {
                result.add(MultiAsteroidResponse.builder()
                        .date(date)
                        .asteroids(generateLookup(dataTable))
                        .build());
            }
        });

        if (!emptyDates.isEmpty()) {
            startDate = emptyDates.stream().findFirst().get();
            for (LocalDate date : emptyDates) {
                endDate = date;
            }
            NeoFeedApiResponse neoFeedApiResponse = nasaApiService.getNeoFeedApi(startDate, endDate)
                    .orElseThrow(() -> new NotFoundException("Data not found"));
            Map<LocalDate, List<NeoLookupApiResponse>> nearEarthObjects = neoFeedApiResponse.getNearEarthObjects();
            if (!nearEarthObjects.isEmpty()) {
                asteroidDbService.saveAll(nearEarthObjects);
            }

            emptyDates.forEach(date -> {
                List<AsteroidTable> dataTable = asteroidDbService.findByApproachDate(date);
                result.add(MultiAsteroidResponse.builder()
                        .date(date)
                        .asteroids(generateLookup(dataTable))
                        .build());
            });
        }
        return result.stream().sorted(Comparator.comparing(MultiAsteroidResponse::getDate)).toList();
    }

    public SingleAsteroidResponse findSingleFeed(String referenceId) {
        if (Objects.isNull(referenceId)) {
            throw new BadRequestException("Reference ID is required");
        }

        NeoLookupApiResponse apiResponse = nasaApiService.getNeoLookUp(referenceId)
                .orElseThrow(() -> new NotFoundException("Data not found"));

        AsteroidTable asteroidTable = asteroidDbService.findByReferenceId(referenceId);
        if (Objects.isNull(asteroidTable)) {
            asteroidTable = asteroidDbService.save(apiResponse);
        }

        List<CloseApproachApiData> closeApproachApiData = apiResponse.getClosestApproaches();
        List<CloseApproachTable> closeApproachTables = closeApproachDbService.findByReferenceId(referenceId);
        if (closeApproachTables.size() != closeApproachApiData.size()) {
            closeApproachDbService.save(referenceId, closeApproachApiData);
            closeApproachTables = closeApproachDbService.findByReferenceId(referenceId);
        }

        OrbitTable orbitTable = orbitDataDbService.findByReference(referenceId);
        if (Objects.isNull(orbitTable)) {
            orbitDataDbService.save(referenceId, apiResponse.getOrbitalData());
        }

        return feedsHelper.generateFeedResponse(asteroidTable, closeApproachTables, orbitTable);
    }

    public void delete(String referenceId) {
        asteroidDbService.delete(referenceId);
    }

    private List<SingleAsteroidResponse> generateLookup(List<AsteroidTable> asteroids) {
        return asteroids.stream().map(data -> {
            List<CloseApproachTable> closeApproachTables = closeApproachDbService.findByReferenceId(data.getReferenceId());
            return feedsHelper.generateFeedResponse(data, closeApproachTables);
        }).toList();
    }

    private void validateRequest(LocalDate startDate, LocalDate endDate) throws BadRequestException {
        if (Objects.isNull(startDate)) {
            throw new BadRequestException("Start date is required");
        } else if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date cannot more than end date");
        } else if (endDate.isAfter(startDate.plusDays(7))) {
            throw new BadRequestException("The feed date limit is only 7 days");
        }
    }

    public List<SingleAsteroidResponse> topN(long number, Modes modes) {
        List<AsteroidTable> asteroids;
        if (modes == Modes.DIAMETER) {
            asteroids = asteroidDbService.findTopDiameter(number);
        } else {
            List<CloseApproachTable> closeApproaches = closeApproachDbService.findTopDistance(number);
            asteroids = closeApproaches.stream().map(data ->
                    asteroidDbService.findByReferenceId(data.getReferenceId())
            ).toList();
        }

        return asteroids.stream().map(feedsHelper::generateFeedResponse).toList();
    }
}