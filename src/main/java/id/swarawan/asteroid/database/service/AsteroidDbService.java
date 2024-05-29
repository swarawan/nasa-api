package id.swarawan.asteroid.database.service;

import id.swarawan.asteroid.config.exceptions.NotFoundException;
import id.swarawan.asteroid.database.entity.AsteroidTable;
import id.swarawan.asteroid.database.repository.AsteroidDataRepository;
import id.swarawan.asteroid.database.repository.SentryDataRepository;
import id.swarawan.asteroid.model.api.NeoLookupApiResponse;
import id.swarawan.asteroid.service.feeds.SentryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AsteroidDbService {

    private final AsteroidDataRepository asteroidDataRepository;
    private final CloseApproachDbService closeApproachDbService;
    private final SentryDbService sentryDbService;
    private final OrbitDataDbService orbitDataDbService;

    @Autowired
    public AsteroidDbService(AsteroidDataRepository asteroidDataRepository,
                             CloseApproachDbService closeApproachDbService,
                             SentryDbService sentryDbService, OrbitDataDbService orbitDataDbService) {
        this.asteroidDataRepository = asteroidDataRepository;
        this.closeApproachDbService = closeApproachDbService;
        this.sentryDbService = sentryDbService;
        this.orbitDataDbService = orbitDataDbService;
    }

    public boolean isExistByDate(LocalDate date) {
        return asteroidDataRepository.existsByDate(date.toString()) != 0;
    }

    @Transactional
    public AsteroidTable save(NeoLookupApiResponse apiData) {
        AsteroidTable asteroidData = convert(apiData);
        asteroidDataRepository.save(asteroidData);

        return asteroidData;
    }

    @Transactional
    public void saveAll(Map<LocalDate, List<NeoLookupApiResponse>> apiData) {
        List<AsteroidTable> asteroidData = new ArrayList<>();
        apiData.forEach((date, asteroidObjectApiData) -> {
            boolean isExist = isExistByDate(date);
            if (!isExist) {
                asteroidObjectApiData.forEach(data -> asteroidData.add(convert(data)));
            }
        });
        asteroidDataRepository.saveAll(asteroidData);
    }

    public List<AsteroidTable> findByApproachDate(LocalDate date) {
        return asteroidDataRepository.findByApproachDate(date);
    }

    public AsteroidTable findByReferenceId(String referenceId) {
        return asteroidDataRepository.findByReferenceId(referenceId);
    }

    public List<AsteroidTable> findTopDiameter(long limit) {
        return asteroidDataRepository.findTopDiameter(limit);
    }

    @Transactional
    public void delete(String referenceId) {
        AsteroidTable asteroidData = findByReferenceId(referenceId);
        if (Objects.isNull(asteroidData)) {
            return;
        }

        asteroidDataRepository.delete(asteroidData);

        sentryDbService.delete(referenceId);
        orbitDataDbService.delete(referenceId);
        closeApproachDbService.delete(referenceId);
    }


    private AsteroidTable convert(NeoLookupApiResponse apiData) {
        closeApproachDbService.save(apiData.getReferenceId(), apiData.getClosestApproaches());
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
}
