package id.swarawan.asteroid.service.feeds;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.database.entity.SentryTable;
import id.swarawan.asteroid.database.service.SentryDbService;
import id.swarawan.asteroid.model.api.NeoSentryApiResponse;
import id.swarawan.asteroid.model.response.SentryResponse;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class SentryService {

    private final NasaApiService nasaApiService;
    private final SentryDbService sentryDbService;

    @Autowired
    public SentryService(NasaApiService nasaApiService,
                         SentryDbService sentryDbService) {
        this.nasaApiService = nasaApiService;
        this.sentryDbService = sentryDbService;
    }

    public SentryResponse getNeoSentry(String referenceId) {
        validateRequest(referenceId);

        SentryTable sentryTables = sentryDbService.findByReference(referenceId);
        if (!Objects.isNull(sentryTables)) {
            return convert(sentryTables);
        }

        Optional<NeoSentryApiResponse> apiResponse = nasaApiService.getNeoSentry(referenceId);
        if (apiResponse.isEmpty()) {
            return null;
        }
        NeoSentryApiResponse apiData = apiResponse.get();
        SentryTable newSentry = sentryDbService.save(apiData);
        return convert(newSentry);
    }

    private SentryResponse convert(SentryTable data) {
        return SentryResponse.builder()
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
