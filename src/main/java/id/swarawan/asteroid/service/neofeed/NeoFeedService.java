package id.swarawan.asteroid.service.neofeed;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.config.exceptions.NotFoundException;
import id.swarawan.asteroid.database.entity.AsteroidTable;
import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.service.AsteroidDbService;
import id.swarawan.asteroid.database.service.CloseApproachDbService;
import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import id.swarawan.asteroid.model.api.NeoLookupApiResponse;
import id.swarawan.asteroid.model.response.item.CloseApproachItem;
import id.swarawan.asteroid.model.response.item.DiameterItem;
import id.swarawan.asteroid.model.response.NeoLookupResponse;
import id.swarawan.asteroid.model.response.NeoFeedResponse;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import id.swarawan.asteroid.service.neolookup.NeoLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class NeoFeedService {

    private final NasaApiService nasaApiService;
    private final AsteroidDbService asteroidDbService;
    private final CloseApproachDbService closeApproachDbService;
    private final NeoLookupService neoLookupService;

    @Autowired
    public NeoFeedService(NasaApiService nasaApiService,
                          AsteroidDbService asteroidDbService,
                          CloseApproachDbService closeApproachDbService,
                          NeoLookupService neoLookupService) {
        this.nasaApiService = nasaApiService;
        this.asteroidDbService = asteroidDbService;
        this.closeApproachDbService = closeApproachDbService;
        this.neoLookupService = neoLookupService;
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
                        .asteroids(generateNeoLookup(dataTable))
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
                result.add(NeoFeedResponse.builder()
                        .date(date)
                        .asteroids(generateNeoLookup(dataTable))
                        .build());
            });
        }
        return result.stream().sorted(Comparator.comparing(NeoFeedResponse::getDate)).toList();
    }

    private Set<LocalDate> collectDates(LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> result = new HashSet<>();
        LocalDate tempDate = startDate;
        while (tempDate.isBefore(endDate) || tempDate.equals(endDate)) {
            result.add(tempDate);
            tempDate = tempDate.plusDays(1);
        }
        return result;
    }

    private List<NeoLookupResponse> generateNeoLookup(List<AsteroidTable> asteroids) {
        return asteroids.stream().map(data -> {
            List<CloseApproachTable> closeApproachTables = closeApproachDbService.findByReferenceId(data.getReferenceId());
            return neoLookupService.generateResponse(data, closeApproachTables);
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