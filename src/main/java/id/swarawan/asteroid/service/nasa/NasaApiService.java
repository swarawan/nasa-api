package id.swarawan.asteroid.service.nasa;

import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NasaApiService {

    private NasaApi nasaApi;

    @Autowired
    public NasaApiService(NasaApi nasaApi) {
        this.nasaApi = nasaApi;
    }

    public NeoFeedApiResponse getNeoFeedApi(String key, LocalDate startDate, LocalDate endDate) {
        return nasaApi.getNeoFeedApi(key, startDate, endDate);
    }
}
