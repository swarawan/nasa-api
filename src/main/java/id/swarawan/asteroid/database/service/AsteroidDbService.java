package id.swarawan.asteroid.database.service;

import id.swarawan.asteroid.database.entity.AsteroidTable;
import id.swarawan.asteroid.database.repository.AsteroidDataRepository;
import id.swarawan.asteroid.model.response.NeoFeedResponse;
import id.swarawan.asteroid.model.response.item.NeoFeedItem;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AsteroidDbService {

    private AsteroidDataRepository asteroidDataRepository;
    private CloseApproachDbService closeApproachDbService;

    @Autowired
    public AsteroidDbService(AsteroidDataRepository asteroidDataRepository,
                             CloseApproachDbService closeApproachDbService) {
        this.asteroidDataRepository = asteroidDataRepository;
        this.closeApproachDbService = closeApproachDbService;
    }

    @Transactional
    public void save(List<NeoFeedResponse> neoFeedResponses) {
        List<AsteroidTable> asteroidData = new ArrayList<>();
        neoFeedResponses.stream()
                .filter(data -> !asteroidDataRepository.existsByDate(data.getDate()))
                .filter(data -> !data.getAsteroids().isEmpty())
                .forEach(data -> data.getAsteroids()
                        .stream()
                        .forEach(asteroid -> asteroidData.add(convert(asteroid))));

        asteroidDataRepository.saveAll(asteroidData);
    }

    private AsteroidTable convert(NeoFeedItem asteroid) {
        closeApproachDbService.save(asteroid.getCloseApproaches());
        return AsteroidTable.builder()
                .referenceId(asteroid.getId())
                .approachDate(asteroid.getCloseApproaches().get(0).getApproachDate())
                .approachDateFull(asteroid.getCloseApproaches().get(0).getApproachDateFull())
                .name(asteroid.getName())
                .nasaJplUrl(asteroid.getJplUrl())
                .absoluteMagnitude(asteroid.getAbsoluteMagnitude())
                .diameterKmMin(asteroid.getEstimatedDiameterKm().getDiameterMin())
                .diameterKmMax(asteroid.getEstimatedDiameterKm().getDiameterMax())
                .diameterMilesMin(asteroid.getEstimatedDiameterMiles().getDiameterMin())
                .diameterMilesMax(asteroid.getEstimatedDiameterMiles().getDiameterMax())
                .diameterFeetMin(asteroid.getEstimatedDiameterFeet().getDiameterMin())
                .diameterFeetMax(asteroid.getEstimatedDiameterFeet().getDiameterMax())
                .isHazardPotential(asteroid.getIsHazardAsteroid())
                .build();
    }
}
