package id.swarawan.asteroid.service.neofeed;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.config.exceptions.NotFoundException;
import id.swarawan.asteroid.config.utility.AppUtils;
import id.swarawan.asteroid.database.entity.SentryTable;
import id.swarawan.asteroid.database.repository.SentryDataRepository;
import id.swarawan.asteroid.model.api.NeoSentryApiResponse;
import id.swarawan.asteroid.model.response.NeoSentryResponse;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NeoSentryService {

    private final NasaApiService nasaApiService;
    private final SentryDataRepository sentryDataRepository;

    @Autowired
    public NeoSentryService(NasaApiService nasaApiService,
                            SentryDataRepository sentryDataRepository) {
        this.nasaApiService = nasaApiService;
        this.sentryDataRepository = sentryDataRepository;
    }

    public NeoSentryResponse getNeoSentry(String referenceId) {
        validateRequest(referenceId);

        SentryTable sentryTables = sentryDataRepository.findBySpkId(referenceId);
        if (!Objects.isNull(sentryTables)) {
            return convert(sentryTables);
        }

        NeoSentryApiResponse apiResponse = nasaApiService.getNeoSentry(referenceId);
        if (Objects.isNull(apiResponse)) {
            return null;
        }
        SentryTable newSentry = SentryTable.builder()
                .spkId(apiResponse.getSpkId())
                .designation(apiResponse.getDesignation())
                .sentryId(apiResponse.getSentryId())
                .name(apiResponse.getFullName())
                .yearRangeMin(AppUtils.toInt(apiResponse.getYearRangeMin(), 0))
                .yearRangeMax(AppUtils.toInt(apiResponse.getYearRangeMax(), 0))
                .potentialImpact(AppUtils.toInt(apiResponse.getPotentialImpacts(), 0))
                .impactProbability(apiResponse.getImpactProbability())
                .vInfinite(AppUtils.toDouble(apiResponse.getInfinity(), 0.0))
                .absoluteMagnitude(AppUtils.toDouble(apiResponse.getAbsoluteMagnitude(), 0.0))
                .estimatedDiameter(AppUtils.toDouble(apiResponse.getEstimatedDiameter(), 0.0))
                .palermoScaleAve(AppUtils.toDouble(apiResponse.getPalermoScaleAve(), 0.0))
                .palermoScaleMax(AppUtils.toDouble(apiResponse.getPalermoScaleMax(), 0.0))
                .torinoScale(AppUtils.toInt(apiResponse.getTorinoScale(), 0))
                .lastObs(apiResponse.getLastObs())
                .lastObsJd(AppUtils.toDouble(apiResponse.getLastObsJd(), 0.0))
                .impactDetailsUrl(apiResponse.getUrlImpactDetails())
                .orbitalElementDetailsUrl(apiResponse.getUrlOrbitalElementDetails())
                .build();
        sentryDataRepository.save(newSentry);
        return convert(newSentry);
    }

    private NeoSentryResponse convert(SentryTable data) {
        return NeoSentryResponse.builder()
                .spkId(data.getSpkId())
                .designation(data.getDesignation())
                .sentryId(data.getSentryId())
                .fullName(data.getName())
                .yearRangeMin(data.getYearRangeMin())
                .yearRangeMax(data.getYearRangeMax())
                .potentialImpacts(data.getPotentialImpact())
                .impactProbability(data.getImpactProbability())
                .infinity(data.getVInfinite())
                .absoluteMagnitude(data.getAbsoluteMagnitude())
                .estimatedDiameter(data.getEstimatedDiameter())
                .palermoScaleAve(data.getPalermoScaleAve())
                .palermoScaleMax(data.getPalermoScaleMax())
                .torinoScale(data.getTorinoScale())
                .lastObs(data.getLastObs())
                .lastObsJd(data.getLastObsJd())
                .urlImpactDetails(data.getImpactDetailsUrl())
                .urlOrbitalElementDetails(data.getOrbitalElementDetailsUrl())
                .build();

    }

    private void validateRequest(String referenceId) {
        if (Objects.isNull(referenceId) || referenceId.isEmpty()) {
            throw new BadRequestException("Reference ID is required");
        }
    }
}
