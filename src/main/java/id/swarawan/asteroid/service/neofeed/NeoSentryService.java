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
import java.util.Optional;

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

        Optional<NeoSentryApiResponse> apiResponse = nasaApiService.getNeoSentry(referenceId);
        if (apiResponse.isEmpty()) {
            return null;
        }
        NeoSentryApiResponse apiData = apiResponse.get();
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
