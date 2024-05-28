package id.swarawan.asteroid.database.service;

import id.swarawan.asteroid.config.utility.AppUtils;
import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.repository.CloseApproachRepository;
import id.swarawan.asteroid.model.api.data.CloseApproachApiData;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CloseApproachDbService {

    private final CloseApproachRepository closeApproachRepository;

    @Autowired
    public CloseApproachDbService(CloseApproachRepository closeApproachRepository) {
        this.closeApproachRepository = closeApproachRepository;
    }

    @Transactional
    public void save(List<CloseApproachApiData> closeApproaches) {
        List<CloseApproachTable> closeApproachData = closeApproaches.stream().map(data ->
                CloseApproachTable.builder()
                        .approachDate(data.getApproachDate())
                        .approachDateFull(data.getApproachDateFull())
                        .velocityKps(AppUtils.toDouble(data.getRelativeVelocity().getKilometerPerSecond(), 0.0))
                        .velocityKph(AppUtils.toDouble(data.getRelativeVelocity().getKilometerPerHour(), 0.0))
                        .velocityMph(AppUtils.toDouble(data.getRelativeVelocity().getMilesPerSecond(), 0.0))
                        .distanceAstronomical(AppUtils.toDouble(data.getMissDistance().getAstronomical(), 0.0))
                        .distanceLunar(AppUtils.toDouble(data.getMissDistance().getLunar(), 0.0))
                        .distanceKilometers(AppUtils.toDouble(data.getMissDistance().getKilometers(), 0.0))
                        .distanceMiles(AppUtils.toDouble(data.getMissDistance().getMiles(), 0.0))
                        .orbitingBody(data.getOrbitBody())
                        .build()).toList();
        closeApproachRepository.saveAll(closeApproachData);
    }

    public List<CloseApproachTable> getByAsteroid(Long asteroidId) {
        return closeApproachRepository.findByAsteroid(asteroidId);
    }
}
