package id.swarawan.asteroid.database.service;

import id.swarawan.asteroid.config.exceptions.NotFoundException;
import id.swarawan.asteroid.config.utility.AppUtils;
import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.entity.SentryTable;
import id.swarawan.asteroid.database.repository.CloseApproachRepository;
import id.swarawan.asteroid.model.api.data.CloseApproachApiData;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CloseApproachDbService {

    private final CloseApproachRepository closeApproachRepository;

    @Autowired
    public CloseApproachDbService(CloseApproachRepository closeApproachRepository) {
        this.closeApproachRepository = closeApproachRepository;
    }

    @Transactional
    public void save(String referenceId, List<CloseApproachApiData> closeApproaches) {
        List<CloseApproachTable> closeApproachData = closeApproaches.stream()
                .filter(data -> !existByReferenceIdAndEpoch(referenceId, data.getApproachDateEpoch()))
                .map(data -> CloseApproachTable.builder()
                        .referenceId(referenceId)
                        .approachDate(data.getApproachDate())
                        .approachDateFull(data.getApproachDateFull())
                        .approachDateEpoch(data.getApproachDateEpoch())
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

    public List<CloseApproachTable> findByReferenceId(String referenceId) {
        return closeApproachRepository.findByReferenceId(referenceId);
    }

    public boolean existByReferenceIdAndEpoch(String referenceId, Long epoch) {
        return closeApproachRepository.existByReferenceIdAndEpoch(referenceId, epoch) == 1;
    }

    @Transactional
    public void delete(String referenceId) {
        List<CloseApproachTable> closeApproachTable = findByReferenceId(referenceId);
        if (Objects.isNull(closeApproachTable)) {
            return;
        }

        closeApproachRepository.deleteAll(closeApproachTable);
    }
}
