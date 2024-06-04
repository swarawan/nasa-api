package id.swarawan.asteroid.scheduler;

import id.swarawan.asteroid.database.service.AsteroidDbService;
import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class AppScheduler {

    private NasaApiService nasaApiService;
    private AsteroidDbService asteroidDbService;

    @Autowired
    public AppScheduler(NasaApiService nasaApiService,
                        AsteroidDbService asteroidDbService) {
        this.nasaApiService = nasaApiService;
        this.asteroidDbService = asteroidDbService;
    }

    @Scheduled(cron = "0 53 15 * * *")
    public void weeklyFeedSchedule() {
        System.out.println("Scheduler run");
        LocalDate now = LocalDate.now();

        Optional<NeoFeedApiResponse> response = nasaApiService.getNeoFeedApi(now, now);
        if (response.isEmpty()) {
            System.out.println("Data not found");
            return;
        }
        NeoFeedApiResponse apiResponse = response.get();
        if (apiResponse.getElementCount() <= 0) {
            System.out.println("Data is empty");
            return;
        }
        asteroidDbService.saveAll(apiResponse.getNearEarthObjects());
    }

}
