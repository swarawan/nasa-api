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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public List<CloseApproachTable> findTopDistance(Long number) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00.0");
        List<Object[]> result = closeApproachRepository.findTopDistance(number);
        return result.stream().map(data -> CloseApproachTable.builder()
                .referenceId(String.valueOf(data[1]))
                .approachDate(LocalDate.parse(String.valueOf(data[2]), dateTimeFormatter))
                .approachDateFull(String.valueOf(data[3]))
                .approachDateEpoch(Long.parseLong(String.valueOf(data[4])))
                .velocityKps(Double.parseDouble(String.valueOf(data[5])))
                .velocityKph(Double.parseDouble(String.valueOf(data[6])))
                .velocityMph(Double.parseDouble(String.valueOf(data[7])))
                .distanceAstronomical(Double.parseDouble(String.valueOf(data[8])))
                .distanceLunar(Double.parseDouble(String.valueOf(data[9])))
                .distanceKilometers(Double.parseDouble(String.valueOf(data[10])))
                .distanceMiles(Double.parseDouble(String.valueOf(data[11])))
                .orbitingBody(String.valueOf(data[12]))
                .build()).toList();
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
