package id.swarawan.asteroid.service.nasa;

import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import id.swarawan.asteroid.model.api.NeoLookupApiResponse;
import id.swarawan.asteroid.model.api.NeoSentryApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class NasaApiService {

    @Value("${nasa-api}")
    private String nasaApiKey;

    private final NasaApi nasaApi;

    @Autowired
    public NasaApiService(NasaApi nasaApi) {
        this.nasaApi = nasaApi;
    }

    public Optional<NeoFeedApiResponse> getNeoFeedApi(LocalDate startDate, LocalDate endDate) {
        return Optional.of(nasaApi.getNeoFeedApi(nasaApiKey, startDate.toString(), endDate.toString()));
    }

    public Optional<NeoSentryApiResponse> getNeoSentry(String referenceId) {
        return Optional.of(nasaApi.getNeoSentry(nasaApiKey, referenceId));
    }

    public Optional<NeoLookupApiResponse> getNeoLookUp(String referenceId) {
        return Optional.of(nasaApi.getNeoLookup(nasaApiKey, referenceId));
    }
}
