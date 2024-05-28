package id.swarawan.asteroid.database.service;

import id.swarawan.asteroid.database.entity.AsteroidTable;
import id.swarawan.asteroid.database.repository.AsteroidDataRepository;
import id.swarawan.asteroid.model.api.data.AsteroidObjectApiData;
import id.swarawan.asteroid.model.response.NeoFeedResponse;
import id.swarawan.asteroid.model.response.item.NeoFeedItem;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AsteroidDbService {

    private final AsteroidDataRepository asteroidDataRepository;
    private final CloseApproachDbService closeApproachDbService;

    @Autowired
    public AsteroidDbService(AsteroidDataRepository asteroidDataRepository,
                             CloseApproachDbService closeApproachDbService) {
        this.asteroidDataRepository = asteroidDataRepository;
        this.closeApproachDbService = closeApproachDbService;
    }

    /**
     * Get the first and last date that is not in the database.
     * <p>
     * Asteroid data is a very large dataset, and it's a historical data that doesn't change in the future.
     * So returning a range date not in the database can lead to focus on processing new data.
     * <p>
     *
     * @param startDate start date requested by user
     * @param endDate   start date requested by user
     * @return range of date not in database
     */
    public Pair<LocalDate, LocalDate> getNotExistDate(LocalDate startDate, LocalDate endDate) {
        long numOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        List<LocalDate> notExistDateList = new ArrayList<>();
        for (int numOfDay = 0; numOfDay < numOfDays; numOfDay++) {
            boolean isExists = isExistByDate(startDate.plusDays(numOfDay));
            if (!isExists) {
                notExistDateList.add(startDate.plusDays(numOfDay));
            }
        }
        if (notExistDateList.isEmpty()) {
            return null;
        }
        return Pair.of(notExistDateList.get(0), notExistDateList.get(notExistDateList.size() - 1));
    }

    public boolean isExistByDate(LocalDate date) {
        return asteroidDataRepository.existsByDate(date.toString()) == 1;
    }

    @Transactional
    public List<AsteroidTable> save(Map<LocalDate, List<AsteroidObjectApiData>> apiData) {
        List<AsteroidTable> asteroidData = new ArrayList<>();
        apiData.forEach((date, asteroidObjectApiData) -> {
            boolean isExist = isExistByDate(date);
            if (!isExist) {
                asteroidObjectApiData.forEach(data -> asteroidData.add(convert(data)));
            }
        });
        return asteroidData;
    }

    private AsteroidTable convert(AsteroidObjectApiData apiData) {
        closeApproachDbService.save(apiData.getClosestApproaches());
        return AsteroidTable.builder()
                .referenceId(apiData.getId())
                .approachDate(apiData.getClosestApproaches().get(0).getApproachDate())
                .name(apiData.getName())
                .nasaJplUrl(apiData.getNasaJplUrl())
                .absoluteMagnitude(apiData.getAbsoluteMagnitude())
                .diameterKmMin(apiData.getEstimatedDiameter().getKilometers().getMin())
                .diameterKmMax(apiData.getEstimatedDiameter().getKilometers().getMax())
                .diameterMilesMin(apiData.getEstimatedDiameter().getMiles().getMin())
                .diameterMilesMax(apiData.getEstimatedDiameter().getMiles().getMax())
                .diameterFeetMin(apiData.getEstimatedDiameter().getFeet().getMin())
                .diameterFeetMax(apiData.getEstimatedDiameter().getFeet().getMax())
                .isHazardPotential(apiData.getIsHazardousAsteroid())
                .isSentryObject(apiData.getIsSentryObject())
                .build();
    }

    public List<AsteroidTable> getAllByRange(LocalDate startDate, LocalDate endDate) {
        return asteroidDataRepository.getAllByRange(startDate, endDate);
    }
}
