package id.swarawan.asteroid.database.service;

import id.swarawan.asteroid.config.exceptions.NotFoundException;
import id.swarawan.asteroid.config.utility.AppUtils;
import id.swarawan.asteroid.database.entity.SentryTable;
import id.swarawan.asteroid.database.repository.SentryDataRepository;
import id.swarawan.asteroid.model.api.NeoSentryApiResponse;
import id.swarawan.asteroid.service.feeds.SentryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class SentryDbService {

    private final SentryDataRepository sentryDataRepository;

    @Autowired
    public SentryDbService(SentryDataRepository sentryDataRepository) {
        this.sentryDataRepository = sentryDataRepository;
    }

    @Transactional
    public SentryTable save(NeoSentryApiResponse apiData) {
        SentryTable newSentry = SentryTable.builder()
                .spkId(apiData.getSpkId())
                .designation(apiData.getDesignation())
                .sentryId(apiData.getSentryId())
                .name(apiData.getFullName())
                .yearRangeMin(AppUtils.toInt(apiData.getYearRangeMin(), 0))
                .yearRangeMax(AppUtils.toInt(apiData.getYearRangeMax(), 0))
                .potentialImpact(AppUtils.toInt(apiData.getPotentialImpacts(), 0))
                .impactProbability(apiData.getImpactProbability())
                .vInfinite(AppUtils.toDouble(apiData.getInfinity(), 0.0))
                .absoluteMagnitude(AppUtils.toDouble(apiData.getAbsoluteMagnitude(), 0.0))
                .estimatedDiameter(AppUtils.toDouble(apiData.getEstimatedDiameter(), 0.0))
                .palermoScaleAve(AppUtils.toDouble(apiData.getPalermoScaleAve(), 0.0))
                .palermoScaleMax(AppUtils.toDouble(apiData.getPalermoScaleMax(), 0.0))
                .torinoScale(AppUtils.toInt(apiData.getTorinoScale(), 0))
                .lastObs(apiData.getLastObs())
                .lastObsJd(AppUtils.toDouble(apiData.getLastObsJd(), 0.0))
                .impactDetailsUrl(apiData.getUrlImpactDetails())
                .orbitalElementDetailsUrl(apiData.getUrlOrbitalElementDetails())
                .build();
        sentryDataRepository.save(newSentry);
        return newSentry;
    }

    @Transactional
    public void delete(String referenceId) {
        SentryTable sentryTable = findByReference(referenceId);
        if (Objects.isNull(sentryTable)) {
            return;
        }

        sentryDataRepository.delete(sentryTable);
    }

    public SentryTable findByReference(String referenceId) {
        return sentryDataRepository.findBySpkId(referenceId);
    }
}
