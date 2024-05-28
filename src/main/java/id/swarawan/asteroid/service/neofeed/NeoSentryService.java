package id.swarawan.asteroid.service.neofeed;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.config.exceptions.NotFoundException;
import id.swarawan.asteroid.config.utility.AppUtils;
import id.swarawan.asteroid.model.api.NeoSentryApiResponse;
import id.swarawan.asteroid.model.response.NeoSentryResponse;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NeoSentryService {

    private final NasaApiService nasaApiService;

    @Autowired
    public NeoSentryService(NasaApiService nasaApiService) {
        this.nasaApiService = nasaApiService;
    }

    public NeoSentryResponse getNeoSentry(String referenceId) {
        NeoSentryResponse.NeoSentryResponseBuilder result = NeoSentryResponse.builder();

        validateRequest(referenceId);

        NeoSentryApiResponse apiResponse = nasaApiService.getNeoSentry(referenceId);
        if (Objects.isNull(apiResponse)) {
            throw new NotFoundException("Data not found");
        }

        result.spkId(apiResponse.getSpkId());
        result.designation(apiResponse.getDesignation());
        result.sentryId(apiResponse.getSentryId());
        result.fullName(apiResponse.getFullName());
        result.yearRangeMin(AppUtils.toInt(apiResponse.getYearRangeMin(), 0));
        result.yearRangeMax(AppUtils.toInt(apiResponse.getYearRangeMax(), 0));
        result.potentialImpacts(AppUtils.toInt(apiResponse.getPotentialImpacts(), 0));
        result.impactProbability(apiResponse.getImpactProbability());
        result.infinity(AppUtils.toDouble(apiResponse.getInfinity(), 0.0));
        result.absoluteMagnitude(AppUtils.toDouble(apiResponse.getAbsoluteMagnitude(), 0.0));
        result.estimatedDiameter(AppUtils.toDouble(apiResponse.getEstimatedDiameter(), 0.0));
        result.palermoScaleAve(AppUtils.toDouble(apiResponse.getPalermoScaleAve(), 0.0));
        result.palermoScaleMax(AppUtils.toDouble(apiResponse.getPalermoScaleMax(), 0.0));
        result.torinoScale(AppUtils.toInt(apiResponse.getTorinoScale(), 0));
        result.lastObs(apiResponse.getLastObs());
        result.lastObsJd(AppUtils.toDouble(apiResponse.getLastObsJd(), 0.0));
        result.urlImpactDetails(apiResponse.getUrlImpactDetails());
        result.urlOrbitalElementDetails(apiResponse.getUrlOrbitalElementDetails());

        return result.build();
    }

    private void validateRequest(String referenceId) {
        if (Objects.isNull(referenceId) || referenceId.isEmpty()) {
            throw new BadRequestException("Reference ID is required");
        }
    }
}
